package com.server.GameServers;

import com.common.models.GameData;
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
    private final Javalin app;
    private static ConcurrentHashMap<String, PlayerConnection> connectedPlayers = new ConcurrentHashMap<>();
    private static CopyOnWriteArrayList<GameServer> activeGames = new CopyOnWriteArrayList<>();

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
                    if (command.equals("INVITE")) {
                        handleInvite(message, ctx);
                    } else if (command.equals("ACCEPT")) {
                        handleAccept(message, ctx);
                    } else if (command.equals("CHOOSE_FARM")) {
                        chooseFarm(user, message, ctx);
                    } else if (command.equals("JOIN_LOBBY")) {
                        joinLobby(user, message, ctx);
                    }
                }
            });

            ws.onClose(ctx -> {
                System.out.println("Disconnected");
                String username = ctx.queryParam("playerUsername");
                connectedPlayers.remove(username);
            });

        });
    }

    public static void chooseFarm(User user, JSONObject obj, WsMessageContext ctx) {
        Lobby lobby = LobbyRepository.findById(user.getCurrentLobbyId());
        if (lobby != null) {
            lobby.getUsersFarm().put(user.getUsername(), obj.getInt("farm"));
            LobbyRepository.save(lobby);
            HashMap<String, String> response = new HashMap<>();
            response.put("type", "FARM_CHOSEN");
            response.put("message", "User" + user.getUsername() + " chose farm" + obj.getInt("farm"));
            response.put("lobby", new Gson().toJson(lobby));
            sendMessageToLobby(lobby, user, new Gson().toJson(response));
        }
    }

    public static void joinLobby(User user, JSONObject obj, WsMessageContext ctx) {
        Lobby lobby = LobbyRepository.findById(obj.getString("lobbyId"));
        HashMap<String, String> response = new HashMap<>();
        if (lobby != null) {
            if (lobby.getUsers().size() >= 4) {
                response.put("type", "RESPONSE");
                response.put("success", "false");
                response.put("message", "Lobby is full");
                ctx.send(new Gson().toJson(response));
                return;
            }
            if (!lobby.isPublic()) {
                String password = obj.getString("password");
                if (password == null || !lobby.getPassword().equals(password)) {
                    response.put("type", "RESPONSE");
                    response.put("success", "false");
                    response.put("message", "Lobby password does not match");
                    ctx.send(new Gson().toJson(response));
                    return;
                }
            }
            user.setCurrentLobbyId(lobby.get_id().toString());
            lobby.getUsers().add(user.getUsername());
            LobbyRepository.save(lobby);
            response.put("type", "RESPONSE");
            response.put("success", "true");
            response.put("message", "User" + user.getUsername() + " joined lobby");
            ctx.send(new Gson().toJson(response));
            response.remove("success");
            response.put("type", "LOBBY_JOINED");
            sendMessageToLobby(lobby, user, new Gson().toJson(response));
        }
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
