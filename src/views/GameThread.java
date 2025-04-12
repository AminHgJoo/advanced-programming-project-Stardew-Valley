package views;

import models.Game;

public class GameThread extends Thread {
    private final Game game;

    public GameThread(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

    }
}
