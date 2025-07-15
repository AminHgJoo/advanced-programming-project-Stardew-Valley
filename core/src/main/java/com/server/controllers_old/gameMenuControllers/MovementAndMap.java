package com.server.controllers_old.gameMenuControllers;

import com.common.models.App;
import com.common.models.GameData;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.Player;
import com.common.models.User;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.BuildingBlock;
import com.common.models.mapObjects.DroppedItem;
import com.common.models.mapObjects.EmptyCell;
import com.common.models.mapObjects.Water;
import com.server.controllers_old.Controller;
import com.server.repositories.GameRepository;

import java.util.ArrayList;
import java.util.Objects;

public class MovementAndMap extends Controller {
    public static Response showCoords(Request request) {
        Coordinate coordinate = App.getLoggedInUser().getCurrentGame().getCurrentPlayer().getCoordinate();
        return new Response(true, "x: " + coordinate.getX() + " y: " + coordinate.getY());
    }

    public static Response cheatEmptyRectangle(Request request) {
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(gameData);
        ArrayList<Cell> cells = farm.getCells();
        Cell cell = Farm.getCellByCoordinate(x, y, cells);
        if (cell == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "no cell found");
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                Cell testCell = Farm.getCellByCoordinate(x + i, y + j, cells);
                if (testCell == null ||
                    testCell.getObjectOnCell() instanceof Water ||
                    testCell.getObjectOnCell() instanceof DroppedItem ||
                    testCell.getObjectOnCell() instanceof BuildingBlock) {
                    GameRepository.saveGame(gameData);
                    return new Response(false, "can't empty rectangle");
                }
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                Cell testCell = Farm.getCellByCoordinate(x + i, y + j, cells);
                testCell.setObjectOnCell(new EmptyCell());
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(true, "rectangle is now empty");
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

    public static Response goToVillage(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        if (player.isInVillage()) {
            GameRepository.saveGame(gameData);
            return new Response(false, "you are already in the village");
        }
        player.setInVillage(true);
        GameRepository.saveGame(gameData);
        return new Response(true, "you are in the village");
    }

    public static Response goToPartnerFarm(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Player partner = gameData.getPartner(player);
        if (partner == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "you are single");
        }
        Farm playerFarm = player.getCurrentFarm(gameData);
        Farm partnerFarm = partner.getFarm();
        if (playerFarm == partnerFarm) {
            GameRepository.saveGame(gameData);
            return new Response(false, "you are already in the farm");
        }
        player.setInVillage(false);
        player.setCurrentFarmNumber(partnerFarm.getFarmNumber());
        Coordinate coordinate = getEmptyCoordinate(player, partner, partnerFarm.getCells());
        if (coordinate == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "no empty cell found");
        }
        player.setCoordinate(coordinate);
        GameRepository.saveGame(gameData);
        return new Response(true, "you are in your partner's farm");
    }

    public static Response walkHome(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Player partner = gameData.getPartner(player);
        Farm playerFarm = player.getFarm();
        player.setInVillage(false);
        Coordinate coordinate = getEmptyCoordinate(player, partner, playerFarm.getCells());
        if (coordinate == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "no empty cell found");
        }
        player.setCoordinate(coordinate);
        GameRepository.saveGame(gameData);
        return new Response(true, "you are in home");
    }

    public static Response showFarm(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Farm farm = gameData.getCurrentPlayer().getCurrentFarm(gameData);
        farm.showFarm(Integer.parseInt(request.body.get("x"))
            , Integer.parseInt(request.body.get("y")),
            Integer.parseInt(request.body.get("size")), gameData);
        return new Response(true, "");
    }

    /// debug method
    public static Response showFullFarm(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Farm farm = gameData.getCurrentPlayer().getFarm();
        farm.showEntireFarm(gameData);
        return new Response(true, "");
    }

    public static Response handleMapHelp(Request request) {
        return new Response(true, "The owner is shown by blue 'O', any other non-owner players are shown as blue 'P' " +
            "all other icons show the first letter of their type and corresponding color; a quick guide is provided below:\n"
            + "B : Building tiles. (includes the mine, greenhouse, and player home.)\n"
            + "E : Empty cells\n"
            + "W : Water cells\n"
            + "M : Foraging Minerals\n"
            + "C : Crops\n"
            + "T : Trees\n"
            + "F : Foraging Crops\n"
            + "D : Dropped Item\n"
            + "A : Artisan Blocks\n");
    }

    public static Response handleShowEnergy(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        double energy = gameData.getCurrentPlayer().getEnergy();
        if (energy == Double.POSITIVE_INFINITY) {
            return new Response(true, "infinity");
        }
        String energyString = String.valueOf((int) energy);
        return new Response(true, energyString);
    }

    public static Response handleSetEnergy(Request request) {
        String energy = request.body.get("value");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        gameData.getCurrentPlayer().setEnergy(Double.parseDouble(energy));
        GameRepository.saveGame(gameData);
        return new Response(true, "energy successfully set to " + energy);
    }

    public static Response handleUnlimitedEnergy(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        gameData.getCurrentPlayer().setEnergy(Double.POSITIVE_INFINITY);
        GameRepository.saveGame(gameData);
        return new Response(true, "energy successfully set to infinity");
    }
}
