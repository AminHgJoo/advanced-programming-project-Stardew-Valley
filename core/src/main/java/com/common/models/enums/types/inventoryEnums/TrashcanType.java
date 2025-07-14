package com.common.models.enums.types.inventoryEnums;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;

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

    public Texture getTexture() {
        if (this == TrashcanType.IRIDIUM) {
            return AssetManager.getImage("trashcaniridium");
        }
        if (this == TrashcanType.GOLD) {
            return AssetManager.getImage("trashcangold");
        }
        if (this == TrashcanType.IRON) {
            return AssetManager.getImage("trashcansteel");
        }
        return AssetManager.getImage("trashcancopper");
    }
}
