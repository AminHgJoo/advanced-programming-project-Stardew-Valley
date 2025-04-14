// TODO null means all seasons

package com.example.models.enums;

public enum ForagingCropsType {
    ;
    private final Season season;
    private final int cost;
    private final int energy;
    ForagingCropsType(Season season, int cost, int energy) {
        this.season = season;
        this.cost = cost;
        this.energy = energy;
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
