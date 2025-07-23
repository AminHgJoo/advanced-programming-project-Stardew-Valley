package com.common.models.enums.types.mapObjectTypes;

import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.enums.worldEnums.Season;

import java.util.Arrays;

public enum ForagingCropsType {
    GRASS(Season.values(), 0, 0, MiscType.FIBER, "Grass", "grass"),
    COMMON_MUSHROOM(Season.values(), 40, 38, FoodTypes.COMMON_MUSHROOM, "Common Mushroom", "commonMushroom"),
    DAFFODIL(Season.SPRING, 30, 0, FoodTypes.DAFFODIL, "Daffodil", "daffodil"),
    DANDELION(Season.SPRING, 40, 25, FoodTypes.DANDELION, "Dandelion", "dandelion"),
    LEEK(Season.SPRING, 60, 40, FoodTypes.LEEK, "Leek", "leek"),
    MOREL(Season.SPRING, 150, 20, FoodTypes.MOREL, "Morel", "morel"),
    SALMON_BERRY(Season.SPRING, 8, 13, FoodTypes.SALMON_BERRY, "Salmon Berry", "salmonberry"),
    SPRING_ONION(Season.SPRING, 8, 13, FoodTypes.SPRING_ONION, "Spring Onion", "springOnion"),
    WILD_HORSERADISH(Season.SPRING, 50, 13, FoodTypes.WILD_HORSERADISH, "Wild Horseradish", "wildHorseradish"),
    FIDDLE_HEAD_FERN(Season.SUMMER, 90, 25, FoodTypes.FIDDLE_HEAD_FERN, "Fiddle Head Fern", "fiddleheadFern"),
    GRAPE(Season.SUMMER, 80, 38, FoodTypes.GRAPE, "Grape", "grape"),
    RED_MUSHROOM(Season.SUMMER, 75, -50, FoodTypes.RED_MUSHROOM, "Red Mushroom", "redMushroom"),
    SPICE_BERRY(Season.SUMMER, 80, 25, FoodTypes.SPICE_BERRY, "Spice Berry", "spiceBerry"),
    SWEET_PEA(Season.SUMMER, 50, 0, FoodTypes.SWEET_PEA, "Sweet Pea", "sweetPea"),
    BLACKBERRY(Season.FALL, 25, 25, FoodTypes.BLACKBERRY, "Black Berry", "blackBerry"),
    CHANTERELLE(Season.FALL, 160, 75, FoodTypes.CHANTERELLE, "Chanterelle", "chanterelle"),
    HAZELNUT(Season.FALL, 40, 38, FoodTypes.HAZELNUT, "Hazelnut", "hazelnut"),
    PURPLE_MUSHROOM(Season.FALL, 90, 30, FoodTypes.PURPLE_MUSHROOM, "Purple Mushroom", "purpleMushroom"),
    WILD_PLUM(Season.FALL, 80, 25, FoodTypes.WILD_PLUM, "Wild Plum", "wildPlum"),
    CROCUS(Season.WINTER, 60, 0, FoodTypes.CROCUS, "Crocus", "crocus"),
    CRYSTAL_FRUIT(Season.WINTER, 150, 63, FoodTypes.CRYSTAL_FRUIT, "Crystal Fruit", "crystalFruit"),
    HOLLY(Season.WINTER, 80, -37, FoodTypes.HOLLY, "Holly", "holly"),
    SNOW_YAM(Season.WINTER, 100, 30, FoodTypes.SNOW_YAM, "Snow Yam", "snowYam"),
    WINTER_ROOT(Season.WINTER, 70, 25, FoodTypes.WINTER_ROOT, "Winter Root", "winterRoot"),
    ;

    public final String name;
    private final Season[] seasons;
    private final int cost;
    private final int energy;
    private final ItemType harvestedItemType;
    private final String textureName;

    ForagingCropsType(Season[] seasons, int cost, int energy, ItemType harvestedItemType, String name, String textureName) {
        this.seasons = seasons;
        this.cost = cost;
        this.energy = energy;
        this.harvestedItemType = harvestedItemType;
        this.name = name;
        this.textureName = textureName;
    }

    ForagingCropsType(Season season, int cost, int energy, ItemType harvestedItemType, String name, String textureName) {
        this.seasons = new Season[]{season};
        this.cost = cost;
        this.energy = energy;
        this.harvestedItemType = harvestedItemType;
        this.name = name;
        this.textureName = textureName;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Name: ").append(name).append("\n");
        str.append("Value: ").append(cost).append("\n");
        str.append("Energy: ").append(energy).append("\n");
        str.append("Seasons: ").append(Arrays.toString(seasons)).append("\n");
        return str.toString();
    }

    public Season[] getSeasons() {
        return seasons;
    }

    public int getCost() {
        return cost;
    }

    public int getEnergy() {
        return energy;
    }

    public ItemType getHarvestedItemType() {
        return harvestedItemType;
    }

    public String getTextureName() {
        return textureName;
    }
}
