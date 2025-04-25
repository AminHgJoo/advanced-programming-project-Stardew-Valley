package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.enums.Directions;
import com.example.models.enums.types.CropSeedsType;
import com.example.models.items.Seed;
import com.example.models.items.Tool;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Coordinate;
import com.example.models.mapObjects.Crop;

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
            direction = Directions.valueOf(dir);
        } catch (Exception e) {
            return new Response(false, "Invalid direction");
        }
        Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
        Cell cell = player.getFarm().findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
        if (cell == null) {
            return new Response(false, "Cell not found");
        }
        if (!cell.isTilled()) {
            return new Response(false, "Cell is not tilled");
        }
        if (!cell.getObjectOnCell().type.equals("empty")) {
            return new Response(false, "Cell is not empty");
        }
        // TODO check for seed existence
        Seed playerSeed = player.getInventory().findSeedByItemName(seed);
        if (playerSeed == null) {
            return new Response(false, "Seed not found in player inventory");
        }
        Crop plant = new Crop(cropSeedsType);
        cell.setObjectOnCell(plant);
        GameRepository.saveGame(game);
        return new Response(true, "Planting was successful");
    }

    public static Response handleShowPlant(Request request) {
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Cell cell = player.getFarm().findCellByCoordinate(x, y);
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
            direction = Directions.valueOf(dir);
        } catch (Exception e) {
            return new Response(false, "Invalid direction");
        }
        Coordinate cellCoordinate = direction.getCoordinate(player.getCoordinate());
        Cell cell = player.getFarm().findCellByCoordinate(cellCoordinate.getX(), cellCoordinate.getY());
        if (cell == null) {
            return new Response(false, "Cell not found");
        }
        if (!cell.getObjectOnCell().type.equals("plant")) {
            return new Response(false, "Cell is not a plant");
        }
        Crop p = (Crop) cell.getObjectOnCell();
        p.setHasBeenFertilized(true);
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
