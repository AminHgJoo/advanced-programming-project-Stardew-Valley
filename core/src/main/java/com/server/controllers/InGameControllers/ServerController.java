package com.server.controllers.InGameControllers;

import com.common.models.GameData;
import com.common.models.Player;
import com.server.GameServers.AppWebSocket;
import com.server.GameServers.GameServer;

import java.util.ArrayList;

public abstract class ServerController {
    private GameServer gs;

    public ServerController(GameServer gs) {
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

    public static Player getPlayer(GameData gameData, String playerId) {
        Player player = null;
        ArrayList<Player> players = gameData.getPlayers();

        for (Player p : players) {
            if (p.getUser_id().equals(playerId)) {
                player = p;
            }
        }
        return player;
    }
}
