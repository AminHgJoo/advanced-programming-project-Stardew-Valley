package com.example.views;

import com.example.Repositories.GameRepository;
import com.example.models.Game;
import dev.morphia.annotations.Transient;
import dev.morphia.query.updates.UpdateOperators;

public class GameThread extends Thread {
    @Transient
    private Game game;
    public boolean keepRunning = false;

    public GameThread(Game game) {
        this.game = game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    //TODO: initialize thread
    //TODO: crop stuff.
    public void run() {

        while (keepRunning) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.advanceTime();
            game.checkSeasonChange();
        }
        //debug code
        System.out.println("Thread Exiting...");
    }
}
