package com.example.models.enums.types;

import com.example.models.enums.Season;

public enum ForagingSeedsType {
    JAZZ_SEEDS(Season.SPRING),
    CARROT_SEEDS(Season.SPRING),
    CAULIFLOWER_SEEDS(Season.SPRING),
    COFFEE_BEAN(Season.SPRING),
    GARLIC_SEEDS(Season.SPRING),
    BEAN_STARTER(Season.SPRING),
    KALE_SEEDS(Season.SPRING),
    PARSNIP_SEEDS(Season.SPRING),
    POTATO_SEEDS(Season.SPRING),
    RHUBARB_SEEDS(Season.SPRING),
    STRAWBERRY_SEEDS(Season.SPRING),
    TULIP_BULB(Season.SPRING),
    RICE_SHOOT(Season.SPRING),
    BLUEBERRY_SEEDS(Season.SUMMER),
    CORN_SEEDS(Season.SUMMER),
    HOPS_SEEDS(Season.SUMMER),
    PEPPER_SEEDS(Season.SUMMER),
    MELON_SEEDS(Season.SUMMER),
    POPPY_SEEDS(Season.SUMMER),
    RADISH_SEEDS(Season.SUMMER),
    RED_CABBAGE_SEEDS(Season.SUMMER),
    STARFRUIT_SEEDS(Season.SUMMER),
    SPANGLE_SEEDS(Season.SUMMER),
    SUMMER_SQUASH_SEEDS(Season.SUMMER),
    SUNFLOWER_SEEDS(Season.SUMMER),
    TOMATO_SEEDS(Season.SUMMER),
    WHEAT_SEEDS(Season.SUMMER),
    AMARANTH_SEEDS(Season.FALL),
    ARTICHOKE_SEEDS(Season.FALL),
    BEET_SEEDS(Season.FALL),
    BOK_CHOY_SEEDS(Season.FALL),
    BROCCOLI_SEEDS(Season.FALL),
    CRANBERRY_SEEDS(Season.FALL),
    EGGPLANT_SEEDS(Season.FALL),
    FAIRY_SEEDS(Season.FALL),
    GRAPE_STARTER(Season.FALL),
    PUMPKIN_SEEDS(Season.FALL),
    YAM_SEEDS(Season.FALL),
    RARE_SEEDS(Season.FALL),
    POWDERMELON_SEEDS(Season.WINTER),
    ANCIENT_SEEDS(Season.values()),
    MIXED_SEEDS(Season.values()),;

    private final Season[] seasons;

    ForagingSeedsType(Season[] season) {
        this.seasons = season;
    }

    ForagingSeedsType(Season season) {
        this.seasons = new Season[]{season};
    }

    public Season[] getSeasons() {
        return seasons;
    }
}
