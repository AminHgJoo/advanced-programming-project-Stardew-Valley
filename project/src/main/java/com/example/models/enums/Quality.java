package com.example.models.enums;

public enum Quality {
    /// Only for a few tools, not items.
    DEFAULT("Default", 1),
    /// all below for fishing rod corresponds to : training - bamboo - fiberglass - iridium rods.
    COPPER("Copper", 1),
    SILVER("Silver", 1.25),
    GOLD("Gold", 1.5),
    IRIDIUM("Iridium", 2),
    ;

    private final String name;
    private final int qualityLevel;
    private final double priceFactor;

    /// returns ordinal value.
    public int getQualityLevel() {
        return qualityLevel;
    }

    Quality(String name, double priceFactor) {
        this.name = name;
        this.priceFactor = priceFactor;
        this.qualityLevel = ordinal();
    }

    public double getPriceFactor() {
        return priceFactor;
    }

    @Override
    public String toString() {
        return name;
    }
}
