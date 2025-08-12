package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.Directions;
import com.common.models.enums.types.itemTypes.CropSeedsType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.enums.types.itemTypes.TreeSeedsType;
import com.common.models.enums.types.mapObjectTypes.TreeType;
import com.common.models.enums.worldEnums.Season;
import com.common.models.items.TreeSeed;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapObjects.Crop;
import com.common.models.mapObjects.EmptyCell;
import com.common.models.mapObjects.Tree;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class FarmingController extends ServerController {
    public FarmingController(GameServer gs) {
        super(gs);
    }

    public void seedPlanting(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String seed = (String) body.get("seed");
            String dir = (String) body.get("direction");
            String id = ctx.attribute("id");
            double x1 = (Double) body.get("x");
            double y1 = (Double) body.get("y");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            CropSeedsType cropSeedsType = CropSeedsType.findCropBySeed(seed);
            if (cropSeedsType == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("crop not found"));
                return;
            }
            float[] dxAndDy = WorldController.getXAndYIncrement(dir);
            float dx = dxAndDy[0];
            float dy = dxAndDy[1];
            player.setCoordinate(new Coordinate((float) x1, (float) y1));
            float playerX = (player.getCoordinate().getX() + dx) / 32;
            float playerY = 50 - (player.getCoordinate().getY() + dy) / 32;
            Cell cell = player.getCurrentFarm(game).findCellByCoordinate(playerX, playerY);
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
            boolean greenhouseCheck = playerX >= 22
                && playerX <= 28
                && playerY >= 3
                && playerY <= 10;
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
                        float x = arr[i][0] + playerX;
                        float y = arr[i][1] + playerY;
                        Cell c = player.getCurrentFarm(game).findCellByCoordinate(x, y);
                        s = Math.max(s, ((Crop) c.getObjectOnCell()).getStageNumber());
                        c.setObjectOnCell(new EmptyCell());
                    }
                    plant.setGiant(true);
                    plant.setStageNumber(s);
                }
            }
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setMessage("Planted crop").setBody(playerJson));

            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void treePlanting(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String seed = (String) body.get("seed");
            String dir = (String) body.get("direction");
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            TreeSeedsType treeSeedsType = TreeSeedsType.findTreeTypeByName(seed);
            Directions direction;
            try {
                direction = Directions.getDir(dir);
            } catch (Exception e) {
                ctx.json(Response.BAD_REQUEST.setMessage("Invalid direction"));
                return;
            }
            if (direction == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Invalid direction"));
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
            Slot playerSeedSlot = player.getInventory().findTreeSeedByItemName(seed);
            playerSeedSlot.setCount(playerSeedSlot.getCount() - 1);
            if (playerSeedSlot.getCount() <= 0) {
                player.getInventory().getSlots().remove(playerSeedSlot);
            }
            TreeSeed treeSeed = (TreeSeed) playerSeedSlot.getItem();
            boolean check = false;
            for (Season season : treeSeed.getTreeSeedsType().growthSeasons) {
                if (season == game.getSeason()) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                ctx.json(Response.BAD_REQUEST.setMessage("Season not found"));
                return;
            }
            TreeType treeType = TreeType.findTreeTypeByName(seed);
            Tree tree = new Tree(treeType, game.getDate(), game);
            cell.setObjectOnCell(tree);
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setMessage("Tree planted").setBody(playerJson));

            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void fertilization(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String dir = (String) body.get("direction");
            String fertilizer = (String) body.get("fertilizer");
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Directions direction;
            try {
                direction = Directions.getDir(dir);
            } catch (Exception e) {
                ctx.json(Response.BAD_REQUEST.setMessage("Invalid direction"));
                return;
            }
            if (direction == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Invalid direction"));
                return;
            }
            Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
            Cell cell = player.getCurrentFarm(game).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
            if (cell == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell not found"));
                return;
            }
            if (!cell.getObjectOnCell().type.equals("plant")) {
                ctx.json(Response.BAD_REQUEST.setMessage("Cell is not plant"));
                return;
            }
            Slot miscSlot = player.getInventory().getSlotByItemName(fertilizer);
            if (miscSlot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Fertilizer not found"));
                return;
            }
            miscSlot.setCount(miscSlot.getCount() - 1);
            if (miscSlot.getCount() <= 0) {
                player.getInventory().getSlots().remove(miscSlot);
            }
            Crop crop = (Crop) cell.getObjectOnCell();
            if (fertilizer.compareToIgnoreCase(MiscType.BASIC_FERTILIZER.name) == 0) {
                crop.pushBackDeadlines(-1);
            } else if (fertilizer.compareToIgnoreCase(MiscType.QUALITY_FERTILIZER.name) == 0) {
                crop.setHasBeenDeluxeFertilized(true);
            }
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setMessage("Tree planted").setBody(playerJson));

            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
