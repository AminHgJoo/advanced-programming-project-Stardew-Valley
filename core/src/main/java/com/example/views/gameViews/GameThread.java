package com.example.views.gameViews;

import com.example.Repositories.GameRepository;
import com.example.models.GameData;
import dev.morphia.annotations.Transient;

public class GameThread extends Thread {
    @Transient
    private GameData gameData;
    public boolean keepRunning = false;

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
