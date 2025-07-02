package com.common.models.enums.types.mapObjectTypes;

import com.common.models.enums.worldEnums.Season;

public enum ForagingSeedsType {

    JAZZ_SEEDS(Season.SPRING, "Jazz Seeds"),
    CARROT_SEEDS(Season.SPRING, "Carrot Seeds"),
    CAULIFLOWER_SEEDS(Season.SPRING, "Cauliflower Seeds"),
    COFFEE_BEAN(Season.SPRING, "Coffee Bean"),
    GARLIC_SEEDS(Season.SPRING, "Garlic Seeds"),
    BEAN_STARTER(Season.SPRING, "Bean Starter"),
    KALE_SEEDS(Season.SPRING, "Kale Seeds"),
    PARSNIP_SEEDS(Season.SPRING, "Parsnip Seeds"),
    POTATO_SEEDS(Season.SPRING, "Potato Seeds"),
    RHUBARB_SEEDS(Season.SPRING, "Rhubarb Seeds"),
    STRAWBERRY_SEEDS(Season.SPRING, "Strawberry Seeds"),
    TULIP_BULB(Season.SPRING, "Tulip Bulb"),
    RICE_SHOOT(Season.SPRING, "Rice Shoot"),
    BLUEBERRY_SEEDS(Season.SUMMER, "Blueberry Seeds"),
    CORN_SEEDS(Season.SUMMER, "Corn Seeds"),
    HOPS_SEEDS(Season.SUMMER, "Hops Seeds"),
    PEPPER_SEEDS(Season.SUMMER, "Pepper Seeds"),
    MELON_SEEDS(Season.SUMMER, "Melon Seeds"),
    POPPY_SEEDS(Season.SUMMER, "Poppy Seeds"),
    RADISH_SEEDS(Season.SUMMER, "Radish Seeds"),
    RED_CABBAGE_SEEDS(Season.SUMMER, "Red Cabbage Seeds"),
    STARFRUIT_SEEDS(Season.SUMMER, "Starfruit Seeds"),
    SPANGLE_SEEDS(Season.SUMMER, "Spangle Seeds"),
    SUMMER_SQUASH_SEEDS(Season.SUMMER, "Summer Squash Seeds"),
    SUNFLOWER_SEEDS(Season.SUMMER, "Sunflower Seeds"),
    TOMATO_SEEDS(Season.SUMMER, "Tomato Seeds"),
    WHEAT_SEEDS(Season.SUMMER, "Wheat Seeds"),
    AMARANTH_SEEDS(Season.FALL, "Amaranth Seeds"),
    ARTICHOKE_SEEDS(Season.FALL, "Artichoke Seeds"),
    BEET_SEEDS(Season.FALL, "Beet Seeds"),
    BOK_CHOY_SEEDS(Season.FALL, "Bok Choy Seeds"),
    BROCCOLI_SEEDS(Season.FALL, "Broccoli Seeds"),
    CRANBERRY_SEEDS(Season.FALL, "Cranberry Seeds"),
    EGGPLANT_SEEDS(Season.FALL, "Eggplant Seeds"),
    FAIRY_SEEDS(Season.FALL, "Fairy Seeds"),
    GRAPE_STARTER(Season.FALL, "Grape Starter"),
    PUMPKIN_SEEDS(Season.FALL, "Pumpkin Seeds"),
    YAM_SEEDS(Season.FALL, "Yam Seeds"),
    RARE_SEEDS(Season.FALL, "Rare Seeds"),
    POWDERMELON_SEEDS(Season.WINTER, "Powdermelon Seeds"),
    ANCIENT_SEEDS(Season.values(), "Ancient Seeds"),
    MIXED_SEEDS(Season.values(), "Mixed Seeds"),
    ;

    public final Season[] seasons;
    public final String name;

    ForagingSeedsType(Season[] season, String name) {
        this.seasons = season;
        this.name = name;
    }

    ForagingSeedsType(Season season, String name) {
        this.seasons = new Season[]{season};
        this.name = name;
    }

    public Season[] getSeasons() {
        return seasons;
    }

}
