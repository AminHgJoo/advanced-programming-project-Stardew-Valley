package com.client.services;

import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.items.Item;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.DroppedItem;
import com.common.models.mapObjects.EmptyCell;

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
    public boolean dropItem(Player player, Farm farm , Item item , Cell cell) {
        if(item != null ) {
            if(cell != null && cell.getObjectOnCell() instanceof EmptyCell) {
                player.setEquippedItem(null);
                for(Slot slot : player.getInventory().getSlots()) {
                    if(slot.getItem().getName().equals(item.getName())) {
                        player.getInventory().removeSlot(slot);
                        break;
                    }
                }
                cell.setObjectOnCell(new DroppedItem(1,item));
                return true;
            }
        }
        return false;
    }

}
