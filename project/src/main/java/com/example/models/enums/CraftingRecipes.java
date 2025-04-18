package com.example.models.enums;

import com.example.models.Slot;

import java.util.ArrayList;

public enum CraftingRecipes {
    CHERRY_BOMB("Cherry Bomb", 0, 1, 0, 50, new ArrayList<>()),
    BOMB("Bomb", 0, 2, 0, 50, new ArrayList<>()),
    MEGA_BOMB("Mega Bomb", 0, 3, 0, 50, new ArrayList<>()),
    SPRINKLER("Sprinkler", 1, 0, 0, 0, new ArrayList<>()),
    QUALITY_SPRINKLER("Quality Sprinkler", 2, 0, 0, 0, new ArrayList<>()),
    IRIDIUM_SPRINKLER("Iridium Sprinkler", 3, 0, 0, 0, new ArrayList<>()),
    CHARCOAL_KLIN("Charcoal Klin", 1, 0, 0, 0, new ArrayList<>()),
    FURNACE("Furnace", 0, 0, 0, 0, new ArrayList<>()),
    SCARE_CROW("Scare Crow", 0, 0, 0, 0, new ArrayList<>()),
    DELUXE_SCARE_CROW("Deluxe Scarecrow", 2, 0, 0, 0, new ArrayList<>()),
    BEE_HOUSE("Bee House", 1, 0, 0, 0, new ArrayList<>()),
    CHEESE_PRESS("Cheese Press", 2, 0, 0, 0, new ArrayList<>()),
    KEG("Keg", 3, 0, 0, 0, new ArrayList<>()),
    LOOM("Loom", 3, 0, 0, 0, new ArrayList<>()),
    MAYONNAISE_MACHINE("Mayonnaise Machine", 0, 0, 0, 0, new ArrayList<>()),
    OIL_MAKER("Oil Maker", 3, 0, 0, 0, new ArrayList<>()),
    PRESERVES_JAR("Preserves Jar", 2, 0, 0, 0, new ArrayList<>()),
    DEHYDRATOR("Dehydrator", 0, 0, 0, 0, new ArrayList<>()),
    FISH_SMOKER("Fish Smoker", 0, 0, 0, 0, new ArrayList<>()),
    MYSTIC_TREE_SEED("Mystic tree seed", 0, 0, 4, 100, new ArrayList<>());
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
