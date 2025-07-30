package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.NPCModels.NPC;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.items.Item;
import com.common.models.items.Tool;
import com.server.GameServers.GameServer;
import com.server.repositories.GameRepository;
import com.server.utilities.AIChat;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class NpcController extends Controller {
    public void talk(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String npcName = (String) body.get("npcName");
            String message = (String) body.get("message");
            NPC npc = game.findNpcByName(npcName);
            if (npc == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("NPC not found"));
                return;
            }
            if (!player.isInVillage()) {
                ctx.json(Response.BAD_REQUEST.setMessage("Player is not in Village"));
                return;
            }
            if (!npc.getHasTalked().getOrDefault(player.getUser().getUsername(), false)) {
                npc.getHasTalked().put(player.getUser().getUsername(), true);
                player.addXpToNpcFriendship(20, npc);
            }
            String response = AIChat.getNpcDialogue(message , npc.context(game , player.getUser().getUsername()));
            String gameJson = GameGSON.gson.toJson(game);

            ctx.json(Response.OK.setMessage(response).setBody(gameJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "GAME_UPDATED");
            msg.put("game", gameJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void gift(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String npcName = (String) body.get("npcName");
            String itemName = (String) body.get("item");
            NPC npc = game.findNpcByName(npcName);
            if (npc == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("NPC not found"));
                return;
            }
            Slot itemSlot = player.getInventory().getSlotByItemName(itemName);
            if (itemSlot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Item not found"));
                return;
            }
            if (itemSlot.getItem() instanceof Tool) {
                ctx.json(Response.BAD_REQUEST.setMessage("You cannot use this tool"));
                return;
            }
            itemSlot.setCount(itemSlot.getCount() - 1);
            if (itemSlot.getCount() == 0) {
                player.getInventory().removeSlot(itemSlot);
            }
            if (!npc.getGift().getOrDefault(player.getUser().getUsername(), false)) {
                Item item = itemSlot.getItem();
                if (npc.isItemInFavorite(item)) {
                    player.addXpToNpcFriendship(200, npc);
                } else {
                    player.addXpToNpcFriendship(50, npc);
                }
                npc.getGift().put(player.getUser().getUsername(), true);
            }
            String gameJson = GameGSON.gson.toJson(game);
            ctx.json(Response.OK.setMessage(gameJson));
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
