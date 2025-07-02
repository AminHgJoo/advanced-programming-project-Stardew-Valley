package com.common.models.enums.types.itemTypes;

import com.common.models.Slot;
import com.common.models.enums.Quality;
import com.common.models.items.Misc;

public enum MiscType implements ItemType {
    WOOD("Wood", 2, false, false),
    FIBER("Fiber", 1, false, false),
    GRASS_STARTER("Grass Starter", 50, false, false),
    BASIC_FERTILIZER("Basic Fertilizer", 2, false, false),
    QUALITY_FERTILIZER("Quality Fertilizer", 10, false, false),
    SPEED_GRO("Speed Gro", 20, false, false),
    DELUXE_SPEED_GRO("Deluxe Speed Gro", 40, false, false),
    EGG("Egg", 50, false, false),
    BIG_EGG("Big Egg", 50, false, false),
    DUCK_EGG("Duck Egg", 95, false, false),
    DUCK_FEATHER("Duck Feather", 250, false, false),
    WOOL("Wool", 340, false, false),
    RABBITS_FOOT("Rabbit's Foot", 565, false, false),
    DINOSAUR("Dinosaur", 350, false, false),
    MILK("Milk", 125, false, false),
    BIG_MILK("Big Milk", 190, false, false),
    GOAT_MILK("Goat Milk", 225, false, false),
    BIG_GOAT_MILK("Big Goat Milk", 345, false, false),
    TRUFFLE("Truffle", 625, false, false),
    HAY("Hay", 0, false, false),
    COPPER_BAR("Copper Bar", 60, false, false),
    IRON_BAR("Iron Bar", 120, false, false),
    GOLD_BAR("Gold Bar", 250, false, false),
    IRIDIUM_BAR("Iridium Bar", 1000, false, false),
    BOUQUET("Bouquet", 100, false, false),
    WEDDING_RING("Wedding Ring", 1000, false, false),
    BASIC_RETAINING_SOIL("Basic Retaining Soil", 4, false, false),
    QUALITY_RETAINING_SOIL("Quality Retaining Soil", 5, false, false),
    DELUXE_RETAINING_SOIL("Deluxe Retaining Soil", 5, false, false),
    CLOTH("Cloth", 100, false, false),
    CHERRY_BOMB("Cherry Bomb", 50, false, false),
    BOMB("Bomb", 50, false, false),
    MEGA_BOMB("Mega Bomb", 50, false, false),
    SPRINKLER("Sprinkler", 0, false, false),
    QUALITY_SPRINKLER("Quality Sprinkler", 0, false, false),
    IRIDIUM_SPRINKLER("Iridium Sprinkler", 0, false, false),
    CHARCOAL_KLIN("Charcoal Klin", 0, true, true),
    FURNACE("Furnace", 0, true, true),
    SCARE_CROW("Scare Crow", 0, false, true),
    DELUXE_SCARE_CROW("Deluxe Scarecrow", 0, false, true),
    BEE_HOUSE("Bee House", 0, true, true),
    CHEESE_PRESS("Cheese Press", 0, true, true),
    KEG("Keg", 0, true, true),
    LOOM("Loom", 0, true, true),
    MAYONNAISE_MACHINE("Mayonnaise Machine", 0, true, true),
    OIL_MAKER("Oil Maker", 0, true, true),
    PRESERVES_JAR("Preserves Jar", 0, true, true),
    DEHYDRATOR("Dehydrator", 0, true, true),
    FISH_SMOKER("Fish Smoker", 0, true, true),
    SHIPPING_BIN("Shipping Bin", 0, true, true),
    ;

    final public String name;
    final public int value;
    final public boolean isArtisanBlock;
    final public boolean isPlacable;

    MiscType(String name, int value, boolean isArtisanBlock, boolean isPlacable) {
        this.name = name;
        this.value = value;
        this.isArtisanBlock = isArtisanBlock;
        this.isPlacable = isPlacable;
    }

    public static MiscType getMiscTypeByName(String name) {
        MiscType[] values = MiscType.values();
        for (MiscType value : values) {
            if (value.name.compareToIgnoreCase(name) == 0) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public Slot createAmountOfItem(int amount, Quality quality) {
        return new Slot(new Misc(this), amount);
    }
}
