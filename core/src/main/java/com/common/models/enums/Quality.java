package com.common.models.enums;

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

    Quality(String name, double priceFactor) {
        this.name = name;
        this.priceFactor = priceFactor;
        this.qualityLevel = ordinal();
    }

    public static Quality getQualityByName(final String name) {
        for (Quality quality : values()) {
            if (quality.name.compareToIgnoreCase(name) == 0) {
                return quality;
            }
        }

        if (name.compareToIgnoreCase("Training") == 0) {
            return Quality.COPPER;
        }
        if (name.compareToIgnoreCase("Bamboo") == 0) {
            return Quality.SILVER;
        }
        if (name.compareToIgnoreCase("Fiberglass") == 0) {
            return Quality.GOLD;
        }

        return null;
    }

    /// returns ordinal value.
    public int getQualityLevel() {
        return qualityLevel;
    }

    public double getPriceFactor() {
        return priceFactor;
    }

    @Override
    public String toString() {
        return name;
    }
}
