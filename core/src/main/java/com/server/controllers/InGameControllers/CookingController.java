package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.Backpack;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.Quality;
import com.common.models.enums.recipes.CookingRecipes;
import com.common.models.items.Food;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class CookingController extends Controller {
    public void handleCooking(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            HashMap<String, String> body = ctx.bodyAsClass(HashMap.class);
            String recipe = body.get("recipe");
            CookingRecipes targetRecipe = null;

            GameData gameData = gs.getGame();
            Player player = gameData.getCurrentPlayer();

            for (CookingRecipes recipes : player.getUnlockedCookingRecipes()) {
                if (recipes.name.compareToIgnoreCase(recipe) == 0) {
                    targetRecipe = recipes;
                    break;
                }
            }

            if (targetRecipe == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Recipe not found"));
                return;
            }

            Backpack backpack = player.getInventory();

            //one fish
            if (targetRecipe == CookingRecipes.MAKI_ROLL) {
                Slot fishSlot = backpack.getFirstFish();

                if (fishSlot == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("No fish found"));
                    return;
                }

                fishSlot.setCount(fishSlot.getCount() - 1);
                if (fishSlot.getCount() == 0) {
                    backpack.getSlots().remove(fishSlot);
                }
            }

            //deduct ingredients.
            for (Slot ingredient : targetRecipe.ingredients) {
                Slot backpackSlot = backpack.getSlotByItemName(ingredient.getItem().getName());

                int countToReduce = ingredient.getCount();

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

            String json = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(json));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", json);
            gs.broadcast(msg);
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }
}
