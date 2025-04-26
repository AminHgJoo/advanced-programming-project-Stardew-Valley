package com.example.models.enums.types.itemTypes;

import com.example.models.Slot;
import com.example.models.enums.Quality;
import com.example.models.items.ForagingMineral;

public enum ForagingMineralsType implements ItemType {
    STONE(2, "Stone"),
    QUARTZ(25, "Quartz"),
    EARTH_CRYSTAL(50, "Earth Crystal"),
    FROZEN_TEAR(75, "Frozen Tear"),
    FIRE_QUARTZ(100, "Fire Quartz"),
    EMERALD(250, "Emerald"),
    AQUAMARINE(180, "Aquamarine"),
    RUBY(250, "Ruby"),
    AMETHYST(100, "Amethyst"),
    TOPAZ(80, "Topaz"),
    JADE(200, "Jade"),
    DIAMOND(750, "Diamond"),
    PRISMATIC_SHARD(2000, "Prismatic Shard"),
    COPPER_ORE(5, "Copper Ore"),
    IRON_ORE(10, "Iron Ore"),
    GOLD_ORE(25, "Gold Ore"),
    IRIDIUM_ORE(100, "Iridium Ore"),
    COAL(15, "Coal");

    private final int sellPrice;
    public final String name;

    ForagingMineralsType(int sellPrice, String name) {
        this.sellPrice = sellPrice;
        this.name = name;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public Slot createAmountOfItem(int amount) {
        return new Slot(new ForagingMineral(Quality.DEFAULT, this), amount);
    }
}
