// TODO null means all seasons

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
    AMARANTH_SEEDS(Season.AUTUMN),
    ARTICHOKE_SEEDS(Season.AUTUMN),
    BEET_SEEDS(Season.AUTUMN),
    BOK_CHOY_SEEDS(Season.AUTUMN),
    BROCCOLI_SEEDS(Season.AUTUMN),
    CRANBERRY_SEEDS(Season.AUTUMN),
    EGGPLANT_SEEDS(Season.AUTUMN),
    FAIRY_SEEDS(Season.AUTUMN),
    GRAPE_STARTER(Season.AUTUMN),
    PUMPKIN_SEEDS(Season.AUTUMN),
    YAM_SEEDS(Season.AUTUMN),
    RARE_SEEDS(Season.AUTUMN),
    POWDERMELON_SEEDS(Season.WINTER),
    ANCIENT_SEEDS(null),
    MIXED_SEEDS(null)
    ;


    private final Season season;
    ForagingSeedsType(Season season) {
        this.season = season;
    }
    public Season getSeason() {
        return season;
    }
}
