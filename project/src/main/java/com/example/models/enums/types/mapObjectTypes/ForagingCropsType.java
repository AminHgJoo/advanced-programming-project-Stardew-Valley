package com.example.models.enums.types.mapObjectTypes;

import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.worldEnums.Season;

import java.util.Arrays;

public enum ForagingCropsType {
    GRASS(Season.values(), 0, 0, MiscType.FIBER, "Grass"),
    COMMON_MUSHROOM(Season.values(), 40, 38, FoodTypes.COMMON_MUSHROOM, "Common Mushroom"),
    DAFFODIL(Season.SPRING, 30, 0, FoodTypes.DAFFODIL, "Daffodil"),
    DANDELION(Season.SPRING, 40, 25, FoodTypes.DANDELION, "Dandelion"),
    LEEK(Season.SPRING, 60, 40, FoodTypes.LEEK, "Leek"),
    MOREL(Season.SPRING, 150, 20, FoodTypes.MOREL, "Morel"),
    SALMON_BERRY(Season.SPRING, 8, 13, FoodTypes.SALMON_BERRY, "Salmon Berry"),
    SPRING_ONION(Season.SPRING, 8, 13, FoodTypes.SPRING_ONION, "Spring Onion"),
    WILD_HORSERADISH(Season.SPRING, 50, 13, FoodTypes.WILD_HORSERADISH, "Wild Horseradish"),
    FIDDLE_HEAD_FERN(Season.SUMMER, 90, 25, FoodTypes.FIDDLE_HEAD_FERN, "Fiddle Head Fern"),
    GRAPE(Season.SUMMER, 80, 38, FoodTypes.GRAPE, "Grape"),
    RED_MUSHROOM(Season.SUMMER, 75, -50, FoodTypes.RED_MUSHROOM, "Red Mushroom"),
    SPICE_BERRY(Season.SUMMER, 80, 25, FoodTypes.SPICE_BERRY, "Spice Berry"),
    SWEET_PEA(Season.SUMMER, 50, 0, FoodTypes.SWEET_PEA, "Sweet Pea"),
    BLACKBERRY(Season.FALL, 25, 25, FoodTypes.BLACKBERRY, "Black Berry"),
    CHANTERELLE(Season.FALL, 160, 75, FoodTypes.CHANTERELLE, "Chernelle"),
    HAZELNUT(Season.FALL, 40, 38, FoodTypes.HAZELNUT, "Hazelnut"),
    PURPLE_MUSHROOM(Season.FALL, 90, 30, FoodTypes.PURPLE_MUSHROOM, "Purple Mushroom"),
    WILD_PLUM(Season.FALL, 80, 25, FoodTypes.WILD_PLUM, "Wild Plum"),
    CROCUS(Season.WINTER, 60, 0, FoodTypes.CROCUS, "Crocus"),
    CRYSTAL_FRUIT(Season.WINTER, 150, 63, FoodTypes.CRYSTAL_FRUIT, "Crystal Fruit"),
    HOLLY(Season.WINTER, 80, -37, FoodTypes.HOLLY, "Holly"),
    SNOW_YAM(Season.WINTER, 100, 30, FoodTypes.SNOW_YAM, "Snow Yam"),
    WINTER_ROOT(Season.WINTER, 70, 25, FoodTypes.WINTER_ROOT, "Winter Root"),
    ;

    private final Season[] seasons;
    private final int cost;
    private final int energy;
    private final ItemType harvestedItemType;
    public final String name;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Name: ").append(name).append("\n");
        str.append("Value: ").append(cost).append("\n");
        str.append("Energy: ").append(energy).append("\n");
        str.append("Seasons: ").append(Arrays.toString(seasons)).append("\n");
        return str.toString();
    }

    ForagingCropsType(Season[] seasons, int cost, int energy, ItemType harvestedItemType, String name) {
        this.seasons = seasons;
        this.cost = cost;
        this.energy = energy;
        this.harvestedItemType = harvestedItemType;
        this.name = name;
    }

    ForagingCropsType(Season season, int cost, int energy, ItemType harvestedItemType, String name) {
        this.seasons = new Season[]{season};
        this.cost = cost;
        this.energy = energy;
        this.harvestedItemType = harvestedItemType;
        this.name = name;
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
}
