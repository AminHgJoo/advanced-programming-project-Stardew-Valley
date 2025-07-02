package com.server.views.gameViews;

import com.common.models.GameData;
import com.server.repositories.GameRepository;
import dev.morphia.annotations.Transient;

public class GameThread extends Thread {
    public boolean keepRunning = false;
    @Transient
    private GameData gameData;

    public GameThread(GameData gameData) {
        this.gameData = gameData;
    }

    public void setGame(GameData gameData) {
        this.gameData = gameData;
    }

    public void run() {
        while (keepRunning) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (gameData.hasTurnCycleFinished) {
                gameData.advanceTime();
                GameRepository.saveGame(gameData);
            }
            boolean check = gameData.checkSeasonChange();

            if (check) {
                GameRepository.saveGame(gameData);
            }

            gameData.checkForRecipeUnlocking();
            gameData.handleBuffExpiration();
            gameData.checkForSkillUpgrades();

            GameRepository.saveGame(gameData);
        }
        //debug code
        System.out.println("Thread Exiting...");
    }

}
