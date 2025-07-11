package com.server.GameServers;

import com.common.models.GameData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class GameServer extends Thread {
    private ArrayList<PlayerConnection> playerConnections;
    private GameData game;
    private boolean isRunning = true;

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

    @Override
    public void run() {
        while (isRunning) {
            playerConnections.forEach(player ->
                player.send("Game update")
            );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void endGame() {
        isRunning = false;
    }
}
