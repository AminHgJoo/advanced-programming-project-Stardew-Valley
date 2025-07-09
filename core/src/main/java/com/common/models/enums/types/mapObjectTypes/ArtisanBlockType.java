package com.common.models.enums.types.mapObjectTypes;

public enum ArtisanBlockType {
    SHIPPING_BIN("Shipping Bin" , "Shipping_Bin"),
    CHARCOAL_KLIN("Charcoal Klin" , "Charcoal_Klin"),
    FURNACE("Furnace" , "Furnace"),
    SCARE_CROW("Scare Crow" , "Scarecrow"),
    DELUXE_SCARE_CROW("Deluxe Scarecrow" , "Deluxe_Scarecrow"),
    BEE_HOUSE("Bee House" , "Bee_House"),
    CHEESE_PRESS("Cheese Press" , "Cheese_Press"),
    KEG("Keg" , "Keg"),
    LOOM("Loom" , "Loom"),
    MAYONNAISE_MACHINE("Mayonnaise Machine" , "Mayonnaise_Machine"),
    OIL_MAKER("Oil Maker" , "Oil_Maker"),
    PRESERVES_JAR("Preserves Jar" , "Preserves_Jar"),
    DEHYDRATOR("Dehydrator" , "Dehydrator"),
    FISH_SMOKER("Fish Smoker" , "Fish_Smoker"),;

    public final String name;
    private final String textureName;

    ArtisanBlockType(String name, String textureName) {
        this.textureName = textureName;
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
