package com.example.views;

import com.example.models.App;
import com.example.models.Game;

public class GameThread extends Thread {
    private final Game game;

    public GameThread(Game game) {
        this.game = game;
    }

    //TODO: initialize thread
    @Override
    public void run() {
        while (game.isGameOngoing()) {
            try {
                this.wait(100);
            } catch (InterruptedException _) {
            }
            game.advanceTime();
            game.checkSeasonChange();
        }
    }
}
