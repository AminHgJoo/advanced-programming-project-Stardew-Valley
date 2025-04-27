package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.Quality;
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
        String itemName = request.body.get("itemName");
        CookingRecipes targetRecipe = null;

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();

        for (CookingRecipes recipes : player.getUnlockedCookingRecipes()) {
            if (recipes.name.compareToIgnoreCase(itemName) == 0) {
                targetRecipe = recipes;
                break;
            }
        }

        if (targetRecipe == null) {
            return new Response(false, "No recipe found for: " + itemName);
        }

        Backpack backpack = player.getInventory();

        if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
            return new Response(false, "Your inventory is full.");
        }

        if (player.getUsedEnergyInTurn() + 3 > 50) {
            return new Response(false, "You will exceed your max energy usage limit!");
        }

        if (player.getEnergy() - 3 < 0) {
            return new Response(false, "You don't have enough energy to craft an item.");
        }

        player.setEnergy(player.getEnergy() - 3);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + 3);

        for (Slot ingredient : targetRecipe.ingredients) {
            Slot backpackSlot = backpack.getSlotByItemName(ingredient.getItem().getName());
            Slot fridgeSlot = player.getRefrigeratorSlotByName(ingredient.getItem().getName());

            int cumulativeCount = 0;

            if (fridgeSlot != null) {
                cumulativeCount += fridgeSlot.getCount();
            }

            if (backpackSlot != null) {
                cumulativeCount += backpackSlot.getCount();
            }

            if (cumulativeCount < ingredient.getCount()) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have the ingredients to cook this recipe.");
            }
        }

        //one fish
        if (targetRecipe == CookingRecipes.MAKI_ROLL) {
            Slot fishSlot = backpack.getFirstFish();

            if (fishSlot == null) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have the fish to cook this recipe.");
            }

            fishSlot.setCount(fishSlot.getCount() - 1);
            if (fishSlot.getCount() == 0) {
                backpack.getSlots().remove(fishSlot);
            }
        }

        //deduct ingredients.
        for (Slot ingredient : targetRecipe.ingredients) {
            Slot backpackSlot = backpack.getSlotByItemName(ingredient.getItem().getName());
            Slot fridgeSlot = player.getRefrigeratorSlotByName(ingredient.getItem().getName());

            int countToReduce = ingredient.getCount();

            //prioritize the fridge count.

            if (fridgeSlot != null) {
                int howMuchInFridge = fridgeSlot.getCount();

                if (howMuchInFridge > countToReduce) {
                    fridgeSlot.setCount(howMuchInFridge - countToReduce);
                    countToReduce = 0;
                } else {
                    countToReduce -= howMuchInFridge;
                    player.getRefrigeratorSlots().remove(fridgeSlot);
                }
            }

            if (countToReduce > 0) {
                backpackSlot.setCount(backpackSlot.getCount() - countToReduce);
                if (backpackSlot.getCount() == 0) {
                    backpack.getSlots().remove(backpackSlot);
                }
                countToReduce = 0;
            }
        }

        Slot cookedItemSlot = new Slot(new Food(Quality.DEFAULT, targetRecipe.craftingResultType), 1);

        Slot destinationSlot = backpack.getSlotByItemName(cookedItemSlot.getItem().getName());

        if (destinationSlot == null) {
            backpack.getSlots().add(cookedItemSlot);
        } else {
            destinationSlot.setCount(destinationSlot.getCount() + cookedItemSlot.getCount());
        }

        GameRepository.saveGame(game);
        return new Response(true, "You cooked " + itemName);
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
