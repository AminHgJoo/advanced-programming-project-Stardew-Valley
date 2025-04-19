package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;

public class InventoryFunctionalities extends Controller {
    public static Response handleShowInventory(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Backpack backpack = game.getCurrentPlayer().getInventory();
        StringBuilder output = new StringBuilder();
        for (Slot slot : backpack.getSlots()) {
            output.append(slot.toString()).append("\n");
        }
        if (output.isEmpty())
            return new Response(true, "Your Backpack is empty!");
        return new Response(true, output.toString());

    }

    public static Response handleInventoryTrashing(Request request) {
        String itemName = request.body.get("itemName");
        String number = request.body.get("number");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Backpack backpack = game.getCurrentPlayer().getInventory();
        Slot slot = backpack.getSlotByItemName(itemName);
        if (slot == null)
            return new Response(false, "item(s) does not exist!");
        if (number == null) {
            backpack.removeSlot(slot);
            return new Response(true, "Item successfully trashed!");
        }
        int numberInt = Integer.parseInt(number);
        slot.setCount(slot.getCount() - numberInt);
        if (slot.getCount() <= 0) {
            backpack.removeSlot(slot);
            return new Response(true, "Item successfully trashed!");
        }
        return new Response(true, numberInt + " of item(s) successfully trashed!");

    }

    public static Response handleToolEquip(Request request) {
        return null;

    }

    public static Response handleEquippedToolQuery(Request request) {
        return null;

    }

    public static Response handleShowTools(Request request) {
        return null;

    }

    public static Response handleToolUpgrade(Request request) {
        return null;

    }

    public static Response handleCraftInfoQuery(Request request) {
        return null;

    }

    public static Response handleShowCraftingRecipes(Request request) {
        return null;

    }

    public static Response handleItemCrafting(Request request) {
        return null;

    }

    public static Response handlePlaceItem(Request request) {
        return null;

    }

    public static Response handleAddItemCheat(Request request) {
        return null;

    }

    public static Response handleEating(Request request) {
        return null;

    }
}
