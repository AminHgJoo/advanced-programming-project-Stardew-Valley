package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.User;
import com.example.models.mapModels.Cell;
import com.example.utilities.FindPath;

import java.util.ArrayList;

public class MovementAndMap extends Controller {
    public static Response handleWalking(Request request) {
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Cell src = player.getFarm().findCellByCoordinate(player.getCoordinate().getX() , player.getCoordinate().getY());
        Cell dest = player.getFarm().findCellByCoordinate(x, y).clone();
        if(dest == null || !dest.getObjectOnCell().isWalkable) {
            return  new Response(false , "destination is not valid");
        }
        FindPath.pathBFS(src , dest , player.getFarm().getCells());
        ArrayList<Cell> path = new ArrayList<Cell>();
        while(dest != null) {
            path.add(dest);
            dest = dest.prev;
        }
        player.getFarm().initialCells();
        return null;
    }

    public static Response handlePrintMap(Request request) {
        return null;

    }

    public static Response handleMapHelp(Request request) {
        return null;

    }

    public static Response handleShowEnergy(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        double energy = game.getCurrentPlayer().getEnergy();
        if(energy == Double.POSITIVE_INFINITY) {
            return new Response(true, "infinity");
        }
        String energyString = String.valueOf((int)energy);
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
