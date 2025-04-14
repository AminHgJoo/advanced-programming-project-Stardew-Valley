package com.example.models.enums;

public enum ForagingMineralsType {
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

    private final int cellPrice;
    ForagingMineralsType(int cellPrice) {
        this.cellPrice = cellPrice;
    }

    public int getCellPrice() {
        return cellPrice;
    }
}
