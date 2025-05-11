package com.example.models.enums.recipes;

import com.example.models.Slot;
import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.ForagingMineralsType;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.types.itemTypes.TreeSeedsType;
import com.example.models.items.ForagingMineral;
import com.example.models.items.Misc;
import com.example.models.items.TreeSeed;

import java.util.Arrays;

public enum CraftingRecipes {
    CHERRY_BOMB("Cherry Bomb", "Destroys everything in a 3 tile radius.", 0, 1, 0, 0, 50,
            new Slot[]{new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COPPER_ORE), 4), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 1)},
            MiscType.CHERRY_BOMB),
    BOMB("Bomb", "Destroys everything in a 5 tile radius.", 0, 2, 0, 0, 50,
            new Slot[]{new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.IRON_ORE), 4), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 1)},
            MiscType.BOMB),
    MEGA_BOMB("Mega Bomb", "Destroys everything in a 7 tile radius.", 0, 3, 0, 0, 50,
            new Slot[]{new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.GOLD_ORE), 4), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 1)},
            MiscType.MEGA_BOMB),
    SPRINKLER("Sprinkler", "Waters 4 adjacent crops.", 1, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.COPPER_BAR), 1), new Slot(new Misc(MiscType.IRON_BAR), 1)},
            MiscType.SPRINKLER),
    QUALITY_SPRINKLER("Quality Sprinkler", "Waters 8 adjacent crops.", 2, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.GOLD_BAR), 1), new Slot(new Misc(MiscType.IRON_BAR), 1)},
            MiscType.QUALITY_SPRINKLER),
    IRIDIUM_SPRINKLER("Iridium Sprinkler", "Waters 24 adjacent crops.", 3, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.GOLD_BAR), 1), new Slot(new Misc(MiscType.IRIDIUM_BAR), 1)},
            MiscType.IRIDIUM_SPRINKLER),
    CHARCOAL_KLIN("Charcoal Klin", "Converts 10 wood to 1 coal.", 1, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.COPPER_BAR), 2), new Slot(new Misc(MiscType.WOOD), 20)},
            MiscType.CHARCOAL_KLIN),
    FURNACE("Furnace", "Convert ores and coal into ingots.", 0, 0, 0, 0, 0,
            new Slot[]{new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.STONE), 25), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COPPER_ORE), 20)},
            MiscType.FURNACE),
    SCARE_CROW("Scare Crow", "Prevents crow attacks in an 8 tile radius.", 0, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.FIBER), 20), new Slot(new Misc(MiscType.WOOD), 50), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 1)},
            MiscType.SCARE_CROW),
    DELUXE_SCARE_CROW("Deluxe Scarecrow", "Prevents crow attacks in a 12 tile radius.", 2, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.FIBER), 20), new Slot(new Misc(MiscType.WOOD), 50), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 1), new Slot(new Misc(MiscType.IRIDIUM_BAR), 1)},
            MiscType.DELUXE_SCARE_CROW),
    BEE_HOUSE("Bee House", "Produces honey.", 1, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.IRON_BAR), 1), new Slot(new Misc(MiscType.WOOD), 40), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 8)},
            MiscType.BEE_HOUSE),
    CHEESE_PRESS("Cheese Press", "Produces cheese from milk.", 2, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.COPPER_BAR), 1), new Slot(new Misc(MiscType.WOOD), 45), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.STONE), 45)},
            MiscType.CHEESE_PRESS),
    KEG("Keg", "Ferments fruits and vegetables into drinks.", 3, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.IRON_BAR), 1), new Slot(new Misc(MiscType.COPPER_BAR), 1), new Slot(new Misc(MiscType.WOOD), 30)},
            MiscType.KEG),
    LOOM("Loom", "Processes wool.", 3, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.FIBER), 30), new Slot(new Misc(MiscType.WOOD), 60)},
            MiscType.LOOM),
    MAYONNAISE_MACHINE("Mayonnaise Machine", "Produces mayonnaise from eggs.", 0, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.COPPER_BAR), 1), new Slot(new Misc(MiscType.WOOD), 15), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.STONE), 15)},
            MiscType.MAYONNAISE_MACHINE),
    OIL_MAKER("Oil Maker", "Produces oil from truffles.", 3, 0, 0, 0, 0,
            new Slot[]{new Slot(new Misc(MiscType.IRON_BAR), 1), new Slot(new Misc(MiscType.GOLD_BAR), 1), new Slot(new Misc(MiscType.WOOD), 100)},
            MiscType.OIL_MAKER),
    PRESERVES_JAR("Preserves Jar", "Produces jam from fruits and vegetables.", 2, 0, 0, 0, 0,
            new Slot[]{new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 8), new Slot(new Misc(MiscType.WOOD), 50), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.STONE), 40)},
            MiscType.PRESERVES_JAR),
    DEHYDRATOR("Dehydrator", "Dries fruits and mushrooms.", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0,
            new Slot[]{new Slot(new Misc(MiscType.FIBER), 30), new Slot(new Misc(MiscType.WOOD), 30), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.STONE), 20)},
            MiscType.DEHYDRATOR),
    FISH_SMOKER("Fish Smoker", "Smokes fish while preserving their quality.", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0,
            new Slot[]{new Slot(new Misc(MiscType.IRON_BAR), 3), new Slot(new Misc(MiscType.WOOD), 50), new Slot(new ForagingMineral(Quality.DEFAULT, ForagingMineralsType.COAL), 10)},
            MiscType.FISH_SMOKER),
    MYSTIC_TREE_SEED("Mystic tree seed", "Can be planted to grow into a mystic tree.", 0, 0, 4, 0, 100,
            new Slot[]{new Slot(new TreeSeed(TreeSeedsType.ACORNS), 5), new Slot(new TreeSeed(TreeSeedsType.MAPLE_SEEDS), 5), new Slot(new TreeSeed(TreeSeedsType.PINE_CONES), 5), new Slot(new TreeSeed(TreeSeedsType.MAHOGANY_SEEDS), 5)},
            TreeSeedsType.MYSTIC_TREE_SEED);

    public final String name;
    public final String description;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int sellingPrice;
    public final int fishingLevel;
    public final Slot[] ingredients;
    public final ItemType resultItemType;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append(": ");
        stringBuilder.append(description).append("\n");
        stringBuilder.append("Farming level: ").append(farmingLevel).append("\n");
        stringBuilder.append("Mining level: ").append(miningLevel).append("\n");
        stringBuilder.append("Foraging level: ").append(foragingLevel).append("\n");
        stringBuilder.append("Fishing level: ").append(fishingLevel).append("\n");
        stringBuilder.append("Selling price: ").append(sellingPrice).append("\n");
        stringBuilder.append(Arrays.toString(ingredients)).append("\n");
        return stringBuilder.toString();
    }

    CraftingRecipes(String name, String description, int farmingLevel, int miningLevel, int foragingLevel
            , int fishingLevel, int sellingPrice, Slot[] ingredients, ItemType resultItemType) {
        this.name = name;
        this.description = description;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.sellingPrice = sellingPrice;
        this.fishingLevel = fishingLevel;
        this.ingredients = ingredients;
        this.resultItemType = resultItemType;
    }

    public static CraftingRecipes findByName(String name) {
        for (CraftingRecipes cr : CraftingRecipes.values()) {
            if (cr.name.compareToIgnoreCase(name) == 0) {
                return cr;
            }
        }
        return null;
    }
}
