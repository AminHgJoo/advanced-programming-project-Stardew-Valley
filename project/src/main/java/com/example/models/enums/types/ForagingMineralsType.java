package com.example.models.enums.types;

public enum ForagingMineralsType {
    STONE(0),
    QUARTZ(25),
    EARTHCRYSTAL(50),
    FROZENTEAR(75),
    FIREQUARTZ(100),
    EMERALD(250),
    AQUAMARINE(180),
    RUBY(250),
    AMETHYST(100),
    TOPAZ(80),
    JADE(200),
    DIAMOND(750),
    PRISMATICSHARD(2000),
    COPPER(5),
    IRON(10),
    GOLD(25),
    IRIDIUM(100),
    COAL(15);

    private final int sellPrice;
    public final String name;

    ForagingMineralsType(int sellPrice) {
        this.sellPrice = sellPrice;
        this.name = this.toString();
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
