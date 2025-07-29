package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.*;
import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.types.mapObjectTypes.ArtisanBlockType;
import com.common.models.items.Food;
import com.common.models.items.Misc;
import com.common.models.items.Tool;
import com.common.models.items.buffs.ActiveBuff;
import com.common.models.mapModels.Cell;
import com.common.models.mapObjects.ArtisanBlock;
import com.common.models.mapObjects.DroppedItem;
import com.common.models.mapObjects.EmptyCell;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class InventoryController extends Controller {
    private static int[] getXAndYIncrement(String direction) {
        if (direction.compareToIgnoreCase("down") == 0) {
            return new int[]{0, -1};
        } else if (direction.compareToIgnoreCase("up") == 0) {
            return new int[]{0, 1};
        } else if (direction.compareToIgnoreCase("right") == 0) {
            return new int[]{1, 0};
        } else if (direction.compareToIgnoreCase("left") == 0) {
            return new int[]{-1, 0};
        } else if (direction.compareToIgnoreCase("down_right") == 0) {
            return new int[]{1, -1};
        } else if (direction.compareToIgnoreCase("down_left") == 0) {
            return new int[]{-1, -1};
        } else if (direction.compareToIgnoreCase("up_right") == 0) {
            return new int[]{1, 1};
        } else if (direction.compareToIgnoreCase("up_left") == 0) {
            return new int[]{-1, 1};
        } else {
            return new int[]{10000, 10000};
        }
    }

    public void toolEquip(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String toolName = (String) body.get("toolName");
            String id = ctx.attribute("id");
            GameData game = gs.getGame();

            Player player = game.findPlayerByUserId(id);
            if (toolName == null) {
                player.setEquippedItem(null);
                String playerJson = GameGSON.gson.toJson(player);
                ctx.json(Response.OK.setMessage("Set Null").setBody(playerJson));
                HashMap<String, String> msg = new HashMap<>();
                msg.put("type", "PLAYER_UPDATED");
                msg.put("player", playerJson);
                gs.broadcast(msg);
                return;
            }
            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(toolName);

            if (slot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("item(s) does not exist!"));
                return;

            }
            player.setEquippedItem(slot.getItem());
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setMessage("Equipped " + toolName + " successfully!")
                .setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player", playerJson);

            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void changeInventory(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String backpackJson = (String) body.get("backpack");

            Backpack backpack = GameGSON.gson.fromJson(backpackJson, Backpack.class);
            player.setInventory(backpack);
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setMessage("Changed Inventory").setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void placeItem(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String itemName = (String) body.get("itemName");
            System.out.println(itemName);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);

            Backpack backpack = player.getInventory();
            float playerX = player.getCoordinate().getX() / 32;
            float playerY = 49 - player.getCoordinate().getY() / 32;

            Slot slotToPlace = backpack.getSlotByItemName(itemName);
            if (slotToPlace == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("item(s) does not exist!"));
                return;
            }
            Cell targetCell = player.getCurrentFarm(game).findCellByCoordinate(playerX, playerY);
            if (targetCell == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell does not exist!"));
                return;
            }
            if (!(targetCell.getObjectOnCell() instanceof EmptyCell)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Target cell is not empty."));
                return;
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

            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setMessage("Placed " + itemName + " successfully!").setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void eat(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String foodName = (String) body.get("foodName");

            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Backpack backpack = player.getInventory();

            Slot slot = backpack.getSlotByItemName(foodName);

            if (slot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("item(s) does not exist!"));
                return;
            }

            if (!(slot.getItem() instanceof Food) || slot.getItem().getEnergyCost() >= 0) {
                ctx.json(Response.BAD_REQUEST.setMessage("item(s) isn't edible food."));
                return;
            }
            Food food = (Food) slot.getItem();
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.getSlots().remove(slot);
            }
            player.getActiveBuffs().add(new ActiveBuff(food.foodBuff));
            player.setEnergy(Math.min(player.getEnergy() - food.getEnergyCost(), player.getMaxEnergy()));
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "FOOD_EAT");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);

            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void addItem(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String itemName = (String) body.get("itemName");
            String quality = (String) body.get("quality");
            int count = (Integer) body.get("count");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Backpack backpack = player.getInventory();
            HashMap<String, ItemType> allItemsList = App.getAllItemTypes();

            if (!allItemsList.containsKey(itemName)) {
                ctx.json(Response.BAD_REQUEST.setMessage("item(s) does not exist!"));
                return;
            }
            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                ctx.json(Response.BAD_REQUEST.setMessage("item(s) isn't edible backpack!"));
                return;
            }

            Slot slot = backpack.getSlotByItemName(itemName);

            Quality q = Quality.getQualityByName(quality);

            if (slot == null) {
                Slot toBeAddedSlot = allItemsList.get(itemName).createAmountOfItem(count, q);
                backpack.getSlots().add(toBeAddedSlot);
            } else {
                slot.setCount(Math.min(slot.getCount() + count, slot.getItem().getMaxStackSize()));
            }
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void removeItem(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String itemName = (String) body.get("itemName");
            int count = (Integer) body.get("count");

            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Backpack backpack = player.getInventory();

            Slot slot = backpack.getSlotByItemName(itemName);
            if (slot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("item(s) does not exist!"));
                return;
            }
            slot.setCount(slot.getCount() - count);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }

            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
