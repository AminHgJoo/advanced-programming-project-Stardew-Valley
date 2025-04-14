package com.example.models.enums.types;

public enum MiscType {
WOOD(-1), FIBER(-1);
private final int sellPrice;

    MiscType(int sellPrice) {
        this.sellPrice = sellPrice;
    }
}
