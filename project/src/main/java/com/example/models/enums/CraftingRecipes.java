package com.example.models.enums;

import com.example.models.Slot;

import java.util.ArrayList;

public enum CraftingRecipes implements Recipes {
    CHERRY_BOMB("Cherry Bomb", "Destroys everything in a 3 tile radius.", 0, 1, 0, 50, new ArrayList<>()),
    BOMB("Bomb", "Destroys everything in a 5 tile radius.", 0, 2, 0, 50, new ArrayList<>()),
    MEGA_BOMB("Mega Bomb", "Destroys everything in a 7 tile radius.", 0, 3, 0, 50, new ArrayList<>()),
    SPRINKLER("Sprinkler", "Waters 4 adjacent crops.", 1, 0, 0, 0, new ArrayList<>()),
    QUALITY_SPRINKLER("Quality Sprinkler", "Waters 8 adjacent crops.", 2, 0, 0, 0, new ArrayList<>()),
    IRIDIUM_SPRINKLER("Iridium Sprinkler", "Waters 24 adjacent crops.", 3, 0, 0, 0, new ArrayList<>()),
    CHARCOAL_KLIN("Charcoal Klin", "Converts 10 wood to 1 coal.", 1, 0, 0, 0, new ArrayList<>()),
    FURNACE("Furnace", "Convert ores and coal into ingots.", 0, 0, 0, 0, new ArrayList<>()),
    SCARE_CROW("Scare Crow","Prevents crow attacks in an 8 tile radius." , 0, 0, 0, 0, new ArrayList<>()),
    DELUXE_SCARE_CROW("Deluxe Scarecrow", "Prevents crow attacks in a 12 tile radius.", 2, 0, 0, 0, new ArrayList<>()),
    BEE_HOUSE("Bee House", "Produces honey.", 1, 0, 0, 0, new ArrayList<>()),
    CHEESE_PRESS("Cheese Press", "Produces cheese from milk.", 2, 0, 0, 0, new ArrayList<>()),
    KEG("Keg", "Ferments fruits and vegetables into drinks.", 3, 0, 0, 0, new ArrayList<>()),
    LOOM("Loom", "Processes wool.", 3, 0, 0, 0, new ArrayList<>()),
    MAYONNAISE_MACHINE("Mayonnaise Machine", "Produces mayonnaise from eggs.", 0, 0, 0, 0, new ArrayList<>()),
    OIL_MAKER("Oil Maker", "Produces oil from truffles.", 3, 0, 0, 0, new ArrayList<>()),
    PRESERVES_JAR("Preserves Jar", "Produces jam from fruits and vegetables.", 2, 0, 0, 0, new ArrayList<>()),
    DEHYDRATOR("Dehydrator", "Dries fruits and mushrooms.", 0, 0, 0, 0, new ArrayList<>()),
    FISH_SMOKER("Fish Smoker", "Smokes fish while preserving their quality.", 0, 0, 0, 0, new ArrayList<>()),
    MYSTIC_TREE_SEED("Mystic tree seed", "Can be planted to grow into a mystic tree.", 0, 0, 4, 100, new ArrayList<>());

    public final String name;
    public final String description;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int sellingPrice;
    public final ArrayList<Slot> ingredients;

    CraftingRecipes(String name, String description, int farmingLevel, int miningLevel, int foragingLevel, int sellingPrice, ArrayList<Slot> ingredients) {
        this.name = name;
        this.description = description;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.sellingPrice = sellingPrice;
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return name + " Recipe: " + description;
    }
}
