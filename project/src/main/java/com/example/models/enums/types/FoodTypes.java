package com.example.models.enums.types;

import com.example.models.items.buffs.FoodBuff;

public enum FoodTypes implements ItemType {
    APRICOT("Apricot", 38, new FoodBuff("", 0), 59),
    CHERRY("Cherry", 38, new FoodBuff("", 0), 80),
    BANANA("Banana", 75, new FoodBuff("", 0), 150),
    MANGO("Mango", 100, new FoodBuff("", 0), 130),
    ORANGE("Orange", 38, new FoodBuff("", 0), 100),
    PEACH("Peach", 38, new FoodBuff("", 0), 140),
    APPLE("Apple", 38, new FoodBuff("", 0), 100),
    POMEGRANATE("Pomegranate", 38, new FoodBuff("", 0), 140),
    OAK_RESIN("Oak Resin", 150, new FoodBuff("", 0), ),
    MAPLE_SYRUP("Maple Syrup", 0, new FoodBuff("", 0), ),
    PINE_TAR("Pine Tar", 0, new FoodBuff("", 0), ),
    SAP("Sap", 0, new FoodBuff("", 0), ),
    COMMON_MUSHROOM("Common Mushroom", 0, new FoodBuff("", 0), ),
    MYSTIC_SYRUP("Mystic Syrup", 0, new FoodBuff("", 0), ),
    FRIED_EGG("Fried Egg", 50, new FoodBuff("", 0), ),
    BAKED_FISH("Baked Fish", 75, new FoodBuff("", 0), );

    final public String name;
    final public int energy;
    final public FoodBuff foodBuff;
    final public int value;

    FoodTypes(String name, int energy, FoodBuff foodBuff, int value) {
        this.name = name;
        this.energy = energy;
        this.foodBuff = foodBuff;
        this.value = value;
    }
}
