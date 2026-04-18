package com.idlemania.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_states")
@Getter
@Setter
public class GameState {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private double coins = 0.0;

    @Column(nullable = false)
    private double totalCoinsEarned = 0.0;

    @Column(nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @OneToMany(mappedBy = "gameState", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<GeneratorState> generators = new ArrayList<>();

    @OneToMany(mappedBy = "gameState", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UpgradeState> upgrades = new ArrayList<>();
}
