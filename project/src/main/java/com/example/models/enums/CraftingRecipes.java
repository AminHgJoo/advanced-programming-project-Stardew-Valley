package com.example.models.enums;

import com.example.models.Slot;

import java.util.ArrayList;

public enum CraftingRecipes {
    CHERRY_BOMB("Cherry Bomb", 0,1,0,50),
    BOMB("Bomb", 0,2,0,50),
    MEGA_BOMB("Mega Bomb", 0,3,0,50),
    SPRINKLER("Sprinkler", 1,0,0,0),
    QUALITY_SPRINKLER("Quality Sprinkler",2,0,0,0 ),
    IRIDIUM_SPRINKLER("Iridium Sprinkler",3,0,0,0),
    CHARCOAL_KLIN("Charcoal Klin",1,0,0,0),
    FURNACE("Furnace",0,0,0,0),
    SCARE_CROW("Scare Crow",0,0,0,0),
    DELUXE_SCARE_CROW("Deluxe Scarecrow",2,0,0,0),
    BEE_HOUSE("Bee House",1,0,0,0),
    CHEESE_PRESS("Cheese Press",2,0,0,0),
    KEG("Keg",3,0,0,0),
    LOOM("Loom",3,0,0,0),
    MAYONNAISE_MACHINE("Mayonnaise Machine",0,0,0,0),
    OIL_MAKER("Oil Maker",3,0,0,0),
    PRESERVES_JAR("Preserves Jar",2,0,0,0),
    DEHYDRATOR("Dehydrator",0,0,0,0),
    FISH_SMOKER("Fish Smoker",0,0,0,0),
    MYSTIC_TREE_SEED("Mystic tree seed",0,0,4,100);
    private final String name;
    private final int farmingLevel;
    private final int miningLevel;
    private final int foragingLevel;
    private final int sellingPrice;
    private final ArrayList<Slot> ingredients;


    CraftingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int sellingPrice, ArrayList<Slot> ingredients) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.sellingPrice = sellingPrice;
        this.ingredients = ingredients;
    }
}
