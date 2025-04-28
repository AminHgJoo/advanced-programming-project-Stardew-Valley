package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.worldEnums.Season;

public enum StardropProducts implements StoreProductInterface {
    BEER("Beer" , "Drink in moderation." , null , 400,400,Double.POSITIVE_INFINITY,Season.values()),
    SALAD("Salad" , "A healthy garden salad." , null , 220,220,Double.POSITIVE_INFINITY,Season.values()),
    BREAD("Bread","A crusty baguette.", null ,120,120,Double.POSITIVE_INFINITY,Season.values()),
    SPAGHETTI("Spaghetti" , "An old favorite." ,null , 240,240,Double.POSITIVE_INFINITY,Season.values()),
    PIZZA("Pizza" , "It's popular for all the right reasons." , null , 600,600,Double.POSITIVE_INFINITY,Season.values()),
    COFFEE("Coffee","It smells delicious. This is sure to give you a boost." , null , 300,300,Double.POSITIVE_INFINITY,Season.values()),
    WOOD("Wood" , "A sturdy, yet flexible plant material with a wide variety of uses." , null , 10,10,Double.POSITIVE_INFINITY,Season.values()),
    STONE("Stone", "A common material with many uses in crafting and building." , null , 20 ,20 ,Double.POSITIVE_INFINITY,Season.values());

    private final String name;
    private final String description;
    private final ItemType itemType;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private Season[] seasons;
    StardropProducts(String name,String description, ItemType itemType,int price, double outOfSeasonPrice, double dailyLimit , Season[] seasons) {
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
