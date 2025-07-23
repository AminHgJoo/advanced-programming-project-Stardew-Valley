package com.server.GameServers;

import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.User;
import com.common.models.networking.Lobby;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.server.repositories.UserRepository;
import io.javalin.Javalin;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

//TODO handle players DC

public class AppWebSocket {
    private final static Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
            @Override
            public void write(JsonWriter out, LocalDateTime value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public LocalDateTime read(JsonReader in) throws IOException {
                return LocalDateTime.parse(in.nextString());
            }
        })
        .serializeSpecialFloatingPointValues()
        .create();
    private static ConcurrentHashMap<String, PlayerConnection> connectedPlayers = new ConcurrentHashMap<>();
    private static CopyOnWriteArrayList<GameServer> activeGames = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<LoadGameServer> loadGameServers = new CopyOnWriteArrayList<>();
    private final Javalin app;

    public AppWebSocket(Javalin app) {
        this.app = app;
    }

    public static ConcurrentHashMap<String, PlayerConnection> getConnectedPlayers() {
        return connectedPlayers;
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

    public static void broadcast(GameData game, HashMap<String, String> msg) {
        for (Player p : game.getPlayers()) {
            PlayerConnection pc = connectedPlayers.get(p.getUser().getUsername());
            if (pc != null) {
                pc.send(gson.toJson(msg));
            }
        }
    }

    public static void narrowcast(String username, HashMap<String, String> msg) {
        PlayerConnection pc = connectedPlayers.get(username);
        if (pc != null) {
            pc.send(gson.toJson(msg));
        }
    }

    public static boolean startGame(GameData game, Lobby lobby) {
        ArrayList<PlayerConnection> arr = new ArrayList<>();
        for (String p : lobby.getUsers()) {
            PlayerConnection connection = connectedPlayers.get(p);
            if (connection != null) {
                arr.add(connection);
            } else {
                return false;
            }
        }
        GameServer gs = new GameServer(arr, game);
        activeGames.add(gs);
        gs.start();
        return true;
    }

    public static void startOldGame(GameData game, ArrayList<String> usernames) {
        ArrayList<PlayerConnection> arr = new ArrayList<>();
        for (String p : usernames) {
            PlayerConnection connection = connectedPlayers.get(p);
            if (connection != null) {
                arr.add(connection);
            } else {
            }
        }
        GameServer gs = new GameServer(arr, game);
        activeGames.add(gs);
        gs.start();
    }

    public static void loadGame(GameData game, User user) {
        ArrayList<PlayerConnection> arr = new ArrayList<>();
        arr.add(connectedPlayers.get(user.getUsername()));
        LoadGameServer loadGameServer = new LoadGameServer(game, user.get_id(), arr);
        loadGameServers.add(loadGameServer);
        loadGameServer.start();
    }

    public static GameServer getActiveGameById(String gameId) {
        for (GameServer gs : activeGames) {
            if (gs.getGame().get_id().toString().equals(gameId)) {
                return gs;
            }
        }
        return null;
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
