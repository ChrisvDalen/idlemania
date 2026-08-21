package com.idlemania.entity;

import com.idlemania.model.GeneratorType;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "generator_states")
public class GeneratorState {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_state_id", nullable = false)
    private GameState gameState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GeneratorType type;

    @Column(nullable = false)
    private int count = 0;

    public UUID getId() {
        return id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GeneratorType getType() {
        return type;
    }

    public void setType(GeneratorType type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
