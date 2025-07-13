package com.client.utils;

import com.badlogic.gdx.graphics.Texture;
import com.common.models.enums.worldEnums.Season;

public enum SeasonTextures {
    SPRING(AssetManager.getImage("grass")),
    SUMMER(AssetManager.getImage("grasssummer")),
    FALL(AssetManager.getImage("grassfall")),
    WINTER(AssetManager.getImage("grasswinter")),
    ;

    public final Texture texture;

    SeasonTextures(Texture texture) {
        this.texture = texture;
    }

    public static Texture giveSeasonTexture(Season season) {
        if (season == Season.SPRING) {
            return SPRING.texture;
        } else if (season == Season.SUMMER) {
            return SUMMER.texture;
        } else if (season == Season.FALL) {
            return FALL.texture;
        } else {
            return WINTER.texture;
        }
    }
}
