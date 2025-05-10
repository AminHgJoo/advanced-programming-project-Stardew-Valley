package com.example.models.enums.types.mapObjectTypes;

import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.worldEnums.Season;

public enum ForagingCropsType {
    GRASS(Season.values(), 0, 0, MiscType.FIBER),
    COMMON_MUSHROOM(Season.values(), 40, 38, FoodTypes.COMMON_MUSHROOM),
    DAFFODIL(Season.SPRING, 30, 0, FoodTypes.DAFFODIL),
    DANDELION(Season.SPRING, 40, 25, FoodTypes.DANDELION),
    LEEK(Season.SPRING, 60, 40, FoodTypes.LEEK),
    MOREL(Season.SPRING, 150, 20, FoodTypes.MOREL),
    SALMON_BERRY(Season.SPRING, 8, 13, FoodTypes.SALMON_BERRY),
    SPRING_ONION(Season.SPRING, 8, 13, FoodTypes.SPRING_ONION),
    WILD_HORSERADISH(Season.SPRING, 50, 13, FoodTypes.WILD_HORSERADISH),
    FIDDLE_HEAD_FERN(Season.SUMMER, 90, 25, FoodTypes.FIDDLE_HEAD_FERN),
    GRAPE(Season.SUMMER, 80, 38, FoodTypes.GRAPE),
    RED_MUSHROOM(Season.SUMMER, 75, -50, FoodTypes.RED_MUSHROOM),
    SPICE_BERRY(Season.SUMMER, 80, 25, FoodTypes.SPICE_BERRY),
    SWEET_PEA(Season.SUMMER, 50, 0, FoodTypes.SWEET_PEA),
    BLACKBERRY(Season.FALL, 25, 25, FoodTypes.BLACKBERRY),
    CHANTERELLE(Season.FALL, 160, 75, FoodTypes.CHANTERELLE),
    HAZELNUT(Season.FALL, 40, 38, FoodTypes.HAZELNUT),
    PURPLE_MUSHROOM(Season.FALL, 90, 30, FoodTypes.PURPLE_MUSHROOM),
    WILD_PLUM(Season.FALL, 80, 25, FoodTypes.WILD_PLUM),
    CROCUS(Season.WINTER, 60, 0, FoodTypes.CROCUS),
    CRYSTAL_FRUIT(Season.WINTER, 150, 63, FoodTypes.CRYSTAL_FRUIT),
    HOLLY(Season.WINTER, 80, -37, FoodTypes.HOLLY),
    SNOW_YAM(Season.WINTER, 100, 30, FoodTypes.SNOW_YAM),
    WINTER_ROOT(Season.WINTER, 70, 25, FoodTypes.WINTER_ROOT),
    ;

    private final Season[] seasons;
    private final int cost;
    private final int energy;
    private final ItemType harvestedItemType;

    ForagingCropsType(Season[] seasons, int cost, int energy, ItemType harvestedItemType) {
        this.seasons = seasons;
        this.cost = cost;
        this.energy = energy;
        this.harvestedItemType = harvestedItemType;
    }

    ForagingCropsType(Season season, int cost, int energy, ItemType harvestedItemType) {
        this.seasons = new Season[]{season};
        this.cost = cost;
        this.energy = -energy;
        this.harvestedItemType = harvestedItemType;
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
