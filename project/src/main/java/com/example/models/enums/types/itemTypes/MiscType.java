package com.example.models.enums.types.itemTypes;

import com.example.models.Slot;
import com.example.models.items.Misc;

public enum MiscType implements ItemType {
    WOOD("Wood", 2),
    FIBER("Fiber", 1),
    GRASS_STARTER("Grass Starter", 50),
    BASIC_FERTILIZER("Basic Fertilizer", 2),
    QUALITY_FERTILIZER("Quality Fertilizer", 10),
    SPEED_GRO("Speed Gro", 20),
    DELUXE_SPEED_GRO("Deluxe Speed Gro", 40),
    EGG("Egg", 50),
    BIG_EGG("Big Egg", 50),
    DUCK_EGG("Duck Egg", 95),
    DUCK_FEATHER("Duck Feather", 250),
    WOOL("Wool", 340),
    RABBITS_FOOT("Rabbit's Foot", 565),
    DINOSAUR("Dinosaur", 350),
    MILK("Milk", 125),
    BIG_MILK("Big Milk", 190),
    GOAT_MILK("Goat Milk", 225),
    BIG_GOAT_MILK("Big Goat Milk", 345),
    TRUFFLE("Truffle", 625),
    HAY("Hay", 0),
    COPPER_BAR("Copper Bar", 60),
    IRON_BAR("Iron Bar", 120),
    GOLD_BAR("Gold Bar", 250),
    IRIDIUM_BAR("Iridium Bar", 1000),
    BOUQUET("Bouquet", 100),
    WEDDING_RING("Wedding Ring", 1000),
    BASIC_RETAINING_SOIL("Basic Retaining Soil", 4),
    QUALITY_RETAINING_SOIL("Quality Retaining Soil", 5),
    CLOTH("Cloth", 100),
    CHERRY_BOMB("Cherry Bomb", 50),
    BOMB("Bomb", 50),
    MEGA_BOMB("Mega Bomb", 50),
    SPRINKLER("Sprinkler", 0),
    QUALITY_SPRINKLER("Quality Sprinkler", 0),
    IRIDIUM_SPRINKLER("Iridium Sprinkler", 0),
    CHARCOAL_KLIN("Charcoal Klin", 0),
    FURNACE("Furnace", 0),
    SCARE_CROW("Scare Crow", 0),
    DELUXE_SCARE_CROW("Deluxe Scarecrow", 0),
    BEE_HOUSE("Bee House", 0),
    CHEESE_PRESS("Cheese Press", 0),
    KEG("Keg", 0),
    LOOM("Loom", 0),
    MAYONNAISE_MACHINE("Mayonnaise Machine", 0),
    OIL_MAKER("Oil Maker", 0),
    PRESERVES_JAR("Preserves Jar", 0),
    DEHYDRATOR("Dehydrator", 0),
    FISH_SMOKER("Fish Smoker", 0),
    ;

    final public String name;
    final public int value;

    MiscType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Slot createAmountOfItem(int amount) {
        return new Slot(new Misc(this), amount);
    }
}
