package com.example.views;

import com.example.models.Game;

public class GameThread extends Thread {
    private final Game game;

    public GameThread(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

    }
}
