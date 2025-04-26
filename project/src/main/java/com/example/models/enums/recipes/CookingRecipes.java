package com.example.models.enums.recipes;

import com.example.models.Slot;
import com.example.models.enums.types.itemTypes.FoodTypes;

public enum CookingRecipes {
    //TODO: INGREDIENTS ARE WRONG!
    FRIED_EGG("Fried Egg", 0, 0, 0, 0, FoodTypes.FRIED_EGG,
            new Slot[]{}),
    BAKED_FISH("Baked Fish", 0, 0, 0, 0, FoodTypes.BAKED_FISH,
            new Slot[]{}),
    SALAD("Salad", 0, 0, 0, 0, FoodTypes.SALAD,
            new Slot[]{}),
    OMELETTE("Omelette", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.OMELETTE,
            new Slot[]{}),
    PUMPKIN_PIE("Pumpkin Pie", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PUMPKIN_PIE,
            new Slot[]{}),
    SPAGHETTI("Spaghetti", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.SPAGHETTI,
            new Slot[]{}),
    PIZZA("Pizza", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PIZZA,
            new Slot[]{}),
    TORTILLA("Tortilla", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.TORTILLA,
            new Slot[]{}),
    MAKI_ROLL("Maki Roll", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.MAKI_ROLL,
            new Slot[]{}),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.TRIPLE_SHOT_ESPRESSO,
            new Slot[]{}),
    COOKIE("Cookie", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.COOKIE,
            new Slot[]{}),
    HASH_BROWNS("Hash Browns", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.HASH_BROWNS,
            new Slot[]{}),
    PANCAKES("Pancakes", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PANCAKES,
            new Slot[]{}),
    FRUIT_SALAD("Fruit Salad", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.FRUIT_SALAD,
            new Slot[]{}),
    RED_PLATE("Red Plate", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.RED_PLATE,
            new Slot[]{}),
    BREAD("Bread", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.BREAD,
            new Slot[]{}),
    SALMON_DINNER("Salmon Dinner", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.SALMON_DINNER,
            new Slot[]{}),
    FARMERS_LUNCH("Farmer's Lunch", 1, 0, 0, 0, FoodTypes.FARMERS_LUNCH,
            new Slot[]{}),
    SURVIVAL_BURGER("Survival Burger", 0, 0, 3, 0, FoodTypes.SURVIVAL_BURGER,
            new Slot[]{}),
    DISH_OF_THE_SEA("Dish 'O The Sea", 0, 0, 0, 2, FoodTypes.DISH_OF_THE_SEA,
            new Slot[]{}),
    SEAFORM_PUDDING("Seaform Pudding", 0, 0, 0, 3, FoodTypes.SEAFORM_PUDDING,
            new Slot[]{}),
    MINERS_TREAT("Miner's Treat", 0, 1, 0, 0, FoodTypes.MINERS_TREAT,
            new Slot[]{});


    public final String name;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int fishingLevel;
    public final FoodTypes craftingResultType;
    public final Slot[] ingredients;

    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , FoodTypes craftingResultType, Slot[] ingredients) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.craftingResultType = craftingResultType;
        this.ingredients = ingredients;
    }

    /// For single ingredient crafting recipes.
    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , FoodTypes craftingResultType, Slot ingredient) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.craftingResultType = craftingResultType;
        this.ingredients = new Slot[]{ingredient};
    }

    @Override
    public String toString() {
        return name;
    }
}
