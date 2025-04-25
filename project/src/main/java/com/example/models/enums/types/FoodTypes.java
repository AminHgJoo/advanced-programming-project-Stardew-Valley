package com.example.models.enums.types;

import com.example.models.items.buffs.FoodBuff;

public enum FoodTypes implements ItemType {
    APRICOT("Apricot", 0, new FoodBuff("", 0)),
    CHERRY("Cherry", 0, new FoodBuff("", 0)),
    BANANA("Banana", 0, new FoodBuff("", 0)),
    MANGO("Mango", 0, new FoodBuff("", 0)),
    ORANGE("Orange", 0, new FoodBuff("", 0)),
    PEACH("Peach", 0, new FoodBuff("", 0)),
    APPLE("Apple", 0, new FoodBuff("", 0)),
    POMEGRANATE("Pomegranate", 0, new FoodBuff("", 0)),
    OAK_RESIN("Oak Resin", 0, new FoodBuff("", 0)),
    MAPLE_SYRUP("Maple Syrup", 0, new FoodBuff("", 0)),
    PINE_TAR("Pine Tar", 0, new FoodBuff("", 0)),
    SAP("Sap", 0, new FoodBuff("", 0)),
    COMMON_MUSHROOM("Common Mushroom", 0, new FoodBuff("", 0)),
    MYSTIC_SYRUP("Mystic Syrup", 0, new FoodBuff("", 0)),
    ;

    final public String name;
    final public int energy;
    final public FoodBuff foodBuff;

    FoodTypes(String name, int energy, FoodBuff foodBuff) {
        this.name = name;
        this.energy = energy;
        this.foodBuff = foodBuff;
    }
}
