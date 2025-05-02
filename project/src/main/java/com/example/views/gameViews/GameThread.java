package com.example.views.gameViews;

import com.example.Repositories.GameRepository;
import com.example.models.Game;
import com.example.models.mapModels.Farm;
import dev.morphia.annotations.Transient;

import java.util.ArrayList;

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

    //TODO: crop stuff.
    public void run() {
        while (keepRunning) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (game.hasTurnCycleFinished) {
                game.advanceTime();
                GameRepository.saveGame(game);
            }
            boolean check = game.checkSeasonChange();

            if (check) {
                GameRepository.saveGame(game);
            }

            game.checkForRecipeUnlocking();
            game.handleBuffExpiration();
            game.checkForSkillUpgrades();
            game.checkForCropNextStage();
            game.reInitializeStoreProductsCount();
            game.reInitializeNpc();
            game.addPlayersMoney();

            GameRepository.saveGame(game);
        }
        //debug code
        System.out.println("Thread Exiting...");
    }

    public void handleRefreshForaging() {
        ArrayList<Farm> allFarms = game.getMap().getFarms();
        for (Farm farm : allFarms) {
            farm.foragingRefresh();
        }
    }
}
