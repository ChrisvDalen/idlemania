package com.idlemania.controller;

import com.idlemania.dto.BuyGeneratorRequest;
import com.idlemania.dto.BuyUpgradeRequest;
import com.idlemania.dto.GameStateResponse;
import com.idlemania.service.GameService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{playerId}")
    public GameStateResponse getState(@PathVariable UUID playerId) {
        return gameService.getState(playerId);
    }

    @PostMapping("/{playerId}/collect")
    public GameStateResponse collectCoins(@PathVariable UUID playerId) {
        return gameService.collectCoins(playerId);
    }

    @PostMapping("/{playerId}/buy/generator")
    public GameStateResponse buyGenerator(
            @PathVariable UUID playerId,
            @Valid @RequestBody BuyGeneratorRequest request) {
        return gameService.buyGenerator(playerId, request.generatorType());
    }

    @PostMapping("/{playerId}/buy/upgrade")
    public GameStateResponse buyUpgrade(
            @PathVariable UUID playerId,
            @Valid @RequestBody BuyUpgradeRequest request) {
        return gameService.buyUpgrade(playerId, request.upgradeType());
    }
}
