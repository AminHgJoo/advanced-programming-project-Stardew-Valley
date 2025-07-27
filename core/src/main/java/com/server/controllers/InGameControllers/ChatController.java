package com.server.controllers.InGameControllers;

import com.common.models.GameData;
import com.common.utils.ChatMessage;
import com.google.gson.Gson;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.ArrayList;

public class ChatController extends Controller {
    public void fetchMessages(Context ctx, GameServer gs) {
        try {
            GameData gameData = gs.getGame();
            ArrayList<ChatMessage> messages = gameData.chatMessages;
            String arrayJson = new Gson().toJson(messages);
            ctx.json(Response.OK.setMessage("").setBody(arrayJson));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
