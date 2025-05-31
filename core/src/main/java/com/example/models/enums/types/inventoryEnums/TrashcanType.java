package com.example.models.enums.types.inventoryEnums;

public enum TrashcanType {
    DEFAULT(0),
    COPPER(15),
    IRON(30),
    GOLD(45),
    IRIDIUM(60),
    ;
    final public int refundPercentage;

    TrashcanType(int refundPercentage) {
        this.refundPercentage = refundPercentage;
    }
}
