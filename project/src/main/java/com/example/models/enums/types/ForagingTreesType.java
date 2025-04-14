// TODO null means all seasons

package com.example.models.enums.types;

import com.example.models.enums.Season;

public enum ForagingTreesType {
    ACORNS(null),
    MAPLE_SEEDS(null),
    PINE_CONES(null),
    MAHOGANY_SEEDS(null),
    MUSHROOM_TREE_SEEDS(null);

    private final Season season;

    ForagingTreesType(Season season) {
        this.season = season;
    }

    public Season getSeason() {
        return season;
    }
}
