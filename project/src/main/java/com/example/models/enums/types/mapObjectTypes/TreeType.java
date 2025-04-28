package com.example.models.enums.types.mapObjectTypes;

import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.enums.worldEnums.Season;

public enum TreeType {
    TREE_BARK("Tree Bark", "alaki", -1, null, -1, -1, false, -1, (Season[]) null),
    BURNT_TREE("Burnt Tree", "alaki", -1, null, -1, -1, false, -1, (Season[]) null),
    NORMAL_TREE("Normal Tree", "alaki", -1, null, -1, -1, false, -1, (Season[]) null),
    // source
    APRICOT_TREE("Apricot Tree", "Apricot Sapling", 7, FoodTypes.APRICOT, 1, 59, true, 38, Season.SPRING),
    CHERRY_TREE("Cherry Tree", "Cherry Sapling", 7, FoodTypes.CHERRY, 1, 80, true, 38, Season.SPRING),
    BANANA_TREE("Banana Tree", "Banana Sapling", 7, FoodTypes.BANANA, 1, 150, true, 75, Season.SUMMER),
    MANGO_TREE("Mango Tree", "Mango Sapling", 7, FoodTypes.MANGO, 1, 130, true, 100, Season.SUMMER),
    ORANGE_TREE("Orange Tree", "Orange Sapling", 7, FoodTypes.ORANGE, 1, 100, true, 38, Season.SUMMER),
    PEACH_TREE("Peach Tree", "Peach Sapling", 7, FoodTypes.PEACH, 1, 140, true, 38, Season.SUMMER),
    APPLE_TREE("Apple Tree", "Apple Sapling", 7, FoodTypes.APPLE, 1, 100, true, 38, Season.FALL),
    POMEGRANATE_TREE("Pomegranate Tree", "Pomegranate Sapling", 7, FoodTypes.POMEGRANATE, 1, 100, true, 38, Season.FALL),
    OAK_TREE("Oak Tree", "Acorns", 7, FoodTypes.OAK_RESIN, 7, 150, false, 0, Season.values()),
    MAPLE_TREE("Maple Tree", "Maple Seeds", 7, FoodTypes.MAPLE_SYRUP, 9, 200, false, 0, Season.values()),
    PINE_TREE("Pine Tree", "Pine Cones", 7, FoodTypes.PINE_TAR, 5, 100, false, 0, Season.values()),
    MAHOGANY_TREE("Mahogany Tree", "Mahogany Seeds", 7, FoodTypes.SAP, 1, 2, true, -2, Season.values()),
    MUSHROOM_TREE("Mushroom Tree", "Mushroom Tree Seeds", 7, FoodTypes.COMMON_MUSHROOM, 1, 40, true, 38, Season.values()),
    MYSTIC_TREE("Mystic Tree", "Mystic Tree Seeds", 7, FoodTypes.MYSTIC_SYRUP, 7, 1000, true, 500, Season.values());

    final public String name;
    final public String source;
    final public int stageOneTime;
    final public int stageTwoTime;
    final public int stageThreeTime;
    final public int stageFourTime;
    final public FoodTypes fruitItem;
    final public int harvestCycleTime;
    final public int fruitSellPrice;
    final public boolean isFruitEdible;
    final public int fruitEnergy;
    final public Season[] seasonsOfGrowth;

    /// Generic Constructor.
    TreeType(String name, String source, int stageOneTime, int stageTwoTime, int stageThreeTime, int stageFourTime
            , FoodTypes fruitItem, int harvestCycleTime, int fruitSellPrice, boolean isFruitEdible, int fruitEnergy, Season[] seasonsOfGrowth) {
        this.name = name;
        this.source = name;
        this.stageOneTime = stageOneTime;
        this.stageTwoTime = stageTwoTime;
        this.stageThreeTime = stageThreeTime;
        this.stageFourTime = stageFourTime;
        this.fruitItem = fruitItem;
        this.harvestCycleTime = harvestCycleTime;
        this.fruitSellPrice = fruitSellPrice;
        this.isFruitEdible = isFruitEdible;
        this.fruitEnergy = fruitEnergy;
        this.seasonsOfGrowth = seasonsOfGrowth;
    }

    TreeType(String name, String source, int stagesTime, FoodTypes fruitItem, int harvestCycleTime, int fruitSellPrice
            , boolean isFruitEdible, int fruitEnergy, Season[] seasonsOfGrowth) {
        this.name = name;
        this.source = source;
        this.stageOneTime = stagesTime;
        this.stageTwoTime = stagesTime;
        this.stageThreeTime = stagesTime;
        this.stageFourTime = stagesTime;
        this.fruitItem = fruitItem;
        this.harvestCycleTime = harvestCycleTime;
        this.fruitSellPrice = fruitSellPrice;
        this.isFruitEdible = isFruitEdible;
        this.fruitEnergy = fruitEnergy;
        this.seasonsOfGrowth = seasonsOfGrowth;
    }

    public static TreeType findTreeTypeByName(String seedType){
        for (TreeType tree : TreeType.values()) {
            if(tree.source.compareToIgnoreCase(seedType) == 0){
                return tree;
            }
        }
        return null;
    }

    TreeType(String name, String source, int stagesTime, FoodTypes foodTypes, int harvestCycleTime, int fruitSellPrice, boolean b, int fruitEnergy, Season season) {
        this.name = name;
        this.source = source;
        this.stageOneTime = stagesTime;
        this.stageTwoTime = stagesTime;
        this.stageThreeTime = stagesTime;
        this.stageFourTime = stagesTime;
        this.fruitItem = foodTypes;
        this.fruitEnergy = fruitEnergy;
        this.harvestCycleTime = harvestCycleTime;
        this.fruitSellPrice = fruitSellPrice;
        this.isFruitEdible = b;
        this.seasonsOfGrowth = new Season[]{season};
    }
}
