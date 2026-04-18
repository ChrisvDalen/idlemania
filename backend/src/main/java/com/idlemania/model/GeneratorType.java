package com.idlemania.model;

public enum GeneratorType {
    BAGGAGE_CART("Baggage Cart", 10.0, 0.1),
    GATE_DESK("Gate Desk", 100.0, 0.5),
    RUNWAY_CREW("Runway Crew", 1000.0, 4.0);

    public final String displayName;
    public final double baseCost;
    public final double incomePerSecond;

    GeneratorType(String displayName, double baseCost, double incomePerSecond) {
        this.displayName = displayName;
        this.baseCost = baseCost;
        this.incomePerSecond = incomePerSecond;
    }
}
