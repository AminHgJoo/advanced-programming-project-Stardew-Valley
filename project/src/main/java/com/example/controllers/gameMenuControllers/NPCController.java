package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.User;
import com.example.utilities.HuggingFaceChat;

public class NPCController extends Controller {
    public static Response handleTalkNPC(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String npcName = request.body.get("npcName");
        String message = request.body.get("message");

        String response = HuggingFaceChat.getNpcDialogue(message);
        return new Response(true , response);
    }
    public static Response handleMeetNPC(Request request) {
        return null;
    }

    public static Response handleGiftNPC(Request request) {
        return null;
    }

    public static Response handleFriendshipNPCList(Request request) {
        return null;
    }

    public static Response handleQuestList(Request request) {
        return null;
    }

    public static Response handleQuestFinish(Request request) {
        return null;
    }
}
