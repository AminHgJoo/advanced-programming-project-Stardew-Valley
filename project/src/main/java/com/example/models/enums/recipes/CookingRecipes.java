package com.example.models.enums.recipes;

import com.example.models.Slot;
import com.example.models.enums.types.FoodTypes;

import java.util.ArrayList;

public enum CookingRecipes implements Recipe {
    ;

    public final String name;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int fishingLevel;
    public final int sellingPrice;
    public final FoodTypes craftingResult;
    public final ArrayList<Slot> ingredients;

    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , int sellingPrice, FoodTypes craftingResult, ArrayList<Slot> ingredients) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.sellingPrice = sellingPrice;
        this.craftingResult = craftingResult;
        this.ingredients = ingredients;
    }
}
