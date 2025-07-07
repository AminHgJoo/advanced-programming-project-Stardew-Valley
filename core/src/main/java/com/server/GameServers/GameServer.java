package com.server.GameServers;

import com.common.models.GameData;

import java.util.ArrayList;

public class GameServer extends Thread {
    private ArrayList<PlayerConnection> players;
    private GameData game;
    private boolean isRunning = true;

    public GameServer(ArrayList<PlayerConnection> players, GameData game) {
        this.players = players;
        this.game = game;
    }

    @Override
    public void run() {
        while (isRunning) {
            players.forEach(player ->
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
