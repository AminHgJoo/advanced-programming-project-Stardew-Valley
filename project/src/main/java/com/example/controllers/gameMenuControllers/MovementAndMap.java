package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.User;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Farm;
import com.example.utilities.FindPath;
import com.example.views.AppView;

import java.util.ArrayList;

public class MovementAndMap extends Controller {

    public static Response handleWalking(Request request) {
        // TODO test the walk functionality
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Cell src = player.getFarm().findCellByCoordinate(player.getCoordinate().getX(), player.getCoordinate().getY());
        Cell dest = player.getFarm().findCellByCoordinate(x, y).clone();
        if (dest == null || !dest.getObjectOnCell().isWalkable) {
            return new Response(false, "destination is not valid");
        }
        FindPath.pathBFS(src, dest, player.getFarm().getCells());
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
                    player.getFarm().initialCells();
                    return new Response(false, "You have been fainted");
                }
                if (c.energy + player.getUsedEnergyInTurn() > 50) {
                    player.getFarm().initialCells();
                    return new Response(false, "You can not use this much energy");
                }
                player.setCoordinate(c.getCoordinate());
            }
            player.setEnergy(player.getEnergy() - energy);
            player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energy);
            player.getFarm().initialCells();
            return new Response(true, "You successfully moved to the destination");
        } else {
            player.getFarm().initialCells();
            return new Response(false, "Movement process aborted");
        }
    }

    public static Response showFarm(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getFarm();
        farm.showFarm(Integer.parseInt(request.body.get("x"))
                , Integer.parseInt(request.body.get("y")),
                Integer.parseInt(request.body.get("size")));
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
        return new Response(true, "energy successfully set to " + energy);
    }

    public static Response handleUnlimitedEnergy(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        game.getCurrentPlayer().setEnergy(Double.POSITIVE_INFINITY);
        return new Response(true, "energy successfully set to infinity");
    }
}
