package com.example.models.enums.recipes;

import com.example.models.Slot;
import com.example.models.enums.types.FoodTypes;
import com.example.models.items.Food;

public enum CookingRecipes implements Recipe {
    //TODO: INGREDIENTS ARE WRONG!
    FRIED_EGG("Fried Egg", 0, 0, 0, 0, 35, FoodTypes.FRIED_EGG, new Slot(new Food(FoodTypes.FRIED_EGG), 1)),
    BAKED_FISH("Baked Fish", 0, 0, 0, 0, 100, FoodTypes.BAKED_FISH, new Slot(new Food(FoodTypes.BAKED_FISH), 1)),
    SALAD("Salad", 0, 0, 0, 0, 110, FoodTypes.SALAD, new Slot(new Food(FoodTypes.SALAD), 1)),
    OMELETTE("Omelette", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 125, FoodTypes.OMELETTE, new Slot(new Food(FoodTypes.OMELETTE), 1)),
    ;


    public final String name;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int fishingLevel;
    public final int sellingPrice;
    public final FoodTypes craftingResult;
    public final Slot[] ingredients;

    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , int sellingPrice, FoodTypes craftingResult, Slot[] ingredients) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.sellingPrice = sellingPrice;
        this.craftingResult = craftingResult;
        this.ingredients = ingredients;
    }

    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , int sellingPrice, FoodTypes craftingResult, Slot ingredient) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.sellingPrice = sellingPrice;
        this.craftingResult = craftingResult;
        this.ingredients = new Slot[]{ingredient};
    }
}
