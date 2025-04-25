package com.example.models.enums.types;

public enum ArtisanBlockType implements ItemType {
    SHIPPING_BIN("Shipping Bin"),
    ;

    public final String name;

    ArtisanBlockType(String name) {
        this.name = name;
    }
}
