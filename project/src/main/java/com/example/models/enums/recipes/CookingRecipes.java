package com.example.models.enums.recipes;

import com.example.models.Slot;
import com.example.models.enums.types.FoodTypes;

public enum CookingRecipes implements Recipe {
    //TODO: INGREDIENTS ARE WRONG!
    FRIED_EGG("Fried Egg", 0, 0, 0, 0, FoodTypes.FRIED_EGG, (Slot[]) null),
    BAKED_FISH("Baked Fish", 0, 0, 0, 0, FoodTypes.BAKED_FISH, (Slot[]) null),
    SALAD("Salad", 0, 0, 0, 0, FoodTypes.SALAD, (Slot[]) null),
    OMELETTE("Omelette", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.OMELETTE, (Slot[]) null),
    PUMPKIN_PIE("Pumpkin Pie", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PUMPKIN_PIE, (Slot[]) null),
    SPAGHETTI("Spaghetti", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.SPAGHETTI, (Slot[]) null),
    PIZZA("Pizza", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PIZZA, (Slot[]) null),
    TORTILLA("Tortilla", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.TORTILLA, (Slot[]) null),
    MAKI_ROLL("Maki Roll", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.MAKI_ROLL, (Slot[]) null),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.TRIPLE_SHOT_ESPRESSO, (Slot[]) null),
    COOKIE("Cookie", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.COOKIE, (Slot[]) null),
    HASH_BROWNS("Hash Browns", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.HASH_BROWNS, (Slot[]) null),
    PANCAKES("Pancakes", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PANCAKES, (Slot[]) null),
    FRUIT_SALAD("Fruit Salad", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.FRUIT_SALAD, (Slot[]) null),
    RED_PLATE("Red Plate", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.RED_PLATE, (Slot[]) null),
    BREAD("Bread", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.BREAD, (Slot[]) null),
    SALMON_DINNER("Salmon Dinner", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.SALMON_DINNER, (Slot[]) null),
    FARMERS_LUNCH("Farmer's Lunch", 1, 0, 0, 0, FoodTypes.FARMERS_LUNCH, (Slot[]) null),
    SURVIVAL_BURGER("Survival Burger", 0, 0, 3, 0, FoodTypes.SURVIVAL_BURGER, (Slot[]) null),
    DISH_OF_THE_SEA("Dish 'O The Sea", 0, 0, 0, 2, FoodTypes.DISH_OF_THE_SEA, (Slot[]) null),
    SEAFORM_PUDDING("Seaform Pudding", 0, 0, 0, 3, FoodTypes.SEAFORM_PUDDING, (Slot[]) null),
    MINERS_TREAT("Miner's Treat", 0, 1, 0, 0, FoodTypes.MINERS_TREAT, (Slot[]) null);


    public final String name;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int fishingLevel;
    public final FoodTypes craftingResult;
    public final Slot[] ingredients;

    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            ,  FoodTypes craftingResult, Slot[] ingredients) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.craftingResult = craftingResult;
        this.ingredients = ingredients;
    }

    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , FoodTypes craftingResult, Slot ingredient) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.craftingResult = craftingResult;
        this.ingredients = new Slot[]{ingredient};
    }
}
