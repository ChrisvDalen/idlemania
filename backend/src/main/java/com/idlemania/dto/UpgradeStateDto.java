package com.idlemania.dto;

public record UpgradeStateDto(
        String type,
        String displayName,
        double cost,
        boolean purchased,
        String targetGenerator,
        double multiplier
) {
}
