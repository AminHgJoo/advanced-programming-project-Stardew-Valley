package com.common.models.enums.types.inventoryEnums;

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

    public String getTextureName() {
        if (this == TrashcanType.IRIDIUM) {
            return "trashcaniridium";
        }
        if (this == TrashcanType.GOLD) {
            return "trashcangold";
        }
        if (this == TrashcanType.IRON) {
            return "trashcansteel";
        }
        return "trashcancopper";
    }
}
