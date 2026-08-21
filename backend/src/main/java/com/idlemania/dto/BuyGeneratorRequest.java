package com.idlemania.dto;

import com.idlemania.model.GeneratorType;
import jakarta.validation.constraints.NotNull;

public record BuyGeneratorRequest(@NotNull GeneratorType generatorType) {
}
