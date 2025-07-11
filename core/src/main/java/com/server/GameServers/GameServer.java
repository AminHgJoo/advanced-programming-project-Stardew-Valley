package com.server.GameServers;

import com.common.models.GameData;
import com.google.gson.Gson;
import com.server.controllers.InGameControllers.GameServerController;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer extends Thread {
    private ArrayList<PlayerConnection> playerConnections;
    private GameData game;
    private boolean isRunning = true;
    private final GameServerController controller = new GameServerController();

    public GameServer(ArrayList<PlayerConnection> players, GameData game) {
        this.playerConnections = players;
        this.game = game;
        HashMap<String, String> res = new HashMap<>();
        res.put("type", "GAME_START");
        res.put("message", "game has been started successfully");
        res.put("game", new Gson().toJson(game));
        for (PlayerConnection playerConnection : players) {
            playerConnection.send(new Gson().toJson(res));
        }
    }

    public void broadcast(HashMap<String, String> message) {
        for (PlayerConnection playerConnection : playerConnections) {
            playerConnection.send(new Gson().toJson(message));
        }
    }

    public void narrowCast(String username, HashMap<String, String> message) {
        for (PlayerConnection playerConnection : playerConnections) {
            if (playerConnection.getUsername().equals(username)) {
                playerConnection.send(new Gson().toJson(message));
            }
        }
    }

    @Override
    public void run() {
        while (isRunning) {
           /* TODO sending game updates
           broadcast(state...);
           * */
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
