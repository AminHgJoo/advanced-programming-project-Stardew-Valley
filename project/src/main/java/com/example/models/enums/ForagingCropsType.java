// TODO null means all seasons

package com.example.models.enums;

public enum ForagingCropsType {
    COMMON_MUSHROOM(null,40,38),
    DAFFODIL(Season.SPRING,30,0),
    DANDELION(Season.SPRING,40,25),
    LEEK(Season.SPRING,60,40),
    MOREL(Season.SPRING,150,20),
    SALMON_BERRY(Season.SPRING,8,13),
    SPRING_ONION(Season.SPRING,8,13),
    WILD_HORSERADISH(Season.SPRING,50,13),
    FIDDLE_HEAD_FERN(Season.SUMMER,90,25),
    GRAPE(Season.SUMMER,80,38),
    RED_MUSHROOM(Season.SUMMER,75,-50),
    SPICE_BERRY(Season.SUMMER,80,25),
    SWEET_PEA(Season.SUMMER,50,0),
    BLACKBERRY(Season.AUTUMN,25,25),
    CHANTERELLE(Season.AUTUMN,160,75),
    HAZELNUT(Season.AUTUMN,40,38),
    PURPLE_MUSHROOM(Season.AUTUMN,90,30),
    WILD_PLUM(Season.AUTUMN,80,25),
    CROCUS(Season.WINTER , 60,0),
    CRYSTAL_FRUIT(Season.WINTER , 150,63),
    HOLLY(Season.WINTER , 80,-37),
    SNOW_YAM(Season.WINTER , 100,30),
    WINTER_ROOT(Season.WINTER , 70,25),
    ;
    private final Season season;
    private final int cost;
    private final int energy;
    ForagingCropsType(Season season, int cost, int energy) {
        this.season = season;
        this.cost = cost;
        this.energy = -energy;
    }

    public Season getSeason() {
        return season;
    }

    public int getCost() {
        return cost;
    }

    public int getEnergy() {
        return energy;
    }
}
