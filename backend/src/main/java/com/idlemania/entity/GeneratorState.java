package com.idlemania.entity;

import com.idlemania.model.GeneratorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "generator_states")
@Getter
@Setter
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
}
