package com.server.controllers.gameMenuControllers;

import com.common.models.*;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.Quality;
import com.common.models.enums.recipes.CraftingRecipes;
import com.common.models.enums.types.itemTypes.CropSeedsType;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.enums.types.itemTypes.TreeSeedsType;
import com.common.models.enums.types.mapObjectTypes.ArtisanBlockType;
import com.common.models.enums.types.mapObjectTypes.ForagingCropsType;
import com.common.models.enums.types.mapObjectTypes.TreeType;
import com.common.models.items.*;
import com.common.models.items.buffs.ActiveBuff;
import com.common.models.mapModels.Cell;
import com.common.models.mapObjects.ArtisanBlock;
import com.common.models.mapObjects.DroppedItem;
import com.common.models.mapObjects.EmptyCell;
import com.common.repositories.GameRepository;
import com.server.controllers.Controller;
import org.intellij.lang.annotations.Language;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryFunctionalities extends Controller {
    public static Response cheatAddSkillXP(Request request) {
        String skill = request.body.get("skill");
        String amount = request.body.get("amount");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        if (skill.compareToIgnoreCase("farming") == 0) {
            player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + Integer.parseInt(amount));
        } else if (skill.compareToIgnoreCase("foraging") == 0) {
            player.getUnbuffedForagingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + Integer.parseInt(amount));
        } else if (skill.compareToIgnoreCase("fishing") == 0) {
            player.getUnbuffedFishingSkill().setXp(player.getUnbuffedFishingSkill().getXp() + Integer.parseInt(amount));
        } else if (skill.compareToIgnoreCase("mining") == 0) {
            player.getUnbuffedMiningSkill().setXp(player.getUnbuffedMiningSkill().getXp() + Integer.parseInt(amount));
        } else {
            GameRepository.saveGame(gameData);
            return new Response(false, "wrong skill name");
        }
        GameRepository.saveGame(gameData);
        return new Response(true, amount + " successfully added to " + skill + " skill");
    }

    public static Response handleShowInventory(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Backpack backpack = gameData.getCurrentPlayer().getInventory();
        String output = backpack.showInventory();
        if (output.isEmpty())
            return new Response(true, "Your Backpack is empty!");
        return new Response(true, output);
    }

    public static Response handleInventoryTrashing(Request request) {
        String itemName = request.body.get("itemName");
        String number = request.body.get("number");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Backpack backpack = gameData.getCurrentPlayer().getInventory();
        Slot slot = backpack.getSlotByItemName(itemName);
        if (slot == null)
            return new Response(false, "item(s) does not exist!");
        if (number == null)
            return removeSlotHandle(slot, player, backpack, gameData);
        int numberInt = Integer.parseInt(number);
        slot.setCount(slot.getCount() - numberInt);
        if (slot.getCount() <= 0)
            return removeSlotHandle(slot, player, backpack, gameData);
        return removeItemHandle(numberInt, slot, player, gameData);
    }

    private static Response removeItemHandle(int numberInt, Slot slot, Player player, GameData gameData) {
        int cashBack = (numberInt * slot.getItem().getValue() *
            player.getTrashcanRefundPercentage()) / 100;
        player.setMoney(player.getMoney(gameData) + cashBack, gameData);
        GameRepository.saveGame(gameData);
        return new Response(true, numberInt + " of item(s) successfully trashed!");
    }

    private static Response removeSlotHandle(Slot slot, Player player, Backpack backpack, GameData gameData) {
        int cashBack = (slot.getCount() * slot.getItem().getValue() *
            player.getTrashcanRefundPercentage()) / 100;
        player.setMoney(player.getMoney(gameData) + cashBack, gameData);
        backpack.removeSlot(slot);
        GameRepository.saveGame(gameData);
        return new Response(true, "Item successfully trashed!" + "profit: " + cashBack);
    }

    public static Response handleToolEquip(Request request) {
        String toolName = request.body.get("toolName");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Backpack backpack = gameData.getCurrentPlayer().getInventory();
        Slot slot = backpack.getSlotByItemName(toolName);
        if (slot == null)
            return new Response(false, "item(s) does not exist!");
        if (!(slot.getItem() instanceof Tool))
            return new Response(false, "item(s) is not a tool!");
        gameData.getCurrentPlayer().setEquippedItem(slot.getItem());
        GameRepository.saveGame(gameData);
        return new Response(true, "Equipped " + toolName + " successfully!");
    }

    public static Response handleEquippedToolQuery(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Item item = gameData.getCurrentPlayer().getEquippedItem();
        if (item == null)
            return new Response(false, "No equipped tool found!");
        return new Response(true, item.getName());

    }

    public static Response handleShowTools(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Backpack backpack = gameData.getCurrentPlayer().getInventory();
        String output = backpack.showTools();
        if (output.isEmpty())
            return new Response(true, "No tools found!");
        return new Response(true, output);

    }

    public static Response handleCraftInfoQuery(Request request) {
        String queriedName = request.body.get("craftName");

        for (CropSeedsType type : CropSeedsType.values()) {
            if (queriedName.compareToIgnoreCase(type.name) == 0) {
                return new Response(true, type.toString());
            }
        }

        for (TreeType type : TreeType.values()) {
            if (queriedName.compareToIgnoreCase(type.name) == 0) {
                return new Response(true, type.toString());
            }
        }

        for (ForagingCropsType type : ForagingCropsType.values()) {
            if (queriedName.compareToIgnoreCase(type.name) == 0) {
                return new Response(true, type.toString());
            }
        }

        return new Response(false, "Crop not found!");
    }

    public static Response handleShowCraftingRecipes(Request request) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Crafting Recipes:\n");

        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();

        for (CraftingRecipes craftingRecipes : player.getUnlockedCraftingRecipes()) {
            stringBuilder.append(craftingRecipes.toString()).append("\n");
        }

        return new Response(true, stringBuilder.toString());
    }

    public static Response handleItemCrafting(Request request) {
        String itemName = request.body.get("itemName");
        CraftingRecipes targetRecipe = null;

        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();

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
                GameRepository.saveGame(gameData);
                return new Response(false, "You don't have the required materials to craft this item!");
            }

            if (playerSlot.getCount() < ingredient.getCount()) {
                GameRepository.saveGame(gameData);
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

        GameRepository.saveGame(gameData);
        return new Response(true, "You crafted " + targetRecipe.name);
    }

    private static int[] getXAndYIncrement(String direction) {
        if (direction.compareToIgnoreCase("up") == 0) {
            return new int[]{0, -1};
        } else if (direction.compareToIgnoreCase("down") == 0) {
            return new int[]{0, 1};
        } else if (direction.compareToIgnoreCase("right") == 0) {
            return new int[]{1, 0};
        } else if (direction.compareToIgnoreCase("left") == 0) {
            return new int[]{-1, 0};
        } else if (direction.compareToIgnoreCase("up_right") == 0) {
            return new int[]{1, -1};
        } else if (direction.compareToIgnoreCase("up_left") == 0) {
            return new int[]{-1, -1};
        } else if (direction.compareToIgnoreCase("down_right") == 0) {
            return new int[]{1, 1};
        } else if (direction.compareToIgnoreCase("down_left") == 0) {
            return new int[]{-1, 1};
        } else {
            return new int[]{10000, 10000};
        }
    }

    public static Response handlePlaceItem(Request request) {
        String direction = request.body.get("direction");
        String itemName = request.body.get("itemName");

        int[] xAndY = getXAndYIncrement(direction);

        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Backpack backpack = player.getInventory();

        int dx = xAndY[0];
        int dy = xAndY[1];
        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();

        Slot slotToPlace = backpack.getSlotByItemName(itemName);

        if (slotToPlace == null) {
            return new Response(false, "Item not found.");
        }

        Cell targetCell = player.getCurrentFarm(gameData).findCellByCoordinate(playerX + dx, playerY + dy);

        if (targetCell == null) {
            return new Response(false, "Invalid placement location.");
        }

        if (!(targetCell.getObjectOnCell() instanceof EmptyCell)) {
            return new Response(false, "Target cell is not empty.");
        }

        if (slotToPlace.getItem() instanceof Misc) {

            Misc misc = (Misc) slotToPlace.getItem();

            if (misc.getMiscType().isArtisanBlock && misc.getMiscType().isPlacable) {
                targetCell.setObjectOnCell(new ArtisanBlock(ArtisanBlockType.getArtisanBlockTypeByName(misc.getName())));
                slotToPlace.setCount(slotToPlace.getCount() - 1);
                if (slotToPlace.getCount() == 0) {
                    backpack.getSlots().remove(slotToPlace);
                }
            } else if (misc.getMiscType().isPlacable) {
                targetCell.setObjectOnCell(new DroppedItem(slotToPlace.getCount(), slotToPlace.getItem()));
                slotToPlace.setCount(slotToPlace.getCount() - 1);
                if (slotToPlace.getCount() == 0) {
                    backpack.getSlots().remove(slotToPlace);
                }
            } else {
                targetCell.setObjectOnCell(new DroppedItem(slotToPlace.getCount(), slotToPlace.getItem()));
                backpack.getSlots().remove(slotToPlace);
            }
        } else {
            targetCell.setObjectOnCell(new DroppedItem(slotToPlace.getCount(), slotToPlace.getItem()));
            backpack.getSlots().remove(slotToPlace);
        }

        GameRepository.saveGame(gameData);
        return new Response(true, "Item placed successfully on Coordinates : x = " + (playerX + dx) + ", y = " + (playerY + dy));
    }

    public static Response handleAddItemCheat(Request request) {
        int count = Integer.parseInt(request.body.get("count"));
        String itemName = request.body.get("itemName");

        HashMap<String, ItemType> allItemsList = App.getAllItemTypes();

        @Language("Regexp")
        String toolsRegex = "(?<quality>Default|Copper|Silver|Gold|Iridium|Training|Bamboo|Fiberglass) " +
            "(?<toolName>Axe|Hoe|Pickaxe|Fishing Rod|Scythe)";
        Pattern pattern = Pattern.compile(toolsRegex);
        Matcher matcher = pattern.matcher(itemName);

        boolean matchFound = matcher.matches();

        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Backpack backpack = player.getInventory();

        if (!allItemsList.containsKey(itemName) && !matchFound) {
            return new Response(false, "Item doesn't exist.");
        }

        if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
            return new Response(false, "You don't have enough space to add an item.");
        }

        Slot slot = backpack.getSlotByItemName(itemName);

        Quality quality = Quality.DEFAULT;

        if (matchFound) {
            quality = Quality.getQualityByName(matcher.group("quality"));
        }

        if (slot == null) {
            Slot toBeAddedSlot = allItemsList.get(matchFound ? matcher.group("toolName") : itemName).createAmountOfItem(count, quality);
            backpack.getSlots().add(toBeAddedSlot);
        } else {
            slot.setCount(Math.min(slot.getCount() + count, slot.getItem().getMaxStackSize()));
        }

        GameRepository.saveGame(gameData);
        return new Response(true, "Item added successfully.");
    }

    public static Response handleEating(Request request) {
        String foodName = request.body.get("foodName");

        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
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

        player.getActiveBuffs().add(new ActiveBuff(food.foodBuff));
        player.setEnergy(Math.min(player.getEnergy() - food.getEnergyCost(), player.getMaxEnergy()));

        GameRepository.saveGame(gameData);
        return new Response(true, "You successfully consumed: " + food.getName());
    }
}
