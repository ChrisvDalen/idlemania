package com.idlemania.service;

import com.idlemania.dto.*;
import com.idlemania.entity.GameState;
import com.idlemania.entity.GeneratorState;
import com.idlemania.entity.Player;
import com.idlemania.entity.UpgradeState;
import com.idlemania.model.GeneratorType;
import com.idlemania.model.UpgradeType;
import com.idlemania.repository.GameStateRepository;
import com.idlemania.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameService {

    private final PlayerRepository playerRepository;
    private final GameStateRepository gameStateRepository;

    public GameService(PlayerRepository playerRepository, GameStateRepository gameStateRepository) {
        this.playerRepository = playerRepository;
        this.gameStateRepository = gameStateRepository;
    }

    public PlayerResponse createPlayer(String name) {
        Player player = new Player();
        player.setName(name);
        playerRepository.save(player);

        GameState state = new GameState();
        state.setPlayer(player);

        for (GeneratorType type : GeneratorType.values()) {
            GeneratorState gen = new GeneratorState();
            gen.setGameState(state);
            gen.setType(type);
            state.getGenerators().add(gen);
        }

        for (UpgradeType type : UpgradeType.values()) {
            UpgradeState upg = new UpgradeState();
            upg.setGameState(state);
            upg.setType(type);
            state.getUpgrades().add(upg);
        }

        gameStateRepository.save(state);
        return new PlayerResponse(player.getId(), player.getName());
    }

    public GameStateResponse getState(UUID playerId) {
        GameState state = findAndUpdate(playerId);
        return toResponse(state);
    }

    public GameStateResponse collectCoins(UUID playerId) {
        GameState state = findAndUpdate(playerId);
        state.setCoins(state.getCoins() + 1.0);
        state.setTotalCoinsEarned(state.getTotalCoinsEarned() + 1.0);
        return toResponse(state);
    }

    public GameStateResponse buyGenerator(UUID playerId, GeneratorType type) {
        GameState state = findAndUpdate(playerId);
        GeneratorState gen = findGenerator(state, type);

        double cost = nextCost(type, gen.getCount());
        if (state.getCoins() < cost) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough coins");
        }

        state.setCoins(state.getCoins() - cost);
        gen.setCount(gen.getCount() + 1);
        return toResponse(state);
    }

    public GameStateResponse buyUpgrade(UUID playerId, UpgradeType type) {
        GameState state = findAndUpdate(playerId);
        UpgradeState upg = findUpgrade(state, type);

        if (upg.isPurchased()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already purchased");
        }
        if (state.getCoins() < type.cost) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough coins");
        }

        state.setCoins(state.getCoins() - type.cost);
        upg.setPurchased(true);
        return toResponse(state);
    }

    // --- helpers ---

    private GameState findAndUpdate(UUID playerId) {
        GameState state = gameStateRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
        applyOfflineProgress(state);
        return state;
    }

    private void applyOfflineProgress(GameState state) {
        LocalDateTime now = LocalDateTime.now();
        // Cap offline progress at 24 hours to prevent exploits
        long secondsElapsed = Math.min(
                ChronoUnit.SECONDS.between(state.getLastUpdated(), now),
                86400L
        );
        if (secondsElapsed > 0) {
            double earned = incomePerSecond(state) * secondsElapsed;
            state.setCoins(state.getCoins() + earned);
            state.setTotalCoinsEarned(state.getTotalCoinsEarned() + earned);
        }
        state.setLastUpdated(now);
    }

    private double incomePerSecond(GameState state) {
        Set<UpgradeType> purchased = state.getUpgrades().stream()
                .filter(UpgradeState::isPurchased)
                .map(UpgradeState::getType)
                .collect(Collectors.toSet());

        double total = 0;
        for (GeneratorState gen : state.getGenerators()) {
            if (gen.getCount() == 0) continue;
            double income = gen.getType().incomePerSecond * gen.getCount();
            for (UpgradeType upg : UpgradeType.values()) {
                if (upg.targetGenerator == gen.getType() && purchased.contains(upg)) {
                    income *= upg.multiplier;
                }
            }
            total += income;
        }
        return total;
    }

    private double nextCost(GeneratorType type, int currentCount) {
        return type.baseCost * Math.pow(1.15, currentCount);
    }

    private GeneratorState findGenerator(GameState state, GeneratorType type) {
        return state.getGenerators().stream()
                .filter(g -> g.getType() == type)
                .findFirst()
                .orElseThrow();
    }

    private UpgradeState findUpgrade(GameState state, UpgradeType type) {
        return state.getUpgrades().stream()
                .filter(u -> u.getType() == type)
                .findFirst()
                .orElseThrow();
    }

    private GameStateResponse toResponse(GameState state) {
        Set<UpgradeType> purchased = state.getUpgrades().stream()
                .filter(UpgradeState::isPurchased)
                .map(UpgradeState::getType)
                .collect(Collectors.toSet());

        List<GeneratorStateDto> generators = state.getGenerators().stream()
                .sorted(Comparator.comparingInt(g -> g.getType().ordinal()))
                .map(g -> {
                    double multiplier = 1.0;
                    for (UpgradeType upg : UpgradeType.values()) {
                        if (upg.targetGenerator == g.getType() && purchased.contains(upg)) {
                            multiplier *= upg.multiplier;
                        }
                    }
                    double ips = g.getType().incomePerSecond * g.getCount() * multiplier;
                    return new GeneratorStateDto(
                            g.getType().name(),
                            g.getType().displayName,
                            g.getCount(),
                            round(nextCost(g.getType(), g.getCount()), 2),
                            round(ips, 3)
                    );
                })
                .toList();

        List<UpgradeStateDto> upgrades = state.getUpgrades().stream()
                .sorted(Comparator.comparingInt(u -> u.getType().ordinal()))
                .map(u -> new UpgradeStateDto(
                        u.getType().name(),
                        u.getType().displayName,
                        u.getType().cost,
                        u.isPurchased(),
                        u.getType().targetGenerator.name(),
                        u.getType().multiplier
                ))
                .toList();

        double totalIps = generators.stream().mapToDouble(GeneratorStateDto::incomePerSecond).sum();

        return new GameStateResponse(
                state.getPlayer().getId(),
                state.getPlayer().getName(),
                round(state.getCoins(), 2),
                round(state.getTotalCoinsEarned(), 2),
                round(totalIps, 3),
                generators,
                upgrades
        );
    }

    private double round(double value, int places) {
        double factor = Math.pow(10, places);
        return Math.round(value * factor) / factor;
    }
}
