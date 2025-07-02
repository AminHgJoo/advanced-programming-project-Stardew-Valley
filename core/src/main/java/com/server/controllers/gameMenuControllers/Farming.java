package com.server.controllers.gameMenuControllers;

import com.common.repositories.GameRepository;
import com.server.controllers.Controller;
import com.common.models.App;
import com.common.models.GameData;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.Directions;
import com.common.models.enums.types.itemTypes.CropSeedsType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.enums.types.itemTypes.TreeSeedsType;
import com.common.models.enums.types.mapObjectTypes.TreeType;
import com.common.models.enums.worldEnums.Season;
import com.common.models.items.Tool;
import com.common.models.items.TreeSeed;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapObjects.Crop;
import com.common.models.mapObjects.EmptyCell;
import com.common.models.mapObjects.Tree;

public class Farming extends Controller {
    public static Response handleSeedPlanting(Request request) {
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        String seed = request.body.get("seed");
        CropSeedsType cropSeedsType = CropSeedsType.findCropBySeed(seed);
        if (cropSeedsType == null) {
            return new Response(false, "crop not found");
        }
        String dir = request.body.get("direction");
        Directions direction;
        try {
            direction = Directions.getDir(dir);
        } catch (Exception e) {
            return new Response(false, "Invalid direction");
        }
        if(direction == null){
            return new Response(false, "Invalid direction");
        }
        Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
        Cell cell = player.getCurrentFarm(gameData).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
        if (cell == null) {
            return new Response(false, "Cell not found");
        }
        if (!cell.isTilled()) {
            return new Response(false, "Cell is not tilled");
        }
        if (!cell.getObjectOnCell().type.equals("empty")) {
            return new Response(false, "Cell is not empty");
        }
        Slot playerSeedSlot = player.getInventory().findSeedByItemName(seed);
        if (playerSeedSlot == null) {
            return new Response(false, "Seed not found in player inventory");
        }
        playerSeedSlot.setCount(playerSeedSlot.getCount() - 1);
        if (playerSeedSlot.getCount() <= 0) {
            player.getInventory().getSlots().remove(playerSeedSlot);
        }
        if (cropSeedsType == CropSeedsType.RANDOM_CROP) {
            cropSeedsType = cropSeedsType.getRandomCropSeedsType(gameData.getSeason());
        }
        boolean check = false;
        for (Season season : cropSeedsType.season) {
            if (season == gameData.getSeason()) {
                check = true;
                break;
            }
        }
        boolean greenhouseCheck = cellCoordinate.getX() >= 22
                && cellCoordinate.getX() <= 28
                && cellCoordinate.getY() >= 3
                && cellCoordinate.getY() <= 10;

        if (!check && !greenhouseCheck) {
            return new Response(false, "This crop can not be planted in this season");
        }
        Crop plant = new Crop(cropSeedsType, gameData.getDate());

        //Greenhouse coords: x : [22,28] , y : [3,10]
        cell.setObjectOnCell(plant);
        if (cropSeedsType.canBeGiant && !greenhouseCheck) {
            int arr[][] = player.getCurrentFarm(gameData).giantCropsTogether(cell);
            if (arr != null) {
                int s = 0;
                for (int i = 0; i < arr.length; i++) {
                    int x = arr[i][0] + cellCoordinate.getX();
                    int y = arr[i][1] + cellCoordinate.getY();
                    Cell c = player.getCurrentFarm(gameData).findCellByCoordinate(x, y);
                    s = Math.max(s, ((Crop) c.getObjectOnCell()).getStageNumber());
                    c.setObjectOnCell(new EmptyCell());
                }
                plant.setGiant(true);
                plant.setStageNumber(s);
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(true, "Planting was successful");
    }

    public static Response handleTreePlanting(Request request) {
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        String seed = request.body.get("seed");
        TreeSeedsType treeSeedsType = TreeSeedsType.findTreeTypeByName(seed);
        if (treeSeedsType == null) {
            return new Response(false, "Tree seed not found");
        }
        String dir = request.body.get("direction");
        Directions direction;
        try {
            direction = Directions.getDir(dir);
        } catch (Exception e) {
            return new Response(false, "Invalid direction");
        }
        Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
        Cell cell = player.getCurrentFarm(gameData).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
        if (cell == null) {
            return new Response(false, "Cell not found");
        }
        if (!cell.isTilled()) {
            return new Response(false, "Cell is not tilled");
        }
        if (!cell.getObjectOnCell().type.equals("empty")) {
            return new Response(false, "Cell is not empty");
        }
        Slot playerSeedSlot = player.getInventory().findTreeSeedByItemName(seed);
        playerSeedSlot.setCount(playerSeedSlot.getCount() - 1);
        if (playerSeedSlot.getCount() <= 0) {
            player.getInventory().getSlots().remove(playerSeedSlot);
        }
        TreeSeed treeSeed = (TreeSeed) playerSeedSlot.getItem();
        boolean check = false;
        for (Season season : treeSeed.getTreeSeedsType().growthSeasons) {
            if (season == gameData.getSeason()) {
                check = true;
                break;
            }
        }
        if (!check) {
            return new Response(false, "This tree can not be planted in this season");
        }
        TreeType treeType = TreeType.findTreeTypeByName(seed);
        Tree tree = new Tree(treeType, gameData.getDate());
        cell.setObjectOnCell(tree);
        GameRepository.saveGame(gameData);
        return new Response(true, "Planting was successful");
    }

    public static Response handleShowPlant(Request request) {
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Cell cell = player.getCurrentFarm(gameData).findCellByCoordinate(x, y);
        if (cell == null) {
            return new Response(false, "Cell not found");
        }
        if (!cell.getObjectOnCell().type.equals("plant")) {
            return new Response(false, "Cell is not a plant");
        }
        Crop plant = (Crop) cell.getObjectOnCell();
        return new Response(true, plant.cropDesc());
    }

    public static Response handleFertilization(Request request) {
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        String fertilizer = request.body.get("fertilizer");
        String dir = request.body.get("direction");
        Directions direction;
        try {
            direction = Directions.getDir(dir);
        } catch (Exception e) {
            return new Response(false, "Invalid direction");
        }
        Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
        Cell cell = player.getCurrentFarm(gameData).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
        if (cell == null) {
            return new Response(false, "Cell not found");
        }
        if (!cell.getObjectOnCell().type.equals("plant")) {
            return new Response(false, "Cell is not a plant");
        }
        Slot miscSlot = player.getInventory().getSlotByItemName(fertilizer);
        if (miscSlot == null) {
            return new Response(false, "Fertilizer not found");
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

        GameRepository.saveGame(gameData);
        return new Response(true, "Fertilization was successful");
    }

    public static Response handleHowMuchWater(Request request) {
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Tool wateringCan = player.getInventory().getWateringCan();
        return new Response(true, "You have " + wateringCan.getWaterReserve() + " water");
    }
}
