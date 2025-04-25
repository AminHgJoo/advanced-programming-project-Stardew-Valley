package com.example.models.enums;

import com.example.models.Slot;

import java.util.ArrayList;

public enum CookingRecipes implements Recipes {
    ;

    public final String name;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int fishingLevel;
    public final int sellingPrice;
    public final ArrayList<Slot> ingredients;


}
