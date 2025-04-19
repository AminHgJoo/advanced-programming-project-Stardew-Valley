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
        for(Slot slot : backpack.getSlots()) {
            output.append(slot.toString()).append("\n");
        }
        if(output.isEmpty())
            return new Response(true, "Your Backpack is empty!");
        return new Response(true, output.toString());

    }

    public static Response handleInventoryTrashing(Request request) {
        return null;

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
