package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.Backpack;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.EmptyCell;
import com.common.models.mapObjects.Water;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import groovyjarjarasm.asm.Handle;
import io.javalin.http.Context;

import java.util.HashMap;

public class WorldController extends Controller {
    public void buildGreenhouse(Context ctx, GameServer gs) {
        try {
            String id = ctx.pathParam("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Farm farm = player.getFarm();
            Backpack backpack = player.getInventory();
            Cell testCell = Farm.getCellByCoordinate(25, 4, farm.getCells());
            if (testCell.getObjectOnCell() instanceof Water) {
                ctx.json(Response.BAD_REQUEST.setMessage("You are already built."));
                return;
            }
            Slot slot = backpack.getSlotByItemName(MiscType.WOOD.name);

            if (slot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have any wood."));
                return;
            }

            if (slot.getCount() < 500) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have any wood."));
                return;
            }

            if (player.getMoney(game) < 1000) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have enough money."));
                return;
            }

            slot.setCount(slot.getCount() - 500);

            if (slot.getCount() == 0) {
                backpack.getSlots().remove(slot);
            }

            player.setMoney(player.getMoney(game) - 1000, game);

            for (int i = 23; i < 28; i++) {
                for (int j = 4; j < 10; j++) {
                    Cell cell = Farm.getCellByCoordinate(i, j, farm.getCells());
                    cell.setObjectOnCell(new EmptyCell());
                }
            }

            Cell cell = Farm.getCellByCoordinate(25, 10, farm.getCells());
            cell.setObjectOnCell(new EmptyCell());

            Cell cell1 = Farm.getCellByCoordinate(25, 4, farm.getCells());
            cell1.setObjectOnCell(new Water());

            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(playerJson));
            HashMap<String , String> msg = new HashMap<>();
            msg.put("type" , "GREENHOUSE_BUILD");
            msg.put("player_user_id" , id);
            msg.put("player" , playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
