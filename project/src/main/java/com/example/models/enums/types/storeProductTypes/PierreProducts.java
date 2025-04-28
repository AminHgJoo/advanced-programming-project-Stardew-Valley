package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.worldEnums.Season;

public enum PierreProducts implements StoreProductInterface {
    // General Items
    RICE("Rice", "A basic grain often served under vegetables.", null, 200, 200, Double.POSITIVE_INFINITY, Season.values()),
    WHEAT_FLOUR("Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.", null, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    BOUQUET("Bouquet", "A gift that shows your romantic interest.\n(Unlocked after reaching level 2 friendship with a player)", null, 1000, 1000, 2, Season.values()),
    WEDDING_RING("Wedding Ring", "It's used to ask for another farmer's hand in marriage.\n(Unlocked after reaching level 3 friendship with a player)", null, 10000, 10000, 2, Season.values()),
    SUGAR("Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.", null, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    OIL("Oil", "All purpose cooking oil.", null, 200, 200, Double.POSITIVE_INFINITY, Season.values()),
    VINEGAR("Vinegar", "An aged fermented liquid used in many cooking recipes.", null, 200, 200, Double.POSITIVE_INFINITY, Season.values()),
    DELUXE_RETAINING_SOIL("Deluxe Retaining Soil", "This soil has a 100% chance of staying watered overnight. Mix into tilled soil.", null, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    GRASS_STARTER("Grass Starter", "Place this on your farm to start a new patch of grass.", null, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    SPEED_GRO("Speed-Gro", "Makes the plants grow 1 day earlier.", null, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    APPLE_SAPLING("Apple Sapling", "Takes 28 days to produce a mature Apple tree. Bears fruit in the fall. Only grows if the 8 surrounding \"tiles\" are empty.", null, 4000, 4000, Double.POSITIVE_INFINITY, Season.values()),
    APRICOT_SAPLING("Apricot Sapling", "Takes 28 days to produce a mature Apricot tree. Bears fruit in the spring. Only grows if the 8 surrounding \"tiles\" are empty.", null, 2000, 2000, Double.POSITIVE_INFINITY, Season.values()),
    CHERRY_SAPLING("Cherry Sapling", "Takes 28 days to produce a mature Cherry tree. Bears fruit in the spring. Only grows if the 8 surrounding \"tiles\" are empty.", null, 3400, 3400, Double.POSITIVE_INFINITY, Season.values()),
    ORANGE_SAPLING("Orange Sapling", "Takes 28 days to produce a mature Orange tree. Bears fruit in the summer. Only grows if the 8 surrounding \"tiles\" are empty.", null, 4000, 4000, Double.POSITIVE_INFINITY, Season.values()),
    PEACH_SAPLING("Peach Sapling", "Takes 28 days to produce a mature Peach tree. Bears fruit in the summer. Only grows if the 8 surrounding \"tiles\" are empty.", null, 6000, 6000, Double.POSITIVE_INFINITY, Season.values()),
    POMEGRANATE_SAPLING("Pomegranate Sapling", "Takes 28 days to produce a mature Pomegranate tree. Bears fruit in the fall. Only grows if the 8 surrounding \"tiles\" are empty.", null, 6000, 6000, Double.POSITIVE_INFINITY, Season.values()),
    BASIC_RETAINING_SOIL("Basic Retaining Soil", "This soil has a chance of staying watered overnight. Mix into tilled soil.", null, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    QUALITY_RETAINING_SOIL("Quality Retaining Soil", "This soil has a good chance of staying watered overnight. Mix into tilled soil.", null, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    PARSNIP_SEEDS("Parsnip Seeds", "Plant these in the spring. Takes 4 days to mature.", null, 20, 30, 5, new Season[]{Season.SPRING}),
    BEAN_STARTER("Bean Starter", "Plant these in the spring. Takes 10 days to mature, but keeps producing after that. Grows on a trellis.", null, 60, 90, 5, new Season[]{Season.SPRING}),
    CAULIFLOWER_SEEDS("Cauliflower Seeds", "Plant these in the spring. Takes 12 days to produce a large cauliflower.", null, 80, 120, 5, new Season[]{Season.SPRING}),
    POTATO_SEEDS("Potato Seeds", "Plant these in the spring. Takes 6 days to mature, and has a chance of yielding multiple potatoes at harvest.", null, 50, 75, 5, new Season[]{Season.SPRING}),
    TULIP_BULB("Tulip Bulb", "Plant in spring. Takes 6 days to produce a colorful flower. Assorted colors.", null, 20, 30, 5, new Season[]{Season.SPRING}),
    KALE_SEEDS("Kale Seeds", "Plant these in the spring. Takes 6 days to mature. Harvest with the scythe.", null, 70, 105, 5, new Season[]{Season.SPRING}),
    JAZZ_SEEDS("Jazz Seeds", "Plant in spring. Takes 7 days to produce a blue puffball flower.", null, 30, 45, 5, new Season[]{Season.SPRING}),
    GARLIC_SEEDS("Garlic Seeds", "Plant these in the spring. Takes 4 days to mature.", null, 40, 60, 5, new Season[]{Season.SPRING}),
    RICE_SHOOT("Rice Shoot", "Plant these in the spring. Takes 8 days to mature. Grows faster if planted near a body of water.\nHarvest with the scythe.", null, 40, 60, 5, new Season[]{Season.SPRING}),
    MELON_SEEDS("Melon Seeds", "Plant these in the summer. Takes 12 days to mature.", null, 80, 120, 5, new Season[]{Season.SUMMER}),
    TOMATO_SEEDS("Tomato Seeds", "Plant these in the summer. Takes 11 days to mature, and continues to produce after first harvest.", null, 50, 75, 5, new Season[]{Season.SUMMER}),
    BLUEBERRY_SEEDS("Blueberry Seeds", "Plant these in the summer. Takes 13 days to mature, and continues to produce after first harvest.", null, 80, 120, 5, new Season[]{Season.SUMMER}),
    PEPPER_SEEDS("Pepper Seeds", "Plant these in the summer. Takes 5 days to mature, and continues to produce after first harvest.", null, 40, 60, 5, new Season[]{Season.SUMMER}),
    WHEAT_SEEDS("Wheat Seeds", "Plant these in the summer or fall. Takes 4 days to mature. Harvest with the scythe.", null, 10, 15, 5, new Season[]{Season.SUMMER , Season.FALL}),
    RADISH_SEEDS("Radish Seeds", "Plant these in the summer. Takes 6 days to mature.", null, 40, 60, 5, new Season[]{Season.SUMMER}),
    POPPY_SEEDS("Poppy Seeds", "Plant in summer. Produces a bright red flower in 7 days.", null, 100, 150, 5, new Season[]{Season.SUMMER}),
    SPANGLE_SEEDS("Spangle Seeds", "Plant in summer. Takes 8 days to produce a vibrant tropical flower. Assorted colors.", null, 50, 75, 5, new Season[]{Season.SUMMER}),
    HOPS_STARTER("Hops Starter", "Plant these in the summer. Takes 11 days to grow, but keeps producing after that. Grows on a trellis.", null, 60, 90, 5, new Season[]{Season.SUMMER}),
    CORN_SEEDS("Corn Seeds", "Plant these in the summer or fall. Takes 14 days to mature, and continues to produce after first harvest.", null, 150, 225, 5, new Season[]{Season.SUMMER , Season.FALL}),
    SUNFLOWER_SEEDS("Sunflower Seeds", "Plant in summer or fall. Takes 8 days to produce a large sunflower. Yields more seeds at harvest.", null, 200, 300, 5, new Season[]{Season.SUMMER , Season.FALL}),
    RED_CABBAGE_SEEDS("Red Cabbage Seeds", "Plant these in the summer. Takes 9 days to mature.", null, 100, 150, 5, new Season[]{Season.SUMMER}),
    EGGPLANT_SEEDS("Eggplant Seeds", "Plant these in the fall. Takes 5 days to mature, and continues to produce after first harvest.", null, 20, 30, 5, new Season[]{Season.FALL}),
    PUMPKIN_SEEDS("Pumpkin Seeds", "Plant these in the fall. Takes 13 days to mature.", null, 100, 150, 5, new Season[]{Season.FALL}),
    BOK_CHOY_SEEDS("Bok Choy Seeds", "Plant these in the fall. Takes 4 days to mature.", null, 50, 75, 5, new Season[]{Season.FALL}),
    YAM_SEEDS("Yam Seeds", "Plant these in the fall. Takes 10 days to mature.", null, 60, 90, 5, new Season[]{Season.FALL}),
    CRANBERRY_SEEDS("Cranberry Seeds", "Plant these in the fall. Takes 7 days to mature, and continues to produce after first harvest.", null, 240, 360, 5, new Season[]{Season.FALL}),
    FAIRY_SEEDS("Fairy Seeds", "Plant in fall. Takes 12 days to produce a mysterious flower. Assorted Colors.", null, 200, 300, 5, new Season[]{Season.FALL}),
    AMARANTH_SEEDS("Amaranth Seeds", "Plant these in the fall. Takes 7 days to grow. Harvest with the scythe.", null, 70, 105, 5, new Season[]{Season.FALL}),
    GRAPE_STARTER("Grape Starter", "Plant these in the fall. Takes 10 days to grow, but keeps producing after that. Grows on a trellis.", null, 60, 90, 5, new Season[]{Season.FALL}),
    ARTICHOKE_SEEDS("Artichoke Seeds", "Plant these in the fall. Takes 8 days to mature.", null, 30, 45, 5, new Season[]{Season.FALL});

    private final String name;
    private final String description;
    private final ItemType itemType;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private Season[] seasons;
    PierreProducts(String name,String description, ItemType itemType,int price, double outOfSeasonPrice, double dailyLimit , Season[] seasons) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.seasons = seasons;
        this.itemType = itemType;
    }
    public String getDescription() {
        return description;
    }
    public int getPrice() {
        return price;
    }
    public double getOutOfSeasonPrice() {
        return outOfSeasonPrice;
    }
    public double getDailyLimit() {
        return dailyLimit;
    }
    public Season[] getSeasons() {
        return seasons;
    }

    public String getName() {
        return name;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
