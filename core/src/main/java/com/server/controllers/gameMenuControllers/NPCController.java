package com.server.controllers.gameMenuControllers;

import com.common.models.*;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.NPCModels.NPC;
import com.common.models.NPCModels.NPCFriendship;
import com.common.models.NPCModels.NPCReward;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.items.Item;
import com.common.models.items.Tool;
import com.server.repositories.GameRepository;
import com.server.utilities.AIChat;
import com.server.controllers.Controller;

// TODO handle npc friendship level

public class NPCController extends Controller {
    public static Response handleTalkNPC(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        String message = request.body.get("message");
        NPC npc = gameData.findNpcByName(npcName);
        if (npc == null) {
            return new Response(false, "There is no npc with this name.");
        }
        if (!player.isInVillage()) {
            return new Response(false, "You are not in the village.");
        }
        if (!npc.getHasTalked().getOrDefault(player.getUser().getUsername(), false)) {
            npc.getHasTalked().put(player.getUser().getUsername(), true);
            player.addXpToNpcFriendship(20, npc);
            GameRepository.saveGame(gameData);
        }
        String response = AIChat.getNpcDialogue(message);
        return new Response(true, response);
    }

    public static Response handleMeetNPC(Request request) {
        return null;
    }

    public static Response handleGiftNPC(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        String itemName = request.body.get("item");
        NPC npc = gameData.findNpcByName(npcName);
        if (npc == null) {
            return new Response(false, "There is no npc with this name.");
        }
        Slot itemSlot = player.getInventory().getSlotByItemName(itemName);
        if (itemSlot == null) {
            return new Response(false, "There is no item with this name.");
        }
        if (itemSlot.getItem() instanceof Tool) {
            return new Response(false, "You can't gift tool.");
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
        GameRepository.saveGame(gameData);
        return new Response(true, "Gift has been sent.");
    }

    public static Response handleFriendshipNPCList(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();

        return new Response(true, player.npcFriendShipToString());
    }

    public static Response handleQuestList(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        NPC npc = gameData.findNpcByName(npcName);
        if (npc == null) {
            return new Response(false, "There is no npc with this name.");
        }
        NPCFriendship friendship = player.findFriendshipByNPC(npcName);
        return new Response(true, npc.questsToString(friendship.getLevel()));
    }

    public static Response handleQuestFinish(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        NPC npc = gameData.findNpcByName(npcName);
        if (npc == null) {
            return new Response(false, "There is no npc with this name.");
        }
        int index = Integer.parseInt(request.body.get("index"));
        if (index >= 3) {
            return new Response(false, "Invalid quest index");
        }
        Quest q = npc.getQuests().get(index);
        if (q.isCompleted()) {
            return new Response(false, "Quest is completed.");
        }
        ItemType type = q.getItem();

        Slot itemSlot = player.getInventory().getSlotByItemName(type.getName());
        if (itemSlot == null) {
            return new Response(false, "You don't have required items");
        }
        if (itemSlot.getCount() < q.getCount()) {
            return new Response(false, "You don't have enough required items");
        }

        itemSlot.setCount(itemSlot.getCount() - q.getCount());
        if (itemSlot.getCount() == 0) {
            player.getInventory().removeSlot(itemSlot);
        }
        q.setCompleted(true);

        NPCReward reward = npc.getRewards().get(index);
        player.addNpcReward(reward, npc, gameData);
        GameRepository.saveGame(gameData);
        return new Response(true, "Quest has been finished");
    }
}
