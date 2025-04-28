package com.example.models.enums.types.itemTypes;

import com.example.models.Slot;
import com.example.models.enums.worldEnums.Season;
import com.example.models.items.TreeSeed;

public enum TreeSeedsType implements ItemType {
    ACORNS(Season.values(), "Acorns", 0),
    MAPLE_SEEDS(Season.values(), "Maple Seeds", 0),
    PINE_CONES(Season.values(), "Pine Cones", 0),
    MAHOGANY_SEEDS(Season.values(), "Mahogany Seeds", 0),
    MUSHROOM_TREE_SEEDS(Season.values(), "Mushroom Tree Seeds", 0),
    //Non Foraging seeds
    MYSTIC_TREE_SEED(Season.values(), "Mystic tree seed", 100),
    APRICOT_SAPLING(Season.SPRING, "Apricot Sapling", 0),
    CHERRY_SAPLING(Season.SPRING, "Cherry Sapling", 0),
    BANANA_SAPLING(Season.SUMMER, "Banana Sapling", 0),
    MANGO_SAPLING(Season.SUMMER, "Mango Sapling", 0),
    ORANGE_SAPLING(Season.SUMMER, "Orange Sapling", 0),
    PEACH_SAPLING(Season.SUMMER, "Peach Sapling", 0),
    APPLE_SAPLING(Season.FALL, "Apple Sapling", 0),
    POMEGRANATE_SAPLING(Season.FALL, "Pomegranate Sapling", 0),
    ;

    public final Season[] growthSeasons;
    public final String name;
    public final int value;

    TreeSeedsType(Season[] season, String name, int value) {
        this.growthSeasons = season;
        this.name = name;
        this.value = value;
    }

    TreeSeedsType(Season season, String name, int value) {
        this.growthSeasons = new Season[]{season};
        this.name = name;
        this.value = value;
    }

    public static TreeSeedsType findTreeTypeByName(String name){
        for (TreeSeedsType tree : TreeSeedsType.values()) {
            if (tree.name.compareToIgnoreCase(name) == 0) {
                return tree;
            }
        }
        return null;
    }

    @Override
    public Slot createAmountOfItem(int amount) {
        return new Slot(new TreeSeed(this), amount);
    }
}
