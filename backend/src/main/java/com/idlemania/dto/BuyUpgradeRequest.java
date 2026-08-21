package com.idlemania.dto;

import com.idlemania.model.UpgradeType;
import jakarta.validation.constraints.NotNull;

public record BuyUpgradeRequest(@NotNull UpgradeType upgradeType) {
}
