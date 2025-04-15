package com.example.models.enums;

public enum Quality {
    COPPER("Copper"),
    SILVER("Silver"),
    GOLD("Gold"),
    IRIDIUM("Iridium"),;

    private final String name;
    private final int qualityLevel;

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
