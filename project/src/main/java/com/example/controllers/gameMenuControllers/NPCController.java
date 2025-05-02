package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.NPCModels.NPC;
import com.example.models.items.Item;
import com.example.utilities.HuggingFaceChat;

public class NPCController extends Controller {
    public static Response handleTalkNPC(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        String message = request.body.get("message");
        NPC npc = game.findNpcByName(npcName);
        if (npc == null) {
            return new Response(false, "There is no npc with this name.");
        }
        if (!npc.getHasTalked().get(player.getUser().getUsername())) {
            npc.getHasTalked().put(player.getUser().getUsername(), true);
            player.addXpToNpcFriendship(20, npc);
            GameRepository.saveGame(game);
        }
        String response = HuggingFaceChat.getNpcDialogue(message);
        return new Response(true, response);
    }

    public static Response handleMeetNPC(Request request) {
        return null;
    }

    public static Response handleGiftNPC(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        String itemName = request.body.get("item");
        NPC npc = game.findNpcByName(npcName);
        if (npc == null) {
            return new Response(false, "There is no npc with this name.");
        }
        Slot itemSlot = player.getInventory().getSlotByItemName(itemName);
        if (itemSlot == null) {
            return new Response(false, "There is no item with this name.");
        }
        itemSlot.setCount(itemSlot.getCount() - 1);
        if (itemSlot.getCount() == 0) {
            player.getInventory().removeSlot(itemSlot);
        }
        if (npc.getGift().get(player.getUser().getUsername())) {
            Item item = itemSlot.getItem();
            if (npc.isItemInFavorite(item)) {
                player.addXpToNpcFriendship(200, npc);
            } else {
                player.addXpToNpcFriendship(50, npc);
            }
            npc.getGift().put(player.getUser().getUsername(), true);
        }
        GameRepository.saveGame(game);
        return new Response(true , "Gift has been sent.");
    }

    public static Response handleFriendshipNPCList(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        return new Response(true, player.npcFriendShipToString());
    }

    public static Response handleQuestList(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        NPC npc = game.findNpcByName(npcName);
        if (npc == null) {
            return new Response(false, "There is no npc with this name.");
        }
        return new Response(true , npc.questsToString());
    }

    public static Response handleQuestFinish(Request request) {
        return null;
    }
}
