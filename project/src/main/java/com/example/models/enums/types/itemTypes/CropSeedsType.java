package com.example.models.enums.types.itemTypes;

import com.example.models.Slot;
import com.example.models.enums.worldEnums.Season;
import com.example.models.items.Seed;

public enum CropSeedsType implements ItemType {
    BLUE_JAZZ("Blue Jazz", "Jazz Seeds", 1, 2, 2, 2, -1, 7, true, -1, 50, true, 45, new Season[]{Season.SPRING}, false),
    CARROT("Carrot", "Carrot Seeds", 1, 1, 1, -1, -1, 3, true, -1, 35, true, 75, new Season[]{Season.SPRING}, false),
    CAULIFLOWER("Cauliflower", "Cauliflower Seeds", 1, 2, 4, 4, 1, 12, true, -1, 175, true, 75, new Season[]{Season.SPRING}, true),
    COFFEE_BEAN("Coffee Bean", "Coffee Bean", 1, 2, 2, 3, 2, 10, false, 2, 15, false, 0, new Season[]{Season.SPRING, Season.SUMMER}, false),
    GARLIC("Garlic", "Garlic Seeds", 1, 1, 1, 1, -1, 4, true, -1, 60, true, 20, new Season[]{Season.SPRING}, false),
    GREEN_BEAN("Green Bean", "Bean Starter", 1, 1, 1, 3, 4, 10, false, 3, 40, true, 25, new Season[]{Season.SPRING}, false),
    KALE("Kale", "Kale Seeds", 1, 2, 2, 1, -1, 6, true, -1, 110, true, 50, new Season[]{Season.SPRING}, false),
    PARSNIP("Parsnip", "Parsnip Seeds", 1, 1, 1, 1, -1, 4, true, -1, 35, true, 25, new Season[]{Season.SPRING}, false),
    POTATO("Potato", "Potato Seeds", 1, 1, 1, 2, 1, 6, true, -1, 80, true, 25, new Season[]{Season.SPRING}, false),
    RHUBARB("Rhubarb", "Rhubarb Seeds", 2, 2, 2, 3, 4, 13, true, -1, 220, false, 0, new Season[]{Season.SPRING}, false),
    STRAWBERRY("Strawberry", "Strawberry Seeds", 1, 1, 2, 2, 2, 8, false, 4, 120, true, 50, new Season[]{Season.SPRING}, false),
    TULIP("Tulip", "Tulip Bulb", 1, 1, 2, 2, -1, 6, true, -1, 30, true, 45, new Season[]{Season.SPRING}, false),
    UNMILLED_RICE("Unmilled Rice", "Rice Shoot", 1, 2, 2, 3, -1, 8, true, -1, 30, true, 3, new Season[]{Season.SPRING}, false),
    BLUEBERRY("Blueberry", "Blueberry Seeds", 1, 3, 3, 4, 2, 13, false, 4, 50, true, 25, new Season[]{Season.SUMMER}, false),
    CORN("Corn", "Corn Seeds", 2, 3, 3, 3, 3, 14, false, 4, 50, true, 25, new Season[]{Season.SUMMER, Season.FALL}, false),
    HOPS("Hops", "Hops Starter", 1, 1, 2, 3, 4, 11, false, 1, 25, true, 45, new Season[]{Season.SUMMER}, false),
    HOT_PEPPER("Hot Pepper", "Pepper Seeds", 1, 1, 1, 1, 1, 5, false, 3, 40, true, 13, new Season[]{Season.SUMMER}, false),
    MELON("Melon", "Melon Seeds", 1, 2, 3, 3, 3, 12, true, -1, 250, true, 113, new Season[]{Season.SUMMER}, true),
    POPPY("Poppy", "Poppy Seeds", 1, 2, 2, 2, -1, 7, true, -1, 140, true, 45, new Season[]{Season.SUMMER}, false),
    RADISH("Radish", "Radish Seeds", 2, 1, 2, 1, -1, 6, true, -1, 90, true, 45, new Season[]{Season.SUMMER}, false),
    RED_CABBAGE("Red Cabbage", "Red Cabbage Seeds", 2, 1, 2, 2, 2, 9, true, -1, 260, true, 75, new Season[]{Season.SUMMER}, false),
    STARFRUIT("Starfruit", "Starfruit Seeds", 2, 3, 2, 3, 3, 13, true, -1, 750, true, 125, new Season[]{Season.SUMMER}, false),
    SUMMER_SPANGLE("Summer Spangle", "Spangle Seeds", 1, 2, 3, 1, -1, 8, true, -1, 90, true, 45, new Season[]{Season.SUMMER}, false),
    SUMMER_SQUASH("Summer Squash", "Squash Seeds", 1, 1, 1, 2, 1, 6, false, 3, 45, true, 63, new Season[]{Season.SUMMER}, false),
    SUNFLOWER("Sunflower", "Sunflower Seeds", 1, 2, 3, 2, -1, 8, true, -1, 80, true, 45, new Season[]{Season.SUMMER, Season.FALL}, false),
    TOMATO("Tomato", "Tomato Seeds", 2, 2, 2, 2, 3, 11, false, 4, 60, true, 20, new Season[]{Season.SUMMER}, false),
    WHEAT("Wheat", "Wheat Seeds", 1, 1, 1, 1, -1, 4, true, -1, 25, false, 0, new Season[]{Season.SUMMER, Season.FALL}, false),
    AMARANTH("Amaranth", "Amaranth Seeds", 1, 2, 2, 2, -1, 7, true, -1, 150, true, 50, new Season[]{Season.FALL}, false),
    ARTICHOKE("Artichoke", "Artichoke Seeds", 2, 2, 1, 2, 1, 8, true, -1, 160, true, 30, new Season[]{Season.FALL}, false),
    BEET("Beet", "Beet Seeds", 1, 1, 2, 2, -1, 6, true, -1, 100, true, 30, new Season[]{Season.FALL}, false),
    BOK_CHOY("Bok Choy", "Bok Choy Seeds", 1, 1, 1, 1, -1, 4, true, -1, 80, true, 25, new Season[]{Season.FALL}, false),
    BROCCOLI("Broccoli", "Broccoli Seeds", 2, 2, 2, 2, -1, 8, false, 4, 70, true, 63, new Season[]{Season.FALL}, false),
    CRANBERRIES("Cranberries", "Cranberry Seeds", 1, 2, 1, 1, 2, 7, false, 5, 75, true, 38, new Season[]{Season.FALL}, false),
    EGGPLANT("Eggplant", "Eggplant Seeds", 1, 1, 1, 1, -1, 5, false, 5, 60, true, 20, new Season[]{Season.FALL}, false),
    FAIRY_ROSE("Fairy Rose", "Fairy Seeds", 1, 4, 4, 3, -1, 12, true, -1, 290, true, 45, new Season[]{Season.FALL}, false),
    GRAPE("Grape", "Grape Starter", 1, 1, 2, 3, 3, 10, false, 3, 80, true, 38, new Season[]{Season.FALL}, false),
    PUMPKIN("Pumpkin", "Pumpkin Seeds", 1, 2, 3, 4, 3, 13, true, -1, 320, false, 0, new Season[]{Season.FALL}, true),
    YAM("Yam", "Yam Seeds", 1, 3, 3, 3, -1, 10, true, -1, 160, true, 45, new Season[]{Season.FALL}, false),
    SWEET_GEM_BERRY("Sweet Gem Berry", "Rare Seed", 2, 4, 6, 6, 6, 24, true, -1, 3000, false, 0, new Season[]{Season.FALL}, false),
    POWDER_MELON("Powder Melon", "Powdermelon Seeds", 1, 2, 1, 2, 1, 7, true, -1, 60, true, 63, new Season[]{Season.WINTER}, true),
    ANCIENT_FRUIT("Ancient Fruit", "Ancient Seeds", 2, 7, 7, 7, 5, 28, false, 7, 550, false, 0, new Season[]{Season.FALL, Season.SUMMER, Season.SPRING}, false),
    ;

    public final String name;
    public final String source;
    public final int stageZeroDaysToNextStage;
    public final int stageOneDaysToNextStage;
    public final int stageTwoDaysToNextStage;
    public final int stageThreeDaysToNextStage;
    public final int stageFourDaysToNextStage;
    public final int totalHarvestTime;
    public final boolean oneTime;
    public final int regrowthTime;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final double energy;
    public final Season[] season;
    public final boolean canBeGiant;


    CropSeedsType(String name, String source, int stageZeroDaysToNextStage, int stageOneDaysToNextStage, int stageTwoDaysToNextStage, int stageThreeDaysToNextStage, int stageFourDaysToNextStage, int totalHarvestTime, boolean oneTime, int regrowthTime, int baseSellPrice, boolean isEdible, double energy, Season[] season, boolean canBeGiant) {
        this.name = name;
        this.source = source;
        this.stageZeroDaysToNextStage = stageZeroDaysToNextStage;
        this.stageOneDaysToNextStage = stageOneDaysToNextStage;
        this.stageTwoDaysToNextStage = stageTwoDaysToNextStage;
        this.stageThreeDaysToNextStage = stageThreeDaysToNextStage;
        this.stageFourDaysToNextStage = stageFourDaysToNextStage;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.season = season;
        this.canBeGiant = canBeGiant;
    }

    public static CropSeedsType findCropBySeed(String seed) {
        for (CropSeedsType crop : CropSeedsType.values()) {
            if (crop.source.equals(seed)) {
                return crop;
            }
        }
        return null;
    }

    @Override
    public Slot createAmountOfItem(int amount) {
        return new Slot(new Seed(this), amount);
    }
}
