package com.server.controllers.InGameControllers;

import com.server.GameServers.GameServer;

public abstract class Controller {
    private GameServer gs;

    public GameServer getGs() {
        return gs;
    }

    public void setGs(GameServer gs) {
        this.gs = gs;
    }
}
