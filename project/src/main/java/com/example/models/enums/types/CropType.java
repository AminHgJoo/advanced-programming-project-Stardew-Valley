package com.example.models.enums.types;

import com.example.models.enums.Season;

public enum CropType {
    BLUE_JAZZ("Blue Jazz", "Jazz Seeds", 1, 2, 2, 2, -1, 7, true, -1, 50, true, 45, new Season[]{Season.SPRING}, false),
    CARROT("Carrot", "Carrot Seeds", 1, 1, 1, -1, -1, 3, true, -1, 35, true, 75, new Season[]{Season.SPRING}, false),
    CAULIFLOWER("Cauliflower", "Cauliflower Seeds", 1, 2, 4, 4, 1, 12, true, -1, 175, true, 75, new Season[]{Season.SPRING}, true),
    COFFEE_BEAN("Coffee Bean", "Coffee Bean", 1, 2, 2, 3, 2, 10, false, 2, 15, false, 0, new Season[]{Season.SPRING}, new Season[]{Season.SUMMER}, false),
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
    CORN("Corn", "Corn Seeds", 2, 3, 3, 3, 3, 14, false, 4, 50, true, 25, new Season[]{Season.SUMMER, Season.AUTUMN}, false),
    HOPS("Hops", "Hops Starter", 1, 1, 2, 3, 4, 11, false, 1, 25, true, 45, new Season[]{Season.SUMMER}, false),
    HOT_PEPPER("Hot Pepper", "Pepper Seeds", 1, 1, 1, 1, 1, 5, false, 3, 40, true, 13, new Season[]{Season.SUMMER}, false),
    MELON("Melon", "Melon Seeds", 1, 2, 3, 3, 3, 12, true, -1, 250, true, 113, new Season[]{Season.SUMMER}, true),
    POPPY("Poppy", "Poppy Seeds", 1, 2, 2, 2, -1, 7, true, -1, 140, true, 45, new Season[]{Season.SUMMER}, false),
    RADISH("Radish", "Radish Seeds", 2, 1, 2, 1, 6, true, 90, true, 45, 20, new Season[]{Season.SUMMER}, false),
    RED_CABBAGE("Red Cabbage", "Red Cabbage Seeds", 2, 1, 2, 2, 2, 9, true, 260, true, 75 33, new Season[]{Season.SUMMER}, false),
    STARFRUIT("Starfruit", "Starfruit Seeds", 2, 3, 2, 3, 3, 13, true, 750, true, 125 56, new Season[]{Season.SUMMER}, false),
    SUMMER_SPANGLE("Summer Spangle", "Spangle Seeds", 1, 2, 3, 1, 8, true, 90, true, 45, 20, new Season[]{Season.SUMMER}, false),
    SUMMER_SQUASH("Summer Squash", "Squash Seeds", 1, 1, 1, 2, 1, 6, false, 3, 45, true, 63 28, new Season[]{Season.SUMMER}, false),
    SUNFLOWER("Sunflower", "Sunflower Seeds", 1, 2, 3, 2, 8, true, 80, true, 45 20, new Season[]{Season.SUMMER, Season.AUTUMN}, false),
    TOMATO("Tomato", "Tomato Seeds", 2, 2, 2, 2, 3, 11, false, 4, 60, true, 20 9, new Season[]{Season.SUMMER}, false),
    WHEAT("Wheat", "Wheat Seeds", 1, 1, 1, 1, 4, true, 25, false, new Season[]{Season.SUMMER, Season.AUTUMN}, false),
    AMARANTH("Amaranth", "Amaranth Seeds", 1, 2, 2, 2, 7, true, 150, true, 50, 22, new Season[]{Season.AUTUMN}, false),
    ARTICHOKE("Artichoke", "Artichoke Seeds", 2, 2, 1, 2, 1, 8, true, 160, true, 30, 13, new Season[]{Season.AUTUMN}, false),
    BEET("Beet", "Beet Seeds", 1, 1, 2, 2, 6, true, 100, true, 30, 13, new Season[]{Season.AUTUMN}, false),
    BOK_CHOY("Bok Choy", "Bok Choy Seeds", 1, 1, 1, 1, 4, true, 80, true, 25, 11, new Season[]{Season.AUTUMN}, false),
    BROCCOLI("Broccoli", "Broccoli Seeds", 2, 2, 2, 2, 8, false, 4 70, true, 63, 28, new Season[]{Season.AUTUMN}, false),
    CRANBERRIES("Cranberries", "Cranberry Seeds", 1, 2, 1, 1, 2, 7, false, 5 75, true, 38, 17, new Season[]{Season.AUTUMN}, false),
    EGGPLANT("Eggplant", "Eggplant Seeds", 1, 1, 1, 1, 5, false, 5 60, true, 20, 9, new Season[]{Season.AUTUMN}, false),
    FAIRY_ROSE("Fairy Rose", "Fairy Seeds", 1, 4, 4, 3, 12, true, 290, true, 45, 20, new Season[]{Season.AUTUMN}, false),
    GRAPE("Grape", "Grape Starter", 1, 1, 2, 3, 3, 10, false, 3 80, true, 38, 17, new Season[]{Season.AUTUMN}, false),
    PUMPKIN("Pumpkin", "Pumpkin Seeds", 1, 2, 3, 4, 3, 13, true, 320, false, new Season[]{Season.AUTUMN}, true),
    YAM("Yam", "Yam Seeds", 1, 3, 3, 3, 10, true, 160, true, 45, 20, new Season[]{Season.AUTUMN}, false),
    SWEET_GEM_BERRY("Sweet Gem Berry", "Rare Seed", 2, 4, 6, 6, 6, 24, true, 3000, false, new Season[]{Season.AUTUMN}, false),
    POWDER_MELON("Powder Melon", "Powdermelon Seeds", 1, 2, 1, 2, 1, 7, true, 60, true, 63, 28, Winter, true),
    ANCIENT_FRUIT("Ancient Fruit", "Ancient Seeds", 2, 7, 7, 7, 5, 28, false, 7, 550, false, new Season[]{Season.AUTUMN, Season.SUMMER, Season.SPRING}, false),
    ;
    public final String name;
    public final String source;
    public final int StageZeroDaysToNextStage;
    public final int StageOneDaysToNextStage;
    public final int StageTwoDaysToNextStage;
    public final int StageThreeDaysToNextStage;
    public final int StageFourDaysToNextStage;
    public final int totalHarvestTime;
    public final boolean oneTime;
    public final int regrowthTime;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final double energy;
    public final Season[] season;
    public final boolean canBeGiant;


    CropType(String name, String source, int stageZeroDaysToNextStage, int stageOneDaysToNextStage, int stageTwoDaysToNextStage, int stageThreeDaysToNextStage, int stageFourDaysToNextStage, int totalHarvestTime, boolean oneTime, int regrowthTime, int baseSellPrice, boolean isEdible, double energy, Season[] season, boolean canBeGiant) {
        this.name = name;
        this.source = source;
        StageZeroDaysToNextStage = stageZeroDaysToNextStage;
        StageOneDaysToNextStage = stageOneDaysToNextStage;
        StageTwoDaysToNextStage = stageTwoDaysToNextStage;
        StageThreeDaysToNextStage = stageThreeDaysToNextStage;
        StageFourDaysToNextStage = stageFourDaysToNextStage;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.season = season;
        this.canBeGiant = canBeGiant;
    }


}
