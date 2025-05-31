package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.User;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Coordinate;
import com.example.models.mapModels.Farm;
import com.example.models.mapObjects.BuildingBlock;
import com.example.models.mapObjects.DroppedItem;
import com.example.models.mapObjects.EmptyCell;
import com.example.models.mapObjects.Water;
import com.example.utilities.FindPath;
import com.example.utilities.Tile;
import com.example.views.AppView;

import java.util.ArrayList;
import java.util.List;
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
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);
        ArrayList<Cell> cells = farm.getCells();
        Cell cell = Farm.getCellByCoordinate(x, y, cells);
        if (cell == null) {
            GameRepository.saveGame(game);
            return new Response(false, "no cell found");
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                Cell testCell = Farm.getCellByCoordinate(x + i, y + j, cells);
                if (testCell == null ||
                        testCell.getObjectOnCell() instanceof Water ||
                        testCell.getObjectOnCell() instanceof DroppedItem ||
                        testCell.getObjectOnCell() instanceof BuildingBlock) {
                    GameRepository.saveGame(game);
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
        GameRepository.saveGame(game);
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
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        if (player.isInVillage()) {
            GameRepository.saveGame(game);
            return new Response(false, "you are already in the village");
        }
        player.setInVillage(true);
        GameRepository.saveGame(game);
        return new Response(true, "you are in the village");
    }

    public static Response goToPartnerFarm(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Player partner = game.getPartner(player);
        if (partner == null) {
            GameRepository.saveGame(game);
            return new Response(false, "you are single");
        }
        Farm playerFarm = player.getCurrentFarm(game);
        Farm partnerFarm = partner.getFarm();
        if (playerFarm == partnerFarm) {
            GameRepository.saveGame(game);
            return new Response(false, "you are already in the farm");
        }
        player.setInVillage(false);
        player.setCurrentFarmNumber(partnerFarm.getFarmNumber());
        Coordinate coordinate = getEmptyCoordinate(player, partner, partnerFarm.getCells());
        if (coordinate == null) {
            GameRepository.saveGame(game);
            return new Response(false, "no empty cell found");
        }
        player.setCoordinate(coordinate);
        GameRepository.saveGame(game);
        return new Response(true, "you are in your partner's farm");
    }

    public static Response walkHome(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Player partner = game.getPartner(player);
        Farm playerFarm = player.getFarm();
        player.setInVillage(false);
        Coordinate coordinate = getEmptyCoordinate(player, partner, playerFarm.getCells());
        if (coordinate == null) {
            GameRepository.saveGame(game);
            return new Response(false, "no empty cell found");
        }
        player.setCoordinate(coordinate);
        GameRepository.saveGame(game);
        return new Response(true, "you are in home");
    }

    public static Response handleAddCoords(Request request) {
        Request newReq = new Request(request.command);
        Coordinate c = App.getLoggedInUser().getCurrentGame().getCurrentPlayer().getCoordinate();
        newReq.body.put("x", String.valueOf(c.getX() + Integer.parseInt(request.body.get("x"))));
        newReq.body.put("y", String.valueOf(c.getY() + Integer.parseInt(request.body.get("y"))));
        return MovementAndMap.handleWalking(newReq);
    }

    public static Response handleWalking(Request request) {
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        if (player.isInVillage()) {
            GameRepository.saveGame(game);
            return new Response(false, "can't walk in village");
        }
        Tile src = new Tile(player.getCurrentFarm(game).findCellByCoordinate(player.getCoordinate().getX(), player.getCoordinate().getY()));
        if (player.getCurrentFarm(game).findCellByCoordinate(x, y) == null) {
            return new Response(false, "destination is not valid");
        }
        Tile dest = new Tile(player.getCurrentFarm(game).findCellByCoordinate(x, y).clone());
        if (dest == null || !dest.getObjectOnCell().isWalkable) {
            return new Response(false, "destination is not valid");
        }
        dest = FindPath.pathBFS(src, dest, player.getCurrentFarm(game).getCells());
        double energy = dest.energy / (double) 20;
        String message = "Your current energy is: " + player.getEnergy() + "\n" +
                "The path energy cost is : " + energy + "\n" +
                "Do you want to move the path? (Y/N): ";
        System.out.println(message);
        String answer = AppView.scanner.nextLine();
        if (answer.compareToIgnoreCase("Y") == 0) {
            ArrayList<Tile> path = new ArrayList<Tile>();
            while (dest != null) {
                if (dest.prev != null) {
                    dest.energy -= dest.prev.energy;
                }
                dest.energy /= 20
                ;
                path.add(dest);
                dest = dest.prev;
            }
            List<Tile> arr = path.reversed();
            for (Tile c : arr) {
                if (c.energy > player.getEnergy()) {
                    player.setPlayerFainted(true);
                    player.setEnergy(player.getEnergy() - c.energy);
                    LoadingSavingTurnHandling.handleNextTurn(null);
                    GameRepository.saveGame(game);
                    return new Response(false, "You have fainted");
                }
                if (c.energy + player.getUsedEnergyInTurn() > 50) {
                    GameRepository.saveGame(game);
                    return new Response(false, "You can not use this much energy");
                }
                player.setCoordinate(c.getCoordinate());
                player.setEnergy(player.getEnergy() - c.energy);
                player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + c.energy);
            }
            GameRepository.saveGame(game);
            return new Response(true, "You successfully moved to the destination");
        } else {
            player.getCurrentFarm(game).initialCells();
            return new Response(false, "Movement process aborted");
        }
    }

    public static Response showFarm(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getCurrentFarm(game);
        farm.showFarm(Integer.parseInt(request.body.get("x"))
                , Integer.parseInt(request.body.get("y")),
                Integer.parseInt(request.body.get("size")), game);
        return new Response(true, "");
    }

    /// debug method
    public static Response showFullFarm(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getFarm();
        farm.showEntireFarm(game);
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
        Game game = user.getCurrentGame();
        double energy = game.getCurrentPlayer().getEnergy();
        if (energy == Double.POSITIVE_INFINITY) {
            return new Response(true, "infinity");
        }
        String energyString = String.valueOf((int) energy);
        return new Response(true, energyString);
    }

    public static Response handleSetEnergy(Request request) {
        String energy = request.body.get("value");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        game.getCurrentPlayer().setEnergy(Double.parseDouble(energy));
        GameRepository.saveGame(game);
        return new Response(true, "energy successfully set to " + energy);
    }

    public static Response handleUnlimitedEnergy(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        game.getCurrentPlayer().setEnergy(Double.POSITIVE_INFINITY);
        game.getCurrentPlayer().setUsedEnergyInTurn(Double.NEGATIVE_INFINITY);
        GameRepository.saveGame(game);
        return new Response(true, "energy successfully set to infinity");
    }
}
