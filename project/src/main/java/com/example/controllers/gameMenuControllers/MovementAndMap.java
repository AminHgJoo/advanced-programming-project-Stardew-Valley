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
import com.example.utilities.FindPath;
import com.example.views.AppView;

import java.util.ArrayList;
import java.util.Objects;

public class MovementAndMap extends Controller {
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

    public static Response handleWalking(Request request) {
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        if (player.isInVillage()) {
            GameRepository.saveGame(game);
            return new Response(false, "can't walk in village");
        }
        Cell src = player.getCurrentFarm(game).findCellByCoordinate(player.getCoordinate().getX(), player.getCoordinate().getY());
        Cell dest = player.getCurrentFarm(game).findCellByCoordinate(x, y).clone();
        if (dest == null || !dest.getObjectOnCell().isWalkable) {
            return new Response(false, "destination is not valid");
        }
        FindPath.pathBFS(src, dest, player.getCurrentFarm(game).getCells());
        double energy = dest.energy / 20;
        String message = "Your current energy is: " + player.getEnergy() + "\n" +
                "The path energy cost is : " + energy + "\n" +
                "Do you want to move the path? (Y/N): ";
        System.out.println(message);
        String answer = AppView.scanner.nextLine();
        if (answer.compareToIgnoreCase("Y") == 0) {
            ArrayList<Cell> path = new ArrayList<Cell>();
            while (dest != null) {
                path.add(dest);
                dest = dest.prev;
            }
            path.reversed();
            for (Cell c : path) {
                if (c.energy > player.getEnergy()) {
                    player.setPlayerFainted(true);
                    player.setEnergy(player.getEnergy() - c.energy / 20);
                    player.getCurrentFarm(game).initialCells();
                    GameRepository.saveGame(game);
                    return new Response(false, "You have fainted");
                }
                if (c.energy + player.getUsedEnergyInTurn() > 50) {
                    player.getCurrentFarm(game).initialCells();
                    GameRepository.saveGame(game);
                    return new Response(false, "You can not use this much energy");
                }
                player.setCoordinate(c.getCoordinate());
            }
            player.setEnergy(player.getEnergy() - energy);
            player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energy);
            player.getCurrentFarm(game).initialCells();
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
                Integer.parseInt(request.body.get("size")));
        return new Response(true, "");
    }

    /// debug method
    public static Response showFullFarm(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getCurrentFarm(game);
        farm.showEntireFarm();
        return new Response(true, "");
    }

    public static Response handleMapHelp(Request request) {
        return new Response(true, "The player is shown by blue 'P', " +
                "all other icons show the first letter of their type and corresponding color.");
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
        GameRepository.saveGame(game);
        return new Response(true, "energy successfully set to infinity");
    }
}
