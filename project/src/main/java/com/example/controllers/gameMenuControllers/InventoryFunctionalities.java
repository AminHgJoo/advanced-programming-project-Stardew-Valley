package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.recipes.CraftingRecipes;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.types.itemTypes.TreeSeedsType;
import com.example.models.items.*;
import com.example.models.items.buffs.ActiveBuff;
import com.example.models.mapModels.Cell;
import com.example.models.mapObjects.DroppedItem;
import com.example.models.mapObjects.EmptyCell;

import java.util.HashMap;

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
            return removeSlotHandle(slot, player, backpack, game);
        int numberInt = Integer.parseInt(number);
        slot.setCount(slot.getCount() - numberInt);
        if (slot.getCount() <= 0)
            return removeSlotHandle(slot, player, backpack, game);
        return removeItemHandle(numberInt, slot, player, game);
    }

    private static Response removeItemHandle(int numberInt, Slot slot, Player player, Game game) {
        int cashBack = (numberInt * slot.getItem().getValue() *
                player.getTrashcanRefundPercentage()) / 100;
        player.setMoney(player.getMoney() + cashBack);
        GameRepository.saveGame(game);
        return new Response(true, numberInt + " of item(s) successfully trashed!");
    }

    private static Response removeSlotHandle(Slot slot, Player player, Backpack backpack, Game game) {
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

    // TODO BLACKSMITH
    public static Response handleToolUpgrade(Request request) {
        return null;
    }

    //TODO: For crops, trees and all seeds/saplings.
    public static Response handleCraftInfoQuery(Request request) {
        return null;

    }

    public static Response handleShowCraftingRecipes(Request request) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Crafting Recipes:\n");

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();

        for (CraftingRecipes craftingRecipes : player.getUnlockedCraftingRecipes()) {
            stringBuilder.append(craftingRecipes.toString()).append("\n");
        }

        return new Response(true, stringBuilder.toString());
    }

    public static Response handleItemCrafting(Request request) {
        String itemName = request.body.get("itemName");
        CraftingRecipes targetRecipe = null;

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();

        for (CraftingRecipes craftingRecipes : player.getUnlockedCraftingRecipes()) {
            if (craftingRecipes.name.compareToIgnoreCase(itemName) == 0) {
                targetRecipe = craftingRecipes;
                break;
            }
        }

        if (targetRecipe == null) {
            return new Response(false, "No recipe found for: " + itemName);
        }

        Backpack backpack = player.getInventory();

        if (backpack.getSlots().size() == backpack.getType().getMaxCapacity()) {
            return new Response(false, "Your backpack is full, you cannot craft any items!");
        }

        if (player.getUsedEnergyInTurn() + 2 > 50) {
            return new Response(false, "You will exceed your max energy usage limit!");
        }

        if (player.getEnergy() - 2 < 0) {
            return new Response(false, "You don't have enough energy to craft an item.");
        }

        player.setEnergy(player.getEnergy() - 2);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + 2);

        //checks the player's inventory to see if it has enough materials.
        for (Slot ingredient : targetRecipe.ingredients) {
            Slot playerSlot = backpack.getSlotByItemName(ingredient.getItem().getName());

            if (playerSlot == null) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have the required materials to craft this item!");
            }

            if (playerSlot.getCount() < ingredient.getCount()) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have the required materials to craft this item!");
            }
        }

        //deducts the materials.
        for (Slot ingredient : targetRecipe.ingredients) {
            Slot playerSlot = backpack.getSlotByItemName(ingredient.getItem().getName());

            playerSlot.setCount(playerSlot.getCount() - ingredient.getCount());

            if (playerSlot.getCount() == 0) {
                backpack.getSlots().remove(playerSlot);
            }
        }

        Slot craftedItemSlot = null;

        if (targetRecipe.resultItemType == TreeSeedsType.MYSTIC_TREE_SEED) {
            craftedItemSlot = new Slot(new TreeSeed(TreeSeedsType.MYSTIC_TREE_SEED), 1);
        } else {
            craftedItemSlot = new Slot(new Misc((MiscType) targetRecipe.resultItemType), 1);
        }

        Slot destinationSlot = backpack.getSlotByItemName(craftedItemSlot.getItem().getName());

        if (destinationSlot == null) {
            backpack.getSlots().add(craftedItemSlot);
        } else {
            destinationSlot.setCount(destinationSlot.getCount() + craftedItemSlot.getCount());
        }

        GameRepository.saveGame(game);
        return new Response(true, "You crafted " + targetRecipe.name);
    }

    private static int[] getXAndYIncrement(String direction) {
        if (direction.compareToIgnoreCase("u") == 0) {
            return new int[]{0, -1};
        } else if (direction.compareToIgnoreCase("d") == 0) {
            return new int[]{0, 1};
        } else if (direction.compareToIgnoreCase("r") == 0) {
            return new int[]{1, 0};
        } else if (direction.compareToIgnoreCase("l") == 0) {
            return new int[]{-1, 0};
        } else if (direction.compareToIgnoreCase("ur") == 0) {
            return new int[]{1, -1};
        } else if (direction.compareToIgnoreCase("ul") == 0) {
            return new int[]{-1, -1};
        } else if (direction.compareToIgnoreCase("dr") == 0) {
            return new int[]{1, 1};
        } else if (direction.compareToIgnoreCase("dl") == 0) {
            return new int[]{-1, 1};
        } else {
            return null;
        }
    }

    public static Response handlePlaceItem(Request request) {
        String direction = request.body.get("direction");
        String itemName = request.body.get("itemName");

        int[] xAndY = getXAndYIncrement(direction);

        if (xAndY == null) {
            return new Response(false, "Invalid direction.");
        }

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Backpack backpack = player.getInventory();

        int dx = xAndY[0];
        int dy = xAndY[1];
        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();

        Slot slotToPlace = backpack.getSlotByItemName(itemName);

        if (slotToPlace == null) {
            return new Response(false, "Item not found.");
        }

        Cell targetCell = player.getFarm().findCellByCoordinate(playerX + dx, playerY + dy);

        if (targetCell == null) {
            return new Response(false, "Invalid placement location.");
        }

        if (!(targetCell.getObjectOnCell() instanceof EmptyCell)) {
            return new Response(false, "Target cell is not empty.");
        }

        targetCell.setObjectOnCell(new DroppedItem("gray", slotToPlace.getCount(), slotToPlace.getItem()));
        backpack.getSlots().remove(slotToPlace);

        GameRepository.saveGame(game);
        return new Response(true, "Item placed successfully on Coordinates : x = " + (playerX + dx) + ", y = " + (playerY + dy));
    }

    public static Response handleAddItemCheat(Request request) {
        int count = Integer.parseInt(request.body.get("count"));
        String itemName = request.body.get("itemName");

        HashMap<String, ItemType> allItemsList = App.getAllItemTypes();

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Backpack backpack = player.getInventory();

        if (!allItemsList.containsKey(itemName)) {
            return new Response(false, "Item doesn't exist.");
        }

        if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
            return new Response(false, "You don't have enough space to add an item.");
        }

        Slot slot = backpack.getSlotByItemName(itemName);
        Slot toBeAddedSlot = allItemsList.get(itemName).createAmountOfItem(count);

        if (slot == null) {
            backpack.getSlots().add(toBeAddedSlot);
        } else {
            slot.setCount(Math.min(slot.getCount() + count, slot.getItem().getMaxStackSize()));
        }

        GameRepository.saveGame(game);
        return new Response(true, "Item added successfully.");
    }

    public static Response handleEating(Request request) {
        String foodName = request.body.get("foodName");

        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Backpack backpack = player.getInventory();

        Slot slot = backpack.getSlotByItemName(foodName);

        if (slot == null) {
            return new Response(false, "Item not found.");
        }

        if (!(slot.getItem() instanceof Food) || slot.getItem().getEnergyCost() >= 0) {
            return new Response(false, "Item isn't edible food.");
        }

        Food food = (Food) slot.getItem();
        slot.setCount(slot.getCount() - 1);

        if (slot.getCount() == 0) {
            backpack.getSlots().remove(slot);
        }

        player.setEnergy(Math.min(player.getEnergy() - food.getEnergyCost(), player.getMaxEnergy()));
        player.getActiveBuffs().add(new ActiveBuff(food.foodBuff));

        GameRepository.saveGame(game);
        return new Response(true, "You successfully consumed: " + food.getName());
    }
}
