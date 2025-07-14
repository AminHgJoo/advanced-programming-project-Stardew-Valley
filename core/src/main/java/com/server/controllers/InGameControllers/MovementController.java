package com.server.controllers.InGameControllers;


import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Farm;
import com.server.GameServers.GameServer;
import com.server.repositories.GameRepository;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class MovementController extends Controller {
    public void walk(Context ctx  , GameServer gs) {
        try {
            String id = ctx.attribute("id");
            String gameId = ctx.pathParam("gameId");

            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            float x = Float.parseFloat((String) body.get("x"));
            float y = Float.parseFloat((String) body.get("y"));

            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Farm farm = player.getCurrentFarm(game);
            Cell cell = farm.findCellByCoordinate(x , y);
            if(cell  == null){
                ctx.json(Response.BAD_REQUEST.setMessage("Cell not found"));
                return;
            }
            if(!cell.getObjectOnCell().isWalkable){
                ctx.json(Response.BAD_REQUEST.setMessage("Cell is not walkable"));
                return;
            }
            ctx.json(Response.OK.setMessage("Ok"));
            // broadcast

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
