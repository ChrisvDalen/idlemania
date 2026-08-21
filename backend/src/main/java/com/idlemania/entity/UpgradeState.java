package com.idlemania.entity;

import com.idlemania.model.UpgradeType;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "upgrade_states")
public class UpgradeState {

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
    private UpgradeType type;

    @Column(nullable = false)
    private boolean purchased = false;

    public UUID getId() {
        return id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public UpgradeType getType() {
        return type;
    }

    public void setType(UpgradeType type) {
        this.type = type;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
}
