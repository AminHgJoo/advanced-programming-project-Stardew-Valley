package com.server.controllers.InGameControllers;

import com.common.models.GameData;
import com.common.models.Player;
import com.common.utils.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatController extends Controller {
    public void fetchMessages(Context ctx, GameServer gs) {
        try {
            GameData gameData = gs.getGame();
            ArrayList<ChatMessage> messages = gameData.chatMessages;
            String arrayJson = new Gson().toJson(messages);
            ctx.json(Response.OK.setMessage("").setBody(arrayJson));
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    public void addMessage(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String message = (String) body.get("message");
            String sender = (String) body.get("sender");
            String recipient = (String) body.get("recipient");
            Boolean isPrivate = (Boolean) body.get("isPrivate");

            ChatMessage msg = new ChatMessage(message, sender, recipient, isPrivate);
            gs.getGame().chatMessages.add(msg);
            ctx.json(Response.OK.setMessage("Message Added Successfully").setBody(""));

            HashMap<String, String> multicastMsg = new HashMap<>();
            multicastMsg.put("type", "MESSAGE_ADDED");
            multicastMsg.put("message", new GsonBuilder().setPrettyPrinting().create().toJson(msg));
            ArrayList<String> multicastRecipients = new ArrayList<>();

            for (Player player : gs.getGame().getPlayers()) {
                String username = player.getUser().getUsername();

                if (!username.equals(sender)) {
                    multicastRecipients.add(username);
                }
            }

            gs.multicast(multicastMsg, multicastRecipients);

        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    //TODO: Update gameData after cheat execution via broadcast
    public void parseCheat(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String command = (String) body.get("command");
            //TODO: Implement.

            ctx.json(Response.OK.setMessage("Placeholder").setBody(command));
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }
}
