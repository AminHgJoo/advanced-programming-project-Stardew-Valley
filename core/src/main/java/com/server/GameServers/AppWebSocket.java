package com.server.GameServers;

import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.User;
import com.common.models.networking.Lobby;
import com.google.gson.Gson;
import com.server.repositories.GameRepository;
import com.server.repositories.LobbyRepository;
import com.server.repositories.UserRepository;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class AppWebSocket {
    private static ConcurrentHashMap<String, PlayerConnection> connectedPlayers = new ConcurrentHashMap<>();
    private static CopyOnWriteArrayList<GameServer> activeGames = new CopyOnWriteArrayList<>();
    private final Javalin app;

    public AppWebSocket(Javalin app) {
        this.app = app;
    }

    public static void sendMessageToLobby(Lobby lobby, User user, String message) {
        for (String username : lobby.getUsers()) {
            if (!username.equals(user.getUsername())) {
                PlayerConnection connection = connectedPlayers.get(username);
                if (connection != null) {
                    connection.send(message);
                }
            }
        }
    }

    public static void startGame(GameData game , Lobby lobby) {
        ArrayList<PlayerConnection> arr = new ArrayList<>();
        for(String p: lobby.getUsers()){
            PlayerConnection connection = connectedPlayers.get(p);
            if (connection != null) {
                arr.add(connection);
            }
        }
        new GameServer(arr, game).start();
    }

    public void start() {
        app.ws("/game", ws -> {
            ws.onConnect(ctx -> {
                ctx.enableAutomaticPings(5, TimeUnit.SECONDS);
                System.out.println("Someone connected");
                String username = ctx.queryParam("playerUsername");
                connectedPlayers.put(username, new PlayerConnection(username, ctx));
                ctx.send("Connected as " + username);
            });

            ws.onMessage(ctx -> {
                String username = ctx.queryParam("playerUsername");
                User user = UserRepository.findUserByUsername(username);
                if (user != null) {
                    JSONObject message = new JSONObject(ctx.message());
                    String command = message.getString("command");

                }
            });

            ws.onClose(ctx -> {
                System.out.println("Disconnected");
                String username = ctx.queryParam("playerUsername");
                connectedPlayers.remove(username);
            });

        });
    }
}
