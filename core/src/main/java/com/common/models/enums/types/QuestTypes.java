package com.common.models.enums.types;

import com.client.ClientApp;
import com.common.models.Backpack;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.types.itemTypes.MiscType;

public enum QuestTypes {
    PARSNIP_FOR_HARVEY("Harvey", "I need a dozen parsnips for my patients.", FoodTypes.PARSNIP, 10, 1000),
    WOOD_FOR_ROBIN("Robin", "Could anyone fetch me some wood for my next project?", MiscType.WOOD, 200, 1000),
    AMETHYST_FOR_ABIGAIL("Abigail", "I'd like to study some amethyst, anyone brave enough to fetch me one?", ForagingMineralsType.AMETHYST, 1, 1000),
    STONE_FOR_ROBIN("Robin", "Need a lot of stone for a fireplace.", ForagingMineralsType.STONE, 100, 1000);

    public final String vendor;
    public final String description;
    public final ItemType requestedItem;
    public final int quantity;
    public final int rewardDollars;

    QuestTypes(String vendor, String description, ItemType requestedItem, int quantity, int rewardDollars) {
        this.description = description;
        this.vendor = vendor;
        this.requestedItem = requestedItem;
        this.quantity = quantity;
        this.rewardDollars = rewardDollars;
    }

    //TODO
    public boolean attemptQuestFinish() {
        Player player = ClientApp.currentPlayer;
        Backpack backpack = player.getInventory();
        Slot slot = backpack.getSlotByItemName(requestedItem.getName());

        if (slot == null) {
            return false;
        }

        return slot.getCount() >= quantity;
    }

    @Override
    public String toString() {
        return vendor + ": " + description + "\nRequested Item: " + requestedItem.getName() + "\nQuantity: " + quantity ;
    }
}
