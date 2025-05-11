package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.Slot;
import com.example.models.enums.Directions;
import com.example.models.enums.types.itemTypes.CropSeedsType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.types.itemTypes.TreeSeedsType;
import com.example.models.enums.types.mapObjectTypes.TreeType;
import com.example.models.enums.worldEnums.Season;
import com.example.models.items.Tool;
import com.example.models.items.TreeSeed;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Coordinate;
import com.example.models.mapObjects.Crop;
import com.example.models.mapObjects.EmptyCell;
import com.example.models.mapObjects.Tree;

public class Farming extends Controller {
    public static Response handleSeedPlanting(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
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
        Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
        Cell cell = player.getCurrentFarm(game).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
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
            cropSeedsType = cropSeedsType.getRandomCropSeedsType(game.getSeason());
        }
        boolean check = false;
        for (Season season : cropSeedsType.season) {
            if (season == game.getSeason()) {
                check = true;
                break;
            }
        }
        if (!check) {
            return new Response(false, "This crop can not be planted in this season");
        }
        Crop plant = new Crop(cropSeedsType, game.getDate());

        // x : [22,28] , y : [3,10]
        if (cropSeedsType.canBeGiant) {
            int arr[][] = player.getCurrentFarm(game).giantCropsTogether(cell);
            if (arr != null) {
                int s = 0;
                for (int i = 0; i < arr.length; i++) {
                    int x = arr[i][0] + cellCoordinate.getX();
                    int y = arr[i][1] + cellCoordinate.getY();
                    Cell c = player.getCurrentFarm(game).findCellByCoordinate(x, y);
                    s = Math.max(s, ((Crop) c.getObjectOnCell()).getStageNumber());
                    c.setObjectOnCell(new EmptyCell());
                }
                plant.setGiant(true);
                plant.setStageNumber(s);
            }
        }
        cell.setObjectOnCell(plant);
        GameRepository.saveGame(game);
        return new Response(true, "Planting was successful");
    }

    public static Response handleTreePlanting(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
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
        Cell cell = player.getCurrentFarm(game).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
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
            if (season == game.getSeason()) {
                check = true;
                break;
            }
        }
        if (!check) {
            return new Response(false, "This tree can not be planted in this season");
        }
        TreeType treeType = TreeType.findTreeTypeByName(seed);
        Tree tree = new Tree(treeType, game.getDate());
        cell.setObjectOnCell(tree);
        GameRepository.saveGame(game);
        return new Response(true, "Planting was successful");
    }

    public static Response handleShowPlant(Request request) {
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Cell cell = player.getCurrentFarm(game).findCellByCoordinate(x, y);
        if (cell == null) {
            return new Response(false, "Cell not found");
        }
        if (!cell.getObjectOnCell().type.equals("plant")) {
            return new Response(false, "Cell is not a plant");
        }
        Crop plant = (Crop) cell.getObjectOnCell();
        return new Response(true, plant.toString());
    }

    public static Response handleFertilization(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        String fertilizer = request.body.get("fertilizer");
        String dir = request.body.get("direction");
        Directions direction;
        try {
            direction = Directions.getDir(dir);
        } catch (Exception e) {
            return new Response(false, "Invalid direction");
        }
        Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
        Cell cell = player.getCurrentFarm(game).findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
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

        GameRepository.saveGame(game);
        return new Response(true, "Fertilization was successful");
    }

    public static Response handleHowMuchWater(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Tool wateringCan = player.getInventory().getWateringCan();
        return new Response(true, "You have " + wateringCan.getWaterReserve() + " water");
    }
}
