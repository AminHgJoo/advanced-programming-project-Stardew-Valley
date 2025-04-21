package com.example.models.enums;

public enum Quality {
    /// Only for a few tools, not items.
    DEFAULT("Default"),
    /// all below for fishing rod corresponds to : training - bamboo - fiberglass - iridium rods.
    COPPER("Copper"),
    SILVER("Silver"),
    GOLD("Gold"),
    IRIDIUM("Iridium"),
    ;

    private final String name;
    private final int qualityLevel;

    /// returns ordinal value.
    public int getQualityLevel() {
        return qualityLevel;
    }

    Quality(String name) {
        this.name = name;
        this.qualityLevel = ordinal();
    }

    @Override
    public String toString() {
        return name;
    }
}
