package com.example.models.enums.types;

import com.example.models.enums.Season;

public enum ForagingTreesType {

    ACORNS(Season.values()),
    MAPLE_SEEDS(Season.values()),
    PINE_CONES(Season.values()),
    MAHOGANY_SEEDS(Season.values()),
    MUSHROOM_TREE_SEEDS(Season.values()),;

    private final Season[] growthSeasons;

    ForagingTreesType(Season[] season) {
        this.growthSeasons = season;
    }

    public Season[] getGrowthSeasons() {
        return growthSeasons;
    }
}
