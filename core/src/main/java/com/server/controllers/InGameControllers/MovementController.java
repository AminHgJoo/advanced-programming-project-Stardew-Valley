package com.server.controllers.InGameControllers;


import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.server.GameServers.GameServer;
import com.server.repositories.GameRepository;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MovementController extends Controller {
    public void walk(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");

            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            double x = (Double) body.get("x");
            double y = (Double) body.get("y");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Farm farm = player.getCurrentFarm(game);
            x = x / 32d;
            y = 49 - y / 32d;
            Cell cell = farm.findCellByCoordinate((float) x, (float) y);
            if (cell == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell not found"));
                return;
            }
            if (!cell.getObjectOnCell().isWalkable) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell is not walkable " + cell.getObjectOnCell().type));
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

    public void goToVillage(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            if (!player.isInVillage()) {
                player.setInVillage(true);
                ctx.json(Response.OK.setMessage("Player is in Village"));
                HashMap<String, String> msg = new HashMap<>();
                msg.put("type", "PLAYER_GO_TO_VILLAGE");
                msg.put("player_user_id", id);
                gs.broadcast(msg);
                return;
            }
            ctx.json(Response.OK.setMessage("Player is in Village"));

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void goToPartnerFarm(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            GameData game = gs.getGame();

            Player player = game.findPlayerByUserId(id);
            Player partner = game.getPartner(player);

            if (partner == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Partner not found"));
                return;
            }

            Farm playerFarm = player.getCurrentFarm(game);
            Farm partnerFarm = partner.getFarm();
            if (playerFarm == partnerFarm) {
                ctx.json(Response.BAD_REQUEST.setMessage("you are already in the farm"));
                return;
            }
            player.setInVillage(false);
            player.setCurrentFarmNumber(partnerFarm.getFarmNumber());
            Coordinate coordinate = getEmptyCoordinate(player, partner, partnerFarm.getCells());
            if (coordinate == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("no empty cell found"));
                return;
            }
            player.setCoordinate(coordinate);

            ctx.json(Response.OK.setMessage("Ok")
                .setBody("{\"x\" : " + coordinate.getX() + "\"y\" : " + coordinate.getY() + "}"));

            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_GO_TO_PARTNER");
            msg.put("player_user_id", id);
            msg.put("x", String.format("%f", coordinate.getX()));
            msg.put("y", String.format("%f", coordinate.getY()));
            gs.broadcast(msg);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void walkHome(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            GameData game = gs.getGame();

            Player player = game.findPlayerByUserId(id);
            Farm playerFarm = player.getFarm();
            player.setInVillage(false);
            Coordinate coordinate = new Coordinate(0, 0);
            player.setCoordinate(coordinate);
            ctx.json(Response.OK.setMessage("You Are In Home !! ").setBody("{\"x\" : " + coordinate.getX() + "\"y\" : " + coordinate.getY() + "}"));

            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_GO_TO_HOME");
            msg.put("player_user_id", id);
            msg.put("x", String.format("%f", coordinate.getX()));
            msg.put("y", String.format("%f", coordinate.getY()));

            gs.broadcast(msg);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    private static Coordinate getEmptyCoordinate(Player player, Player partner, ArrayList<Cell> cells) {
        for (int i = 60; i >= 0; i--) {
            for (int j = 8; j <= 40; j++) {
                if (Objects.requireNonNull(Farm.getCellByCoordinate(i, j, cells)).getObjectOnCell().isWalkable) {
                    if (partner == null || (partner != null && !(partner.getCoordinate().getX() == i && partner.getCoordinate().getY() == j))) {
                        return new Coordinate(i, j);
                    }
                }
            }
        }
        return null;
    }
}
