package com.server.GameServers;

import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.networking.Lobby;
import com.google.gson.Gson;
import com.server.controllers.InGameControllers.GameServerController;
import com.server.repositories.LobbyRepository;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

import java.util.ArrayList;
import java.util.HashMap;

public class GameServer extends Thread {
    private final GameServerController controller = new GameServerController();
    private final Gson gson = GameGSON.gson;
    private ArrayList<PlayerConnection> playerConnections;
    private GameData game;
    private boolean isRunning = true;
    private int count = 0;

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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            if(count == 7){
                count = 0;
                game.advanceTime();
            }
            String gameJson = this.gson.toJson(game);
            HashMap<String, String> message = new HashMap<>();
            message.put("type", "GAME_UPDATED");
            message.put("game" , gameJson);
            broadcast(message);
        }
    }

    public void handleRequests(Context ctx) {
        if (ctx.method() == HandlerType.POST) {
            controller.routingTheRequests(ctx, this);
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
