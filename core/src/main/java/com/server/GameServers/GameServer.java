package com.server.GameServers;

import com.common.models.GameData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.server.controllers.InGameControllers.GameServerController;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer extends Thread {
    private ArrayList<PlayerConnection> playerConnections;
    private GameData game;
    private boolean isRunning = true;
    private final GameServerController controller = new GameServerController();
    private final Gson gson = new GsonBuilder()
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

    public GameServer(ArrayList<PlayerConnection> players, GameData game) {
        this.playerConnections = players;
        this.game = game;
        HashMap<String, String> res = new HashMap<>();
        res.put("type", "GAME_START");
        res.put("message", "game has been started successfully");
        res.put("game", this.gson.toJson(game));
        for (PlayerConnection playerConnection : players) {
            playerConnection.send(new Gson().toJson(res));
        }
    }

    public void broadcast(HashMap<String, String> message) {
        AppWebSocket.broadcast(game, message);
    }

    public void narrowCast(String username, HashMap<String, String> message) {
        AppWebSocket.narrowcast(username, message);
    }

    @Override
    public void run() {
        while (isRunning) {
           /* TODO sending game updates
           broadcast(state...);
           * */
            HashMap<String, String> message = new HashMap<>();
            message.put("type", "TEST");
            broadcast(message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void handleRequests(Context ctx) {
        if (ctx.method() == HandlerType.POST) {
            controller.routingTheRequests(ctx);
        } else if (ctx.method() == HandlerType.GET) {

        }
    }

    public void endGame() {
        isRunning = false;
    }

    public GameData getGame() {
        return game;
    }
}
