package com.idlemania.controller;

import com.idlemania.dto.CreatePlayerRequest;
import com.idlemania.dto.PlayerResponse;
import com.idlemania.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final GameService gameService;

    public PlayerController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(@RequestBody CreatePlayerRequest request) {
        return ResponseEntity.ok(gameService.createPlayer(request.name()));
    }
}
