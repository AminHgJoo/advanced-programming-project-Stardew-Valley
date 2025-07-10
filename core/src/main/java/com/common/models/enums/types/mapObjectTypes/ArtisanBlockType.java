package com.common.models.enums.types.mapObjectTypes;

public enum ArtisanBlockType {
    SHIPPING_BIN("Shipping Bin" , "shippingBin"),
    CHARCOAL_KLIN("Charcoal Klin" , "charcoalKlin"),
    FURNACE("Furnace" , "Furnace"),
    SCARE_CROW("Scare Crow" , "scarecrow"),
    DELUXE_SCARE_CROW("Deluxe Scarecrow" , "deluxeScarecrow"),
    BEE_HOUSE("Bee House" , "beeHouse"),
    CHEESE_PRESS("Cheese Press" , "cheesePress"),
    KEG("Keg" , "keg"),
    LOOM("Loom" , "loom"),
    MAYONNAISE_MACHINE("Mayonnaise Machine" , "mayonnaiseMachine"),
    OIL_MAKER("Oil Maker" , "oilMaker"),
    PRESERVES_JAR("Preserves Jar" , "preservesJar"),
    DEHYDRATOR("Dehydrator" , "dehydrator"),
    FISH_SMOKER("Fish Smoker" , "fishSmoker"),;

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

    public String getTextureName() {
        return textureName;
    }
}
