package com.server.GameServers;

import com.common.models.mapModels.Map;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.Context;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppWebSocket {
    private final Javalin app;
    private static ConcurrentHashMap<String, PlayerConnection> connectedPlayers = new ConcurrentHashMap<>();
    private static CopyOnWriteArrayList<GameServer> activeGames = new CopyOnWriteArrayList<>();

    public AppWebSocket(Javalin app) {
        this.app = app;
    }

    public void start() {
        app.ws("/game", ws -> {
            ws.onConnect(ctx -> {
                String playerId = ctx.queryParam("playerId");
                connectedPlayers.put(playerId, new PlayerConnection(playerId, ctx));
                ctx.send("Connected as " + playerId);
            });

            ws.onMessage(ctx -> {
                JSONObject message = new JSONObject(ctx.message());
                // TODO find the player by the id
                String command = message.getString("command");
                if (command.equals("INVITE")) {
                    handleInvite(message , ctx);
                } else if (command.equals("ACCEPT")) {
                    handleAccept(message , ctx);
                }
            });

            ws.onClose(ctx -> {
                String playerId = ctx.queryParam("playerId");
                connectedPlayers.remove(playerId);
            });

        });
    }

    public static void handleInvite(JSONObject body  , WsContext ctx) {
        // TODO find the game and players by the id
        // TODO update response messages
        JSONArray players = body.getJSONArray("players");
        for (int i = 0; i < players.length(); i++) {
            String playerId = players.getString(i);
            PlayerConnection connection = connectedPlayers.get(playerId);
            if (connection != null) {
                connection.send("Player " + body.getString("id") + " has invited you to game "
                    + body.getString("gameID"));
            }else {
                ctx.send("Player " + playerId + " is not online now !");
            }
        }
    }

    public static void handleAccept(JSONObject body , WsContext ctx) {

    }
}
