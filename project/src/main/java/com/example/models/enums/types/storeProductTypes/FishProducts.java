package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.worldEnums.Season;

public enum FishProducts implements StoreProduct {
    FISH_SMOKER_RECIPE("Fish Smoker (Recipe)", "A recipe to make Fish Smoker", null, 10000, 10000, 1, null, Season.values()),
    TROUT_SOUP("Trout Soup", "Pretty salty.", null, 250, 250, 1, null, Season.values()),
    BAMBOO_POLE("Bamboo Pole", "Use in the water to catch fish.", null, 500, 500, 1, null, Season.values()),
    TRAINING_ROD("Training Rod", "It's a lot easier to use than other rods, but can only catch basic fish.", null, 25, 25, 1, null, Season.values()),
    FIBERGLASS_ROD("Fiberglass Rod", "Use in the water to catch fish.", null, 1800, 1800, 1, 2, Season.values()),
    IRIDIUM_ROD("Iridium Rod", "Use in the water to catch fish.", null, 7500, 7500, 1, 4, Season.values());

    private final String name;
    private final String description;
    private final ItemType itemType;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private final Integer fishingSkill;
    private Season[] seasons;

    FishProducts(String name, String description, ItemType itemType, int price, double outOfSeasonPrice, double dailyLimit, Integer fishingSkill, Season[] seasons) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.fishingSkill = fishingSkill;
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

    public Integer getFishingSkill() {
        return fishingSkill;
    }
}
