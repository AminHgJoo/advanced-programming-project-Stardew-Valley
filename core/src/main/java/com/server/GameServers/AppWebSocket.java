package com.server.GameServers;

import com.common.models.GameData;
import com.common.models.User;
import com.server.repositories.GameRepository;
import com.server.repositories.UserRepository;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

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
                ctx.enableAutomaticPings(5, TimeUnit.SECONDS);
                System.out.println("Someone connected");
                String playerId = ctx.queryParam("playerId");
                connectedPlayers.put(playerId, new PlayerConnection(playerId, ctx));
                ctx.send("Connected as " + playerId);
            });

            ws.onMessage(ctx -> {
                String playerId = ctx.queryParam("playerId");
                User user = UserRepository.findUserById(playerId);
                if (user != null) {
                    JSONObject message = new JSONObject(ctx.message());
                    // TODO find the player by the id
                    String command = message.getString("command");
                    if (command.equals("INVITE")) {
                        handleInvite(message, ctx);
                    } else if (command.equals("ACCEPT")) {
                        handleAccept(message, ctx);
                    }
                }
            });

            ws.onClose(ctx -> {
                System.out.println("Disconnected");
                String playerId = ctx.queryParam("playerId");
                connectedPlayers.remove(playerId);
            });

        });
    }

    public static void handleInvite(JSONObject body, WsContext ctx) {
        // TODO find the game and players by the id
        // TODO update response messages
        JSONArray players = body.getJSONArray("players");
        for (int i = 0; i < players.length(); i++) {
            String playerId = players.getString(i);
            PlayerConnection connection = connectedPlayers.get(playerId);
            if (connection != null) {
                connection.send("Player " + body.getString("id") + " has invited you to game "
                    + body.getString("gameId"));
            } else {
                ctx.send("Player " + playerId + " is not online now !");
            }
        }
    }

    public static void handleAccept(JSONObject body, WsContext ctx) {
        String gameId = body.getString("gameId");
        GameData game = GameRepository.findGameById(gameId, false);
        if (game != null) {
            if (game.getPlayers().size() == game.getCapacity()) {
                ArrayList<PlayerConnection> players = new ArrayList<>();
                game.getPlayers().forEach(player -> {
                    String user = player.getUser_id();
                    PlayerConnection connection = connectedPlayers.get(user);
                    if (connection != null) {
                        players.add(connection);
                    }
                });
                game.setGameOngoing(true);
                new GameServer(players, game).start();
            }
        }
    }
}
