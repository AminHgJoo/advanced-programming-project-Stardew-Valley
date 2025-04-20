package com.example.views;

import com.example.models.Game;
import dev.morphia.annotations.Transient;

public class GameThread extends Thread {
    @Transient
    private final Game game;

    public GameThread(Game game) {
        this.game = game;
    }

    //TODO: initialize thread
    //TODO: crop stuff.
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
