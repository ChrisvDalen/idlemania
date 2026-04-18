package com.idlemania.model;

public enum UpgradeType {
    TURBO_BAGGAGE("Turbo Baggage", GeneratorType.BAGGAGE_CART, 2.0, 50.0),
    VIP_SERVICE("VIP Service", GeneratorType.GATE_DESK, 2.0, 500.0),
    JET_ENGINES("Jet Engines", GeneratorType.RUNWAY_CREW, 2.0, 5000.0);

    public final String displayName;
    public final GeneratorType targetGenerator;
    public final double multiplier;
    public final double cost;

    UpgradeType(String displayName, GeneratorType targetGenerator, double multiplier, double cost) {
        this.displayName = displayName;
        this.targetGenerator = targetGenerator;
        this.multiplier = multiplier;
        this.cost = cost;
    }
}
