package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.enums.types.itemTypes.ForagingMineralsType;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.worldEnums.Season;
import com.example.models.items.Food;

public enum StardropProducts implements StoreProductInterface {
    BEER("Beer", "Drink in moderation.", FoodTypes.BEER, 400, 400, Double.POSITIVE_INFINITY, Season.values()),
    SALAD("Salad", "A healthy garden salad.", FoodTypes.SALAD, 220, 220, Double.POSITIVE_INFINITY, Season.values()),
    BREAD("Bread", "A crusty baguette.", FoodTypes.BREAD, 120, 120, Double.POSITIVE_INFINITY, Season.values()),
    SPAGHETTI("Spaghetti", "An old favorite.", FoodTypes.SPAGHETTI, 240, 240, Double.POSITIVE_INFINITY, Season.values()),
    PIZZA("Pizza", "It's popular for all the right reasons.", FoodTypes.PIZZA, 600, 600, Double.POSITIVE_INFINITY, Season.values()),
    COFFEE("Coffee", "It smells delicious. This is sure to give you a boost.", FoodTypes.COFFEE, 300, 300, Double.POSITIVE_INFINITY, Season.values()),
    WOOD("Wood", "A sturdy, yet flexible plant material with a wide variety of uses.", MiscType.WOOD, 10, 10, Double.POSITIVE_INFINITY, Season.values()),
    STONE("Stone", "A common material with many uses in crafting and building.", ForagingMineralsType.STONE, 20, 20, Double.POSITIVE_INFINITY, Season.values());

    private final String name;
    private final String description;
    private final ItemType itemType;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private Season[] seasons;

    StardropProducts(String name, String description, ItemType itemType, int price, double outOfSeasonPrice, double dailyLimit, Season[] seasons) {
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
