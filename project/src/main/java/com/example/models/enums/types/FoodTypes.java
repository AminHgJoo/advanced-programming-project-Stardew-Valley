package com.example.models.enums.types;

import com.example.models.Player;
import com.example.models.items.FoodBuff;

public enum FoodTypes {
    APRICOT("Apricot", 0, (Player _) -> {}),
    CHERRY("Cherry", 0, (Player _) -> {}),
    BANANA("Banana", 0, (Player _) -> {}),
    MANGO("Mango", 0, (Player _) -> {}),
    ORANGE("Orange", 0, (Player _) -> {}),
    PEACH("Peach", 0, (Player _) -> {}),
    APPLE("Apple", 0, (Player _) -> {}),
    POMEGRANATE("Pomegranate", 0, (Player _) -> {}),
    OAK_RESIN("Oak Resin", 0, (Player _) -> {}),
    MAPLE_SYRUP("Maple Syrup", 0, (Player _) -> {}),
    PINE_TAR("Pine Tar", 0, (Player _) -> {}),
    SAP("Sap", 0, (Player _) -> {}),
    COMMON_MUSHROOM("Common Mushroom", 0, (Player _) -> {}),
    MYSTIC_SYRUP("Mystic Syrup", 0, (Player _) -> {}),
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
