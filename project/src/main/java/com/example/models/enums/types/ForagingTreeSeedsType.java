package com.example.models.enums.types;

import com.example.models.enums.worldEnums.Season;

public enum ForagingTreeSeedsType implements ItemType {

    ACORNS(Season.values(), "Acorns"),
    MAPLE_SEEDS(Season.values(), "Maple Seeds"),
    PINE_CONES(Season.values(), "Pine Cones"),
    MAHOGANY_SEEDS(Season.values(), "Mahogany Seeds"),
    MUSHROOM_TREE_SEEDS(Season.values(), "Mushroom Tree Seeds"),
    ;

    public final Season[] growthSeasons;
    public final String name;

    ForagingTreeSeedsType(Season[] season, String name) {
        this.growthSeasons = season;
        this.name = name;
    }
}
