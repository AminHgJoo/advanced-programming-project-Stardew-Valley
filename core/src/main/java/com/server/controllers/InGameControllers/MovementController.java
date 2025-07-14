package com.server.controllers.InGameControllers;


import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Farm;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class MovementController extends Controller {
    public void walk(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            String gameId = ctx.pathParam("gameId");

            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            double x = (Double) body.get("x");
            double y = (Double) body.get("y");
            System.out.println("x: " + x + " y: " + y);
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Farm farm = player.getCurrentFarm(game);
            x = x/32d;
            y = 49 - y/32d;
            Cell cell = farm.findCellByCoordinate((float) x,  (float) y);
            if (cell == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell not found"));
                return;
            }
            if (!cell.getObjectOnCell().isWalkable) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell is not walkable"));
                return;
            }
            ctx.json(Response.OK.setMessage("Ok"));
            // broadcast
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_MOVED");
            msg.put("player_user_id", id);
            msg.put("x", String.format("%f", x));
            msg.put("y", String.format("%f", y));
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
