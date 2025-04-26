package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.recipes.CookingRecipes;
import com.example.models.enums.types.MenuTypes;
import com.example.models.items.Food;

public class Cooking extends Controller {

    public static Response handlePickFromRefrigerator(Request request) {
        String item = request.body.get("item");

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Backpack backpack = player.getInventory();

        Slot fridgeSlot = player.getRefrigeratorSlotByName(item);

        if (fridgeSlot == null) {
            return new Response(false, "You don't have this item in your refrigerator.");
        }

        if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
            return new Response(false, "Your inventory is full.");
        }

        Slot backpackSlot = backpack.getSlotByItemName(item);

        if (backpackSlot == null) {
            backpack.getSlots().add(fridgeSlot);
            player.getRefrigeratorSlots().remove(fridgeSlot);
        } else {
            player.getRefrigeratorSlots().remove(fridgeSlot);
            backpackSlot.setCount(Math.min(backpackSlot.getCount() + fridgeSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
        }

        GameRepository.saveGame(game);
        return new Response(true, "Food has been added to your backpack.");
    }

    public static Response handlePutIntoRefrigerator(Request request) {
        String item = request.body.get("item");

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Backpack backpack = player.getInventory();

        Slot backpackSlot = backpack.getSlotByItemName(item);

        if (backpackSlot == null) {
            return new Response(false, "You don't have this item in your backpack.");
        }

        if (!(backpackSlot.getItem() instanceof Food) || backpackSlot.getItem().getEnergyCost() >= 0) {
            return new Response(false, "You can't put anything other than edible food in the fridge.");
        }

        Slot fridgeSlot = player.getRefrigeratorSlotByName(item);

        backpack.getSlots().remove(backpackSlot);

        if (fridgeSlot == null) {
            player.getRefrigeratorSlots().add(backpackSlot);
        } else {
            fridgeSlot.setCount(Math.min(fridgeSlot.getCount() + backpackSlot.getCount(), fridgeSlot.getItem().getMaxStackSize()));
        }

        GameRepository.saveGame(game);
        return new Response(true, "Food has been placed in the refrigerator.");
    }

    public static Response handleCookingFood(Request request) {

    }

    public static Response handleShowCookingRecipes(Request request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cooking recipes : \n");

        Game game = App.getLoggedInUser().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();

        for (CookingRecipes recipe : currentPlayer.getUnlockedCookingRecipes()) {
            stringBuilder.append(recipe.toString()).append("\n");
        }

        return new Response(true, stringBuilder.toString());
    }

    public static Response handleEnterPlayerHome(Request request) {
        App.setCurrMenuType(MenuTypes.PlayerHomeMenu);
        return new Response(true, "Entering home menu.");
    }

    public static Response handleExitPlayerHome(Request request) {
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "Exiting home menu.");
    }
}
