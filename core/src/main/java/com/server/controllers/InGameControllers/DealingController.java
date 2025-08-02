package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.*;
import com.common.models.enums.Quality;
import com.common.models.enums.recipes.CookingRecipes;
import com.common.models.enums.recipes.CraftingRecipes;
import com.common.models.enums.types.inventoryEnums.BackpackType;
import com.common.models.enums.types.inventoryEnums.TrashcanType;
import com.common.models.enums.types.itemTypes.*;
import com.common.models.enums.types.storeProductTypes.BlackSmithProducts;
import com.common.models.enums.types.storeProductTypes.FishProducts;
import com.common.models.items.*;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class DealingController extends Controller {
    public static boolean handleBuyRecipe(String name, StoreProduct p, Player player) {
        CraftingRecipes craft = CraftingRecipes.findByName(name.split(" ")[0]);
        CookingRecipes cook = CookingRecipes.findByName(name.split(" ")[0]);
        if (craft != null) {
            player.getUnlockedCraftingRecipes().add(craft);
            return true;
        }
        if (cook != null) {
            player.getUnlockedCookingRecipes().add(cook);
            return true;
        }

        return false;
    }

    public static boolean handleUpgradeTool(String name, StoreProduct p, Player player) {
        BlackSmithProducts trashCan = BlackSmithProducts.findTrashCanUpgrade(name);
        BlackSmithProducts tool = BlackSmithProducts.findSteelToolUpgrade(name);
        if (trashCan != null) {
            TrashcanType type = trashCan.getTrashcan();
            player.setTrashcanType(type);
            return true;
        } else if (tool != null) {
            Quality q = tool.getTool();
            if (player.getEquippedItem() instanceof Tool t) {
                p.setAvailableCount(p.getAvailableCount() - 1);
                Slot slot = player.getInventory().getSlotByItemName(p.getType().getIngredient().getName());
                if (slot == null || slot.getCount() < 5) {
                    return false;
                }
                slot.setCount(slot.getCount() - 5);
                if (slot.getCount() == 0) {
                    player.getInventory().removeSlot(slot);
                }
                Slot s = player.getInventory().getSlotByItemName(t.getName());
                t.setQuality(q);
                ((Tool) s.getItem()).setQuality(q);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void purchase(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String productName = (String) body.get("productName");
            String storeName = (String) body.get("storeName");
            Store store = game.getMap().getVillage().getStore(storeName);
            if (store == null) {
                ctx.json(Response.NOT_FOUND.setMessage("store not found"));
            }

            int n = 1;
            if ((Integer) body.get("count") != null) {
                n = (Integer) body.get("count");
            }
            StoreProduct p = store.getProduct(productName);
            if (p == null) {
                ctx.json(Response.NOT_FOUND.setMessage("product not found"));
                return;
            }
            if (p.getAvailableCount() < n) {
                ctx.json(Response.NOT_FOUND.setMessage("not enough available products"));
                return;
            }
            if (p.getType().getProductPrice(game.getSeason()) * n > player.getMoney(game)) {
                ctx.json(Response.NOT_FOUND.setMessage("not enough money"));
                return;
            }
            ItemType type = p.getType().getItemType();
            if (type == null && p.getType().getIngredient() == null) {
                if (p.getType().getName().equals("Large Pack")) {
                    player.getInventory().setType(BackpackType.GIANT);
                    player.setMoney((int) (player.getMoney(game) - p.getType().getProductPrice(game.getSeason())), game);
                    p.setAvailableCount(p.getAvailableCount() - n);
                    String gameJson = GameGSON.gson.toJson(game);
                    ctx.json(Response.OK.setMessage("success").setBody(gameJson));
                    HashMap<String, String> msg = new HashMap<>();
                    msg.put("type", "GAME_UPDATED");
                    msg.put("game", gameJson);
                    gs.broadcast(msg);
                    return;
                } else if (p.getType().getName().equals("Deluxe Pack")) {
                    if (player.getInventory().getType() != BackpackType.DEFAULT) {
                        player.getInventory().setType(BackpackType.DELUXE);
                        player.setMoney((int) (player.getMoney(game) - p.getType().getProductPrice(game.getSeason())), game);
                    } else {
                        ctx.json(Response.BAD_REQUEST.setMessage("not enough available products"));
                        return;
                    }
                    p.setAvailableCount(p.getAvailableCount() - n);
                    String gameJson = GameGSON.gson.toJson(game);
                    ctx.json(Response.OK.setMessage("success").setBody(gameJson));
                    HashMap<String, String> msg = new HashMap<>();
                    msg.put("type", "GAME_UPDATED");
                    msg.put("game", gameJson);
                    gs.broadcast(msg);
                    return;
                }
                boolean res = handleBuyRecipe(productName, p, player);
                String gameJson = GameGSON.gson.toJson(game);

                if (res) {
                    p.setAvailableCount(p.getAvailableCount() - n);
                    player.setMoney((int) (player.getMoney(game) - p.getType().getProductPrice(game.getSeason()) * n), game);
                    ctx.json(Response.OK.setMessage("success").setBody(gameJson));
                } else {
                    ctx.json(Response.BAD_REQUEST.setMessage("error").setBody(gameJson));
                }
                HashMap<String, String> msg = new HashMap<>();
                msg.put("type", "GAME_UPDATED");
                msg.put("game", gameJson);
                gs.broadcast(msg);
                return;
            } else if (type == null && p.getType().getIngredient() != null) {
                boolean res = handleUpgradeTool(productName, p, player);
                String gameJson = GameGSON.gson.toJson(game);

                if (res) {
                    player.setMoney((int) (player.getMoney(game) - p.getType().getProductPrice(game.getSeason())), game);
                    ctx.json(Response.OK.setMessage("success").setBody(gameJson));
                } else {
                    ctx.json(Response.BAD_REQUEST.setMessage("error").setBody(gameJson));

                }
                HashMap<String, String> msg = new HashMap<>();
                msg.put("type", "GAME_UPDATED");
                msg.put("game", gameJson);
                gs.broadcast(msg);
                return;
            } else {
                Item item = null;
                if (type instanceof FoodTypes) {
                    item = new Food((FoodTypes) type);
                } else if (type instanceof CropSeedsType) {
                    item = new Seed((CropSeedsType) type);
                } else if (type instanceof TreeSeedsType) {
                    item = new TreeSeed((TreeSeedsType) type);
                } else if (type instanceof FishType) {
                    item = new Fish(Quality.DEFAULT, (FishType) type);
                } else if (type instanceof MiscType) {
                    item = new Misc((MiscType) type);
                } else if (type instanceof ForagingMineralsType) {
                    item = new ForagingMineralItem(Quality.DEFAULT, (ForagingMineralsType) type);
                } else if (type instanceof ToolTypes) {
                    Quality q = Quality.DEFAULT;
                    if (p.getType().getName().equals(FishProducts.BAMBOO_POLE.getName())) {
                        q = Quality.SILVER;
                    } else if (p.getType().getName().equals(FishProducts.TRAINING_ROD.getName())) {
                        q = Quality.COPPER;
                    } else if (p.getType().getName().equals(FishProducts.FIBERGLASS_ROD.getName())) {
                        q = Quality.GOLD;
                    } else if (p.getType().getName().equals(FishProducts.IRIDIUM_ROD.getName())) {
                        q = Quality.IRIDIUM;
                    }
                    item = new Tool(q, (ToolTypes) type, (int) p.getType().getProductPrice(game.getSeason()));
                }
                if (item == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("not enough available products"));
                    return;
                }
                Backpack backpack = player.getInventory();
                Slot slot = backpack.getSlotByItemName(item.getName());
                if (slot == null) {
                    if (backpack.getSlots().size() == backpack.getType().getMaxCapacity()) {
                        ctx.json(Response.BAD_REQUEST.setMessage("not enough available products"));
                        return;
                    }

                    Slot newSlot = new Slot(item, n);
                    backpack.addSlot(newSlot);
                } else {
                    slot.setCount(slot.getCount() + 1);
                }
                player.setMoney((int) (player.getMoney(game) - p.getType().getProductPrice(game.getSeason()) * n), game);
                p.setAvailableCount(p.getAvailableCount() - n);
                String gameJson = GameGSON.gson.toJson(game);
                ctx.json(Response.OK.setMessage("success").setBody(gameJson));
                HashMap<String, String> msg = new HashMap<>();
                msg.put("type", "GAME_UPDATED");
                msg.put("game", gameJson);
                gs.broadcast(msg);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void sellProduct(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String productName = (String) body.get("productName");

            if (!player.isNearShippingBin()) {
                ctx.json(Response.BAD_REQUEST.setMessage("You are not near shipping bin"));
                return;
            }
            Slot productSlot = player.getInventory().getSlotByItemName(productName);
            if (productSlot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("No product"));
                return;
            }
            int n = productSlot.getCount();
            if ((Integer) body.get("count") != null) {
                n = (Integer) body.get("count");
            }
            if (n > productSlot.getCount()) {
                ctx.json(Response.BAD_REQUEST.setMessage("Not enough available products"));
                return;
            }
            productSlot.setCount(productSlot.getCount() - n);
            if (productSlot.getCount() == 0) {
                player.getInventory().removeSlot(productSlot);
            }
            double money = productSlot.getItem().getValue() * n;
            if (productSlot.getItem().getQuality() == Quality.SILVER) {
                money = money * 1.25;
            } else if (productSlot.getItem().getQuality() == Quality.GOLD) {
                money = money * 1.5;
            } else if (productSlot.getItem().getQuality() == Quality.IRIDIUM) {
                money = money * 2;
            }
            player.setMoneyInNextDay(player.getMoneyInNextDay() + (int) money);
            String gameJson = GameGSON.gson.toJson(game);
            ctx.json(Response.OK.setMessage("success").setBody(gameJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "GAME_UPDATED");
            msg.put("game", gameJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}

