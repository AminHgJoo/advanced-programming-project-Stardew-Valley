package com.common.models.enums.types.itemTypes;

import com.common.models.Slot;
import com.common.models.enums.Quality;
import com.common.models.items.ForagingMineralItem;

public enum ForagingMineralsType implements ItemType {
    STONE(2, "Stone", "stone"),
    QUARTZ(25, "Quartz", "quartz"),
    EARTH_CRYSTAL(50, "Earth Crystal", "earthCrystal"),
    FROZEN_TEAR(75, "Frozen Tear", "frozenTear"),
    FIRE_QUARTZ(100, "Fire Quartz", "fireQuartz"),
    EMERALD(250, "Emerald", "emerald"),
    AQUAMARINE(180, "Aquamarine", "aquamarine"),
    RUBY(250, "Ruby", "ruby"),
    AMETHYST(100, "Amethyst", "amethyst"),
    TOPAZ(80, "Topaz", "topaz"),
    JADE(200, "Jade", "jade"),
    DIAMOND(750, "Diamond", "diamond"),
    PRISMATIC_SHARD(2000, "Prismatic Shard", "prismaticShard"),
    COPPER_ORE(5, "Copper Ore", "copperOre"),
    IRON_ORE(10, "Iron Ore", "ironOre"),
    GOLD_ORE(25, "Gold Ore", "goldOre"),
    IRIDIUM_ORE(100, "Iridium Ore", "iridiumOre"),
    COAL(15, "Coal", "coal"),
    ;

    public final String name;
    private final int sellPrice;
    private final String textureName;

    ForagingMineralsType(int sellPrice, String name, String textureName) {
        this.sellPrice = sellPrice;
        this.name = name;
        this.textureName = textureName;
    }

    public static int getPriceByName(String name) {
        for (ForagingMineralsType type : ForagingMineralsType.values()) {
            if (type.name.equals(name)) {
                return type.sellPrice;
            }
        }
        return 0;
    }


    public String getName() {
        return name;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public Slot createAmountOfItem(int amount, Quality quality) {
        return new Slot(new ForagingMineralItem(Quality.DEFAULT, this), amount);
    }

    public String getTextureName() {
        return textureName;
    }
}
