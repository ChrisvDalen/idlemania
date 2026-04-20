package com.idlemania.repository;

import com.idlemania.entity.GameState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameStateRepository extends JpaRepository<GameState, UUID> {
    Optional<GameState> findByPlayerId(UUID playerId);
}
