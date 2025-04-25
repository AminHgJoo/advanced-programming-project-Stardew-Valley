package com.example.models.enums.types;

public enum ForagingMineralsType implements ItemType {
    STONE(0, "Stone"),
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
    COPPER(5, "Copper"),
    IRON(10, "Iron"),
    GOLD(25, "Gold"),
    IRIDIUM(100, "Iridium"),
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
}
