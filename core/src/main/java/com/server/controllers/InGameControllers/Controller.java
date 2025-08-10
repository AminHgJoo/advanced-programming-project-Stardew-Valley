package com.server.controllers.InGameControllers;

import com.server.GameServers.AppWebSocket;
import com.server.GameServers.GameServer;

public abstract class Controller {
    private GameServer gs;

    public Controller(GameServer gs) {
        this.gs = gs;
    }

    public GameServer getGs(String gameId) {
        if (gs == null) {
            GameServer gs = AppWebSocket.getActiveGameById(gameId);
            this.gs = gs;
        }
        return gs;
    }

    public void setGs(GameServer gs) {
        this.gs = gs;
    }
}
