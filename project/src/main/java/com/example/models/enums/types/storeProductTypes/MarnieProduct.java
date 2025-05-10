package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.ForagingMineralsType;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.types.itemTypes.ToolTypes;
import com.example.models.enums.worldEnums.Season;

public enum MarnieProduct implements StoreProductInterface {
    HAY("Hay", "Dried grass used as animal food.", MiscType.HAY, 50, 50, Double.POSITIVE_INFINITY, Season.values()),
    MILK_PAIL("Milk Pail", "Gather milk from your animals.", ToolTypes.MILK_PAIL, 1000, 1000, 1, Season.values()),
    SHEARS("Shears", "Use this to collect wool from sheep", ToolTypes.SHEAR, 1000, 1000, 1, Season.values()),
    CHICKEN("Chicken", "Well cared-for chickens lay eggs every day. Lives in the coop.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    COW("Cow", "Can be milked daily. A milk pail is required to harvest the milk. Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    GOAT("Goat", "Happy provide goat milk every other day. A milk pail is required to harvest the milk. Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    DUCK("Duck", "Happy lay duck eggs every other day. Lives in the coop.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    SHEEP("Sheep", "Can be shorn for wool. A pair of shears is required to harvest the wool. Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    RABBIT("Rabbit", "Provides a place for you to refill your watering can.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    DINOSAUR("Dinosaur", "The Dinosaur is a farm animal that lives in a Big Coop", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    PIG("Pig", "These pigs are trained to find truffles! Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    ;
    private final String name;
    private final String description;
    private final ItemType itemType;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private final Season[] seasons;

    MarnieProduct(String name, String description, ItemType itemType, int price, double outOfSeasonPrice, double dailyLimit, Season[] values) {
        this.name = name;
        this.description = description;
        this.itemType = itemType;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.seasons = values;
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
