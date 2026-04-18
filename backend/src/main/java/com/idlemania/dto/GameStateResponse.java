package com.idlemania.dto;

import java.util.List;
import java.util.UUID;

public record GameStateResponse(
        UUID playerId,
        String playerName,
        double coins,
        double totalCoinsEarned,
        double incomePerSecond,
        List<GeneratorStateDto> generators,
        List<UpgradeStateDto> upgrades
) {
}
