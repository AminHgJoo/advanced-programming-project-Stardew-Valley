package com.client.services;

import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.mapModels.Cell;

public class PlayerService {
    private final Player player;
    private final GameData game;

    public PlayerService(Player player, GameData game) {
        this.player = player;
        this.game = game;
    }

    public boolean walk(float x, float y) {
        x = x / 32;
        y = 49 - y / 32;
        Cell c = player.getFarm().findCellByCoordinate(x, y);
        if (c == null) {
            return false;
        }
        if (!c.getObjectOnCell().isWalkable) {
            return false;
        }
        return true;
    }

}
