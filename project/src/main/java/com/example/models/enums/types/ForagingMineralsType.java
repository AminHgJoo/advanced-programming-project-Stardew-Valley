package com.example.models.enums.types;

public enum ForagingMineralsType {
    STONE(0),
    QUARTZ(25),
    EARTH_CRYSTAL(50),
    FROZEN_TEAR(75),
    FIRE_QUARTZ(100),
    EMERALD(250),
    AQUAMARINE(180),
    RUBY(250),
    AMETHYST(100),
    TOPAZ(80),
    JADE(200),
    DIAMOND(750),
    PRISMATIC_SHARD(2000),
    COPPER(5),
    IRON(10),
    GOLD(25),
    IRIDIUM(100),
    COAL(15);

    private final int sellPrice;

    ForagingMineralsType(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
