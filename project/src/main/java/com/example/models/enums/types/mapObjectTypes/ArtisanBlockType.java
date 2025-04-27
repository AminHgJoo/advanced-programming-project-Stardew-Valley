package com.example.models.enums.types.mapObjectTypes;

public enum ArtisanBlockType {
    SHIPPING_BIN("Shipping Bin"),
    CHARCOAL_KLIN("Charcoal Klin"),
    FURNACE("Furnace"),
    SCARE_CROW("Scare Crow"),
    DELUXE_SCARE_CROW("Deluxe Scarecrow"),
    BEE_HOUSE("Bee House"),
    CHEESE_PRESS("Cheese Press"),
    KEG("Keg"),
    LOOM("Loom"),
    MAYONNAISE_MACHINE("Mayonnaise Machine"),
    OIL_MAKER("Oil Maker"),
    PRESERVES_JAR("Preserves Jar"),
    DEHYDRATOR("Dehydrator"),
    FISH_SMOKER("Fish Smoker");

    public final String name;

    ArtisanBlockType(String name) {
        this.name = name;
    }

    public static ArtisanBlockType getArtisanBlockTypeByName(String name) {
        ArtisanBlockType[] values = ArtisanBlockType.values();
        for (ArtisanBlockType value : values) {
            if (value.name.compareToIgnoreCase(name) == 0) {
                return value;
            }
        }
        return null;
    }
}
