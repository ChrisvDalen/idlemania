package com.idlemania.dto;

public record GeneratorStateDto(
        String type,
        String displayName,
        int count,
        double nextCost,
        double incomePerSecond
) {
}
