package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.Directions;
import com.common.models.enums.types.itemTypes.CropSeedsType;
import com.common.models.enums.worldEnums.Season;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.Crop;
import com.common.models.mapObjects.EmptyCell;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class FarmingController extends Controller {
    public void seedPlanting(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String seed = (String) body.get("seed");
            String dir = (String) body.get("direction");
            String id = ctx.pathParam("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            CropSeedsType cropSeedsType = CropSeedsType.findCropBySeed(seed);
            if (cropSeedsType == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("crop not found"));
                return;
            }
            Directions direction;
            try {
                direction = Directions.getDir(dir);
            } catch (Exception e) {
                ctx.json(Response.BAD_REQUEST.setMessage("Invalid direction: " + dir));
                return;
            }
            if (direction == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Invalid direction: " + dir));
                return;
            }
            Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
            Cell cell = player.getCurrentFarm(game).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
            if (cell == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell not found"));
                return;
            }
            if (!cell.isTilled()) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell is not tilled"));
                return;
            }
            if (!cell.getObjectOnCell().type.equals("empty")) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell is not empty"));
                return;
            }
            Slot playerSeedSlot = player.getInventory().findSeedByItemName(seed);
            if (playerSeedSlot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Seed not found in player inventory"));
                return;
            }
            playerSeedSlot.setCount(playerSeedSlot.getCount() - 1);
            if (playerSeedSlot.getCount() <= 0) {
                player.getInventory().getSlots().remove(playerSeedSlot);
            }
            if (cropSeedsType == CropSeedsType.RANDOM_CROP) {
                cropSeedsType = cropSeedsType.getRandomCropSeedsType(game.getSeason());
            }
            boolean check = false;
            for (Season season : cropSeedsType.season) {
                if (season == game.getSeason()) {
                    check = true;
                    break;
                }
            }
            boolean greenhouseCheck = cellCoordinate.getX() >= 22
                && cellCoordinate.getX() <= 28
                && cellCoordinate.getY() >= 3
                && cellCoordinate.getY() <= 10;
            if (!check && !greenhouseCheck) {
                ctx.json(Response.BAD_REQUEST.setMessage("This crop can not be planted in this season"));
                return;
            }
            Crop plant = new Crop(cropSeedsType, game.getDate());
            cell.setObjectOnCell(plant);
            if (cropSeedsType.canBeGiant && !greenhouseCheck) {
                int arr[][] = player.getCurrentFarm(game).giantCropsTogether(cell);
                if (arr != null) {
                    int s = 0;
                    for (int i = 0; i < arr.length; i++) {
                        float x = arr[i][0] + cellCoordinate.getX();
                        float y = arr[i][1] + cellCoordinate.getY();
                        Cell c = player.getCurrentFarm(game).findCellByCoordinate(x, y);
                        s = Math.max(s, ((Crop) c.getObjectOnCell()).getStageNumber());
                        c.setObjectOnCell(new EmptyCell());
                    }
                    plant.setGiant(true);
                    plant.setStageNumber(s);
                }
            }
            String farmJson = GameGSON.gson.toJson(player.getCurrentFarm(game));
            ctx.json(Response.OK.setMessage("Planted crop").setBody(farmJson));

            HashMap<String ,String> msg = new HashMap<>();
            msg.put("type" , "SEED_PLANTED");
            msg.put("player_user_id", id);
            msg.put("farm" , farmJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
