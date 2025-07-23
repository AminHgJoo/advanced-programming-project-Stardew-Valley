package com.common.models.enums.types.mapObjectTypes;

import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.enums.worldEnums.Season;

import java.util.Arrays;

public enum TreeType {
    //TODO textures not found
    TREE_BARK("Tree Bark", "alaki", -1, MiscType.WOOD, -1, -1, false, -1, (Season[]) null, new String[]{"barkTreeAlaki"}),
    BURNT_TREE("Burnt Tree", "alaki", -1, ForagingMineralsType.COAL, -1, -1, false, -1, (Season[]) null, new String[]{"barkTreeAlaki"}),
    NORMAL_TREE("Normal Tree", "alaki", -1, MiscType.WOOD, -1, -1, false, -1, (Season[]) null, new String[]{"barkTreeAlaki"}),
    // source
    APRICOT_TREE("Apricot Tree", "Apricot Sapling", 7, FoodTypes.APRICOT, 1, 59, true, 38, Season.SPRING, new String[]{"apricotStage1", "apricotStage2", "apricotStage3", "apricotStage4", "apricotStage5"}),
    CHERRY_TREE("Cherry Tree", "Cherry Sapling", 7, FoodTypes.CHERRY, 1, 80, true, 38, Season.SPRING, new String[]{"cherryStage1", "cherryStage2", "cherryStage3", "cherryStage4", "cherryStage5"}),
    BANANA_TREE("Banana Tree", "Banana Sapling", 7, FoodTypes.BANANA, 1, 150, true, 75, Season.SUMMER, new String[]{"bananaStage1", "bananaStage2", "bananaStage3", "bananaStage4", "bananaStage5"}),
    MANGO_TREE("Mango Tree", "Mango Sapling", 7, FoodTypes.MANGO, 1, 130, true, 100, Season.SUMMER, new String[]{"mangoStage1", "mangoStage2", "mangoStage3", "mangoStage4", "mangoStage5"}),
    ORANGE_TREE("Orange Tree", "Orange Sapling", 7, FoodTypes.ORANGE, 1, 100, true, 38, Season.SUMMER, new String[]{"orangeStage1", "orangeStage2", "orangeStage3", "orangeStage4", "orangeStage5"}),
    PEACH_TREE("Peach Tree", "Peach Sapling", 7, FoodTypes.PEACH, 1, 140, true, 38, Season.SUMMER, new String[]{"peachStage1", "peachStage2", "peachStage3", "peachStage4", "peachStage5"}),
    APPLE_TREE("Apple Tree", "Apple Sapling", 7, FoodTypes.APPLE, 1, 100, true, 38, Season.FALL, new String[]{"appleStage1", "appleStage2", "appleStage3", "appleStage4", "appleStage5"}),
    POMEGRANATE_TREE("Pomegranate Tree", "Pomegranate Sapling", 7, FoodTypes.POMEGRANATE, 1, 100, true, 38, Season.FALL, new String[]{"pomegrenateStage1", "pomegrenateStage2", "pomegrenateStage3", "pomegrenateStage4", "pomegrenateStage5"}),
    OAK_TREE("Oak Tree", "Acorns", 7, FoodTypes.OAK_RESIN, 7, 150, false, 0, Season.values(), new String[]{"oakStage1", "oakStage2", "oakStage3", "oakStage4", "oakStage5"}),
    MAPLE_TREE("Maple Tree", "Maple Seeds", 7, FoodTypes.MAPLE_SYRUP, 9, 200, false, 0, Season.values(), new String[]{"mapleStage1", "mapleStage2", "mapleStage3", "mapleStage4", "mapleStage5"}),
    PINE_TREE("Pine Tree", "Pine Cones", 7, FoodTypes.PINE_TAR, 5, 100, false, 0, Season.values(), new String[]{"pineStage1", "pineStage2", "pineStage3", "pineStage4", "pineStage5"}),
    MAHOGANY_TREE("Mahogany Tree", "Mahogany Seeds", 7, FoodTypes.SAP, 1, 2, true, -2, Season.values(), new String[]{"mahoganyStage1", "mahoganyStage2", "mahoganyStage3", "mahoganyStage4", "mahoganyStage5"}),
    MUSHROOM_TREE("Mushroom Tree", "Mushroom Tree Seeds", 7, FoodTypes.COMMON_MUSHROOM, 1, 40, true, 38, Season.values(), new String[]{"mushroomStage1", "mushroomStage2", "mushroomStage3", "mushroomStage4", "mushroomStage5"}),
    MYSTIC_TREE("Mystic Tree", "Mystic Tree Seeds", 7, FoodTypes.MYSTIC_SYRUP, 7, 1000, true, 500, Season.values(), new String[]{"mysticStage1", "mysticStage2", "mysticStage3", "mysticStage4", "mysticStage5"});

    final public String name;
    final public String source;
    final public int stageOneTime;
    final public int stageTwoTime;
    final public int stageThreeTime;
    final public int stageFourTime;
    final public ItemType fruitItem;
    final public int harvestCycleTime;
    final public int fruitSellPrice;
    final public boolean isFruitEdible;
    final public int fruitEnergy;
    final public Season[] seasonsOfGrowth;
    final public String[] textureNames;

    /// Generic Constructor.
    TreeType(String name, String source, int stageOneTime, int stageTwoTime, int stageThreeTime, int stageFourTime
        , FoodTypes fruitItem, int harvestCycleTime, int fruitSellPrice, boolean isFruitEdible, int fruitEnergy, Season[] seasonsOfGrowth, String[] textureNames) {
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
        this.textureNames = textureNames;
    }

    TreeType(String name, String source, int stagesTime, ItemType fruitItem, int harvestCycleTime, int fruitSellPrice
        , boolean isFruitEdible, int fruitEnergy, Season[] seasonsOfGrowth, String[] textureNames) {
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
        this.textureNames = textureNames;
    }

    TreeType(String name, String source, int stagesTime, FoodTypes foodTypes, int harvestCycleTime, int fruitSellPrice, boolean b, int fruitEnergy, Season season, String[] textureNames) {
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
        this.textureNames = textureNames;
    }

    public static TreeType findTreeTypeByName(String seedType) {
        for (TreeType tree : TreeType.values()) {
            if (tree.source.compareToIgnoreCase(seedType) == 0) {
                return tree;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Name: ").append(name).append("\n");
        str.append("Source: ").append(source).append("\n");
        str.append("Stage One Time: ").append(stageOneTime).append("\n");
        str.append("Stage Two Time: ").append(stageTwoTime).append("\n");
        str.append("Stage Three Time: ").append(stageThreeTime).append("\n");
        str.append("Stage Four Time: ").append(stageFourTime).append("\n");
        str.append("Fruit Item: ").append(fruitItem.getName()).append("\n");
        str.append("Harvest Cycle Time: ").append(harvestCycleTime).append("\n");
        str.append("Fruit Sell Price: ").append(fruitSellPrice).append("\n");
        str.append("Is Fruit Edible: ").append(isFruitEdible).append("\n");
        str.append("Fruit Energy: ").append(fruitEnergy).append("\n");
        str.append("Seasons Of Growth: ").append(Arrays.toString(seasonsOfGrowth)).append("\n");
        return str.toString();
    }
}
