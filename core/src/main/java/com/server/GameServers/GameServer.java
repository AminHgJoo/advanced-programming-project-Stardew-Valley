package com.server.GameServers;

import java.util.ArrayList;

public class GameServer extends Thread {
    private ArrayList<PlayerConnection> players;
    private boolean isRunning = true;

    public GameServer(ArrayList<PlayerConnection> players) {
        this.players = players;
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
