package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.worldEnums.Season;

public enum JojaMartProducts implements StoreProductInterface {
    JOJA_COLA("Joja Cola" , "The flagship product of Joja corporation.",null , 75,75,Double.POSITIVE_INFINITY,Season.values() ),
    ANCIENT_SEED("Ancient Seed" , "Could this still grow?" , null ,500,500,1,Season.values()),
    GRASS_STARTER("Grass Starter" , "Place this on your farm to start a new patch of grass." ,null, 125,125,Double.POSITIVE_INFINITY,Season.values()),
    SUGAR("Sugar" , "Adds sweetness to pastries and candies. Too much can be unhealthy.", null , 125,125,Double.POSITIVE_INFINITY,Season.values()),
    WHEAT_FLOUR("Wheat Flour" , "A common cooking ingredient made from crushed wheat seeds." , null , 125,125,Double.POSITIVE_INFINITY,Season.values()),
    RICE("Rice" , "A basic grain often served under vegetables." , null , 250,250,Double.POSITIVE_INFINITY,Season.values()),
    PARSNIP_SEEDS("Parsnip Seeds" , "Plant these in the spring. Takes 4 days to mature." , null , 25,Double.POSITIVE_INFINITY,5,new Season[]{Season.SPRING}),
    BEAN_STARTER("Bean Starter" , "Plant these in the spring. Takes 10 days to mature, but keeps producing after that. Grows on a trellis." , null , 75,Double.POSITIVE_INFINITY,5,new Season[]{Season.SPRING}),
    CAULIFLOWER_SEEDS("Cauliflower Seeds" , "Plant these in the spring. Takes 12 days to produce a large cauliflower.", null , 100 , Double.POSITIVE_INFINITY , 5,new Season[]{Season.SPRING} ),
    POTATO_SEEDS("Potato Seeds" , "Plant these in the spring. Takes 6 days to mature, and has a chance of yielding multiple potatoes at harvest." , null , 62 , Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING} ),
    STRAWBERRY_SEEDS("Strawberry Seeds","Plant these in spring. Takes 8 days to mature, and keeps producing strawberries after that." , null , 100 , Double.POSITIVE_INFINITY,5,new Season[]{Season.SPRING} ),
    TULIP_BULB("Tulip Bulb" , "Plant in spring. Takes 6 days to produce a colorful flower. Assorted colors." , null , 25,Double.POSITIVE_INFINITY,5,new Season[]{Season.SPRING} ),
    KALE_SEEDS("Kale Seeds" , "Plant these in the spring. Takes 6 days to mature. Harvest with the scythe.",null , 87, Double.POSITIVE_INFINITY,5,new Season[]{Season.SPRING} ),
    COFFEE_BEANS("Coffee Beans" ,"Plant in summer or spring. Takes 10 days to grow, Then produces coffee Beans every other day." , null , 200 , Double.POSITIVE_INFINITY , 1 , new Season[]{Season.SPRING , Season.SUMMER} ),
    CARROT_SEEDS("Carrot Seeds" , "Plant in the spring. Takes 3 days to grow." , null , 5 , Double.POSITIVE_INFINITY , 10, new Season[]{Season.SPRING} ),
    RHUBARB_SEEDS("Rhubarb Seeds" , "Plant these in the spring. Takes 13 days to mature." , null ,100 , Double.POSITIVE_INFINITY,5,new Season[]{Season.SPRING} ),
    JAZZ_SEEDS("Jazz Seeds" , "Plant in spring. Takes 7 days to produce a blue puffball flower." , null , 37 , Double.POSITIVE_INFINITY, 5 , new Season[]{Season.SPRING} ),
    TOMATO_SEEDS("Tomato Seeds", "Plant these in the summer. Takes 11 days to mature, and continues to produce after first harvest.", null, 62, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    PEPPER_SEEDS("Pepper Seeds", "Plant these in the summer. Takes 5 days to mature, and continues to produce after first harvest.", null, 50, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    WHEAT_SEEDS("Wheat Seeds", "Plant these in the summer or fall. Takes 4 days to mature. Harvest with the scythe.", null, 12, Double.POSITIVE_INFINITY, 10, new Season[]{Season.SUMMER , Season.FALL}),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", "Plant in the summer. Takes 6 days to grow, and continues to produce after first harvest.", null, 10, Double.POSITIVE_INFINITY, 10, new Season[]{Season.SUMMER}),
    RADISH_SEEDS("Radish Seeds", "Plant these in the summer. Takes 6 days to mature.", null, 50, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    MELON_SEEDS("Melon Seeds", "Plant these in the summer. Takes 12 days to mature.", null, 100, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    HOPS_STARTER("Hops Starter", "Plant these in the summer. Takes 11 days to grow, but keeps producing after that. Grows on a trellis.", null, 75, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    POPPY_SEEDS("Poppy Seeds", "Plant in summer. Produces a bright red flower in 7 days.", null, 125, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    SPANGLE_SEEDS("Spangle Seeds", "Plant in summer. Takes 8 days to produce a vibrant tropical flower. Assorted colors.", null, 62, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    STARFRUIT_SEEDS("Starfruit Seeds", "Plant these in the summer. Takes 13 days to mature.", null, 400, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    SUNFLOWER_SEEDS("Sunflower Seeds", "Plant in summer or fall. Takes 8 days to produce a large sunflower. Yields more seeds at harvest.", null, 125, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER, Season.FALL}),
    CORN_SEEDS("Corn Seeds", "Plant these in the summer or fall. Takes 14 days to mature, and continues to produce after first harvest.", null, 187, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    EGGPLANT_SEEDS("Eggplant Seeds", "Plant these in the fall. Takes 5 days to mature, and continues to produce after first harvest.", null, 25, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    PUMPKIN_SEEDS("Pumpkin Seeds", "Plant these in the fall. Takes 13 days to mature.", null, 125, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    BROCCOLI_SEEDS("Broccoli Seeds", "Plant in the fall. Takes 8 days to mature, and continues to produce after first harvest.", null, 15, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    AMARANTH_SEEDS("Amaranth Seeds", "Plant these in the fall. Takes 7 days to grow. Harvest with the scythe.", null, 87, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    GRAPE_STARTER("Grape Starter", "Plant these in the fall. Takes 10 days to grow, but keeps producing after that. Grows on a trellis.", null, 75, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    BEET_SEEDS("Beet Seeds", "Plant these in the fall. Takes 6 days to mature.", null, 20, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    YAM_SEEDS("Yam Seeds", "Plant these in the fall. Takes 10 days to mature.", null, 75, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    BOK_CHOY_SEEDS("Bok Choy Seeds", "Plant these in the fall. Takes 4 days to mature.", null, 62, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    CRANBERRY_SEEDS("Cranberry Seeds", "Plant these in the fall. Takes 7 days to mature, and continues to produce after first harvest.", null, 300, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    FAIRY_SEEDS("Fairy Seeds", "Plant in fall. Takes 12 days to produce a mysterious flower. Assorted Colors.", null, 250, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    RARE_SEED("Rare Seed", "Sow in fall. Takes all season to grow.", null, 1000, Double.POSITIVE_INFINITY, 1, new Season[]{Season.FALL}),
    POWDERMELON_SEEDS("Powdermelon Seeds","This special melon grows in the winter. Takes 7 days to grow.",null , 20,  Double.POSITIVE_INFINITY , 10 , new Season[]{Season.WINTER})
    ;
    private final String name;
    private final String description;
    private final ItemType itemType;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private Season[] seasons;
    JojaMartProducts(String name,String description, ItemType itemType,int price, double outOfSeasonPrice, double dailyLimit , Season[] seasons) {
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
