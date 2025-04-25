package com.example.models.enums.types;

import com.example.models.items.buffs.FoodBuff;

public enum FoodTypes implements ItemType {
    APRICOT("Apricot", 38, new FoodBuff("", 0, 0), 59),
    CHERRY("Cherry", 38, new FoodBuff("", 0, 0), 80),
    BANANA("Banana", 75, new FoodBuff("", 0, 0), 150),
    MANGO("Mango", 100, new FoodBuff("", 0, 0), 130),
    ORANGE("Orange", 38, new FoodBuff("", 0, 0), 100),
    PEACH("Peach", 38, new FoodBuff("", 0, 0), 140),
    APPLE("Apple", 38, new FoodBuff("", 0, 0), 100),
    POMEGRANATE("Pomegranate", 38, new FoodBuff("", 0, 0), 140),
    OAK_RESIN("Oak Resin", 0, new FoodBuff("", 0, 0), 150),
    MAPLE_SYRUP("Maple Syrup", 0, new FoodBuff("", 0, 0), 200),
    PINE_TAR("Pine Tar", 0, new FoodBuff("", 0, 0), 100),
    SAP("Sap", -2, new FoodBuff("", 0, 0), 2),
    COMMON_MUSHROOM("Common Mushroom", 38, new FoodBuff("", 0, 0), 40),
    MYSTIC_SYRUP("Mystic Syrup", 500, new FoodBuff("", 0, 0), 1000),
    FRIED_EGG("Fried Egg", 50, new FoodBuff("", 0, 0), 35),
    BAKED_FISH("Baked Fish", 75, new FoodBuff("", 0, 0), 100),
    SALAD("Salad", 113, new FoodBuff("", 0, 0), 110),
    OMELETTE("Omelette", 100, new FoodBuff("", 0, 0), 125),
    PUMPKIN_PIE("Pumpkin Pie", 225, new FoodBuff("", 0, 0), 385),
    SPAGHETTI("Spaghetti", 75, new FoodBuff("", 0, 0), 120),
    PIZZA("Pizza", 150, new FoodBuff("", 0, 0), 300),
    TORTILLA("Tortilla", 50, new FoodBuff("", 0, 0), 50),
    MAKI_ROLL("Maki Roll", 100, new FoodBuff("", 0, 0), 100),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", 200, new FoodBuff("maxEnergy", 100, 5), 450),
    COOKIE("Cookie", 90, new FoodBuff("", 0, 0), 140),
    HASH_BROWNS("Hash Browns", 90, new FoodBuff("farming", 1, 5), 120),
    PANCAKES("Pancakes", 90, new FoodBuff("foraging", 1, 11), 80),
    FRUIT_SALAD("Fruit Salad", 263, new FoodBuff("", 0, 0), 450),
    RED_PLATE("Red Plate", 240, new FoodBuff("maxEnergy", 50, 3), 400),
    BREAD("Bread", 50, new FoodBuff("", 0, 0), 60),
    SALMON_DINNER("Salmon Dinner", 125, new FoodBuff("", 0, 0), 300),
    VEGETABLE_MEDLEY("Vegetable Medley", 165, new FoodBuff("", 0, 0), 120),
    FARMERS_LUNCH("Farmer's Lunch", 200, new FoodBuff("farming", 1, 5), 150),
    SURVIVAL_BURGER("Survival Burger", 125, new FoodBuff("foraging", 1, 5), 180),
    DISH_OF_THE_SEA("Dish O' The Sea", 150, new FoodBuff("fishing", 1, 5), 220),
    SEAFORM_PUDDING("Seaform Pudding", 175, new FoodBuff("fishing", 1, 10), 300),
    MINERS_TREAT("Miner's Treat", 125, new FoodBuff("mining", 1, 5), 200);

    //TODO: Add buff to player's list of buffs.
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
