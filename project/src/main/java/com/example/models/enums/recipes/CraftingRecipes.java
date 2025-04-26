package com.example.models.enums.recipes;

import com.example.models.Slot;

public enum CraftingRecipes {

    //TODO: Ingredients.
    CHERRY_BOMB("Cherry Bomb", "Destroys everything in a 3 tile radius.", 0, 1, 0, 0, 50, null),
    BOMB("Bomb", "Destroys everything in a 5 tile radius.", 0, 2, 0, 0, 50, null),
    MEGA_BOMB("Mega Bomb", "Destroys everything in a 7 tile radius.", 0, 3, 0, 0, 50, null),
    SPRINKLER("Sprinkler", "Waters 4 adjacent crops.", 1, 0, 0, 0, 0, null),
    QUALITY_SPRINKLER("Quality Sprinkler", "Waters 8 adjacent crops.", 2, 0, 0, 0, 0, null),
    IRIDIUM_SPRINKLER("Iridium Sprinkler", "Waters 24 adjacent crops.", 3, 0, 0, 0, 0, null),
    CHARCOAL_KLIN("Charcoal Klin", "Converts 10 wood to 1 coal.", 1, 0, 0, 0, 0, null),
    FURNACE("Furnace", "Convert ores and coal into ingots.", 0, 0, 0, 0, 0, null),
    SCARE_CROW("Scare Crow", "Prevents crow attacks in an 8 tile radius.", 0, 0, 0, 0, 0, null),
    DELUXE_SCARE_CROW("Deluxe Scarecrow", "Prevents crow attacks in a 12 tile radius.", 2, 0, 0, 0, 0, null),
    BEE_HOUSE("Bee House", "Produces honey.", 1, 0, 0, 0, 0, null),
    CHEESE_PRESS("Cheese Press", "Produces cheese from milk.", 2, 0, 0, 0, 0, null),
    KEG("Keg", "Ferments fruits and vegetables into drinks.", 3, 0, 0, 0, 0, null),
    LOOM("Loom", "Processes wool.", 3, 0, 0, 0, 0, null),
    MAYONNAISE_MACHINE("Mayonnaise Machine", "Produces mayonnaise from eggs.", 0, 0, 0, 0, 0, null),
    OIL_MAKER("Oil Maker", "Produces oil from truffles.", 3, 0, 0, 0, 0, null),
    PRESERVES_JAR("Preserves Jar", "Produces jam from fruits and vegetables.", 2, 0, 0, 0, 0, null),
    DEHYDRATOR("Dehydrator", "Dries fruits and mushrooms.", 0, 0, 0, 0, 0, null),
    FISH_SMOKER("Fish Smoker", "Smokes fish while preserving their quality.", 0, 0, 0, 0, 0, null),
    MYSTIC_TREE_SEED("Mystic tree seed", "Can be planted to grow into a mystic tree.", 0, 0, 4, 0, 100, null);

    public final String name;
    public final String description;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int sellingPrice;
    public final int fishingLevel;
    public final Slot[] ingredients;

    CraftingRecipes(String name, String description, int farmingLevel, int miningLevel, int foragingLevel
            , int fishingLevel, int sellingPrice, Slot[] ingredients) {
        this.name = name;
        this.description = description;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.sellingPrice = sellingPrice;
        this.fishingLevel = fishingLevel;
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return name + " Recipe: " + description;
    }
}
