package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.User;

public class MovementAndMap extends Controller {
    public static Response handleWalking(Request request) {
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
