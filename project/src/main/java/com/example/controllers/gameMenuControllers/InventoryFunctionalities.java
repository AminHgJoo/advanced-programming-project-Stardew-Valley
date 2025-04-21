package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.items.Item;
import com.example.models.items.Tool;
import org.jetbrains.annotations.NotNull;

public class InventoryFunctionalities extends Controller {
    public static Response handleShowInventory(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Backpack backpack = game.getCurrentPlayer().getInventory();
        String output = backpack.showInventory();
        if (output.isEmpty())
            return new Response(true, "Your Backpack is empty!");
        return new Response(true, output);
    }

    public static Response handleInventoryTrashing(Request request) {
        String itemName = request.body.get("itemName");
        String number = request.body.get("number");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Backpack backpack = game.getCurrentPlayer().getInventory();
        Slot slot = backpack.getSlotByItemName(itemName);
        if (slot == null)
            return new Response(false, "item(s) does not exist!");
        if (number == null)
            return removeSlotHandle(slot, player, backpack , game);
        int numberInt = Integer.parseInt(number);
        slot.setCount(slot.getCount() - numberInt);
        if (slot.getCount() <= 0)
            return removeSlotHandle(slot, player, backpack , game);
        return removeItemHandle(numberInt, slot, player , game);
    }

    private static @NotNull Response removeItemHandle(int numberInt, Slot slot, Player player , Game game) {
        int cashBack = (numberInt * slot.getItem().getValue() *
                player.getTrashcanRefundPercentage()) / 100;
        player.setMoney(player.getMoney() + cashBack);
        GameRepository.saveGame(game);
        return new Response(true, numberInt + " of item(s) successfully trashed!");
    }

    private static @NotNull Response removeSlotHandle(Slot slot, Player player, Backpack backpack , Game game) {
        int cashBack = (slot.getCount() * slot.getItem().getValue() *
                player.getTrashcanRefundPercentage()) / 100;
        player.setMoney(player.getMoney() + cashBack);
        backpack.removeSlot(slot);
        GameRepository.saveGame(game);
        return new Response(true, "Item successfully trashed!" + "profit: " + cashBack);
    }

    public static Response handleToolEquip(Request request) {
        String toolName = request.body.get("toolName");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Backpack backpack = game.getCurrentPlayer().getInventory();
        Slot slot = backpack.getSlotByItemName(toolName);
        if (slot == null)
            return new Response(false, "item(s) does not exist!");
        if (!(slot.getItem() instanceof Tool))
            return new Response(false, "item(s) is not a tool!");
        game.getCurrentPlayer().setEquippedItem(slot.getItem());
        GameRepository.saveGame(game);
        return new Response(true, "Equipped " + toolName + " successfully!");
    }

    public static Response handleEquippedToolQuery(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Item item = game.getCurrentPlayer().getEquippedItem();
        if (item == null)
            return new Response(false, "No equipped tool found!");
        return new Response(true, item.getName());

    }

    public static Response handleShowTools(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Backpack backpack = game.getCurrentPlayer().getInventory();
        String output = backpack.showTools();
        if (output.isEmpty())
            return new Response(true, "No tools found!");
        return new Response(true, output);

    }

    public static Response handleToolUpgrade(Request request) {
        // TODO BLACKSMITH
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
