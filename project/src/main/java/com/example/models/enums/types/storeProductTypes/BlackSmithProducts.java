package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.Quality;
import com.example.models.enums.types.inventoryEnums.TrashcanType;
import com.example.models.enums.types.itemTypes.ForagingMineralsType;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.worldEnums.Season;

public enum BlackSmithProducts implements StoreProductInterface {
    COPPER_ORE("Copper Ore", "A common ore that can be smelted into bars.", ForagingMineralsType.COPPER_ORE, 75, 75, Double.POSITIVE_INFINITY, Season.values()),
    IRON_ORE("Iron Ore", "A fairly common ore that can be smelted into bars.", ForagingMineralsType.IRON_ORE, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    COAL("Coal", "A combustible rock that is useful for crafting and smelting.", ForagingMineralsType.COAL, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    GOLD_ORE("Gold Ore", "A precious ore that can be smelted into bars.", ForagingMineralsType.GOLD_ORE, 400, 400, Double.POSITIVE_INFINITY, Season.values()),
    COPPER_TOOL("Copper Tool", MiscType.COPPER_BAR, 2000, 2000, 1, Season.values()),
    STEEL_TOOL("Steel Tool", MiscType.IRON_BAR, 5000, 5000, 1, Season.values()),
    GOLD_TOOL("Gold Tool", MiscType.GOLD_BAR, 10000, 10000, 1, Season.values()),
    IRIDIUM_TOOL("Iridium Tool", MiscType.IRIDIUM_BAR, 25000, 25000, 1, Season.values()),
    COPPER_TRASH_CAN("Copper Trash Can", MiscType.COPPER_BAR, 1000, 1000, 1, Season.values()),
    STEEL_TRASH_CAN("Steel Trash Can", MiscType.COPPER_BAR, 2500, 2500, 1, Season.values()),
    GOLD_TRASH_CAN("Gold Trash Can", MiscType.COPPER_BAR, 5000, 5000, 1, Season.values()),
    IRIDIUM_TRASH_CAN("Iridium Trash Can", MiscType.COPPER_BAR, 12500, 12500, 1, Season.values()),
    ;

    private final String name;
    private String description = "";
    private final ItemType itemType;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private ItemType ingredient = null;
    private Season[] seasons;

    BlackSmithProducts(String name, String description, ItemType itemType, int price, double outOfSeasonPrice, double dailyLimit, Season[] seasons) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.seasons = seasons;
        this.itemType = itemType;
    }

    BlackSmithProducts(String name, ItemType ingredient, int price, double outOfSeasonPrice, double dailyLimit, Season[] seasons) {
        this.name = name;
        this.ingredient = ingredient;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.seasons = seasons;
        this.itemType = null;
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

    public ItemType getIngredient() {
        return ingredient;
    }

    public void setIngredient(ItemType ingredient) {
        this.ingredient = ingredient;
    }

    public static BlackSmithProducts findTrashCanUpgrade(String name) {
        if (COPPER_TRASH_CAN.name.compareToIgnoreCase(name) == 0) {
            return COPPER_TRASH_CAN;
        } else if (STEEL_TRASH_CAN.name.compareToIgnoreCase(name) == 0) {
            return STEEL_TRASH_CAN;
        } else if (GOLD_TRASH_CAN.name.compareToIgnoreCase(name) == 0) {
            return GOLD_TRASH_CAN;
        } else if (IRIDIUM_TRASH_CAN.name.compareToIgnoreCase(name) == 0) {
            return IRIDIUM_TRASH_CAN;
        }
        return null;
    }

    public static BlackSmithProducts findSteelToolUpgrade(String name) {
        if (COPPER_TOOL.name.compareToIgnoreCase(name) == 0) {
            return COPPER_TOOL;
        }
        if (STEEL_TOOL.name.compareToIgnoreCase(name) == 0) {
            return STEEL_TOOL;
        }
        if (GOLD_TOOL.name.compareToIgnoreCase(name) == 0) {
            return GOLD_TOOL;
        }
        if (IRIDIUM_TOOL.name.compareToIgnoreCase(name) == 0) {
            return IRIDIUM_TOOL;
        }
        return null;
    }

    public TrashcanType getTrashcan() {
        if (name.equals(COPPER_TRASH_CAN.name)) {
            return TrashcanType.COPPER;
        } else if (name.equals((STEEL_TRASH_CAN.name))) {
            return TrashcanType.IRON;
        } else if (name.equals(GOLD_TRASH_CAN.name)) {
            return TrashcanType.GOLD;
        } else if (name.equals(IRIDIUM_TRASH_CAN.name)) {
            return TrashcanType.IRON;
        }
        return null;
    }

    public Quality getTool() {
        if (name.equals(COPPER_TOOL.name)) {
            return Quality.COPPER;
        } else if (name.equals(STEEL_TOOL.name)) {
            return Quality.SILVER;
        } else if (name.equals(GOLD_TOOL.name)) {
            return Quality.GOLD;
        } else if (name.equals(IRIDIUM_TOOL.name)) {
            return Quality.IRIDIUM;
        }
        return null;
    }
}
