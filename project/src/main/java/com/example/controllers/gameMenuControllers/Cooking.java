package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.enums.recipes.CookingRecipes;
import com.example.models.enums.types.MenuTypes;

public class Cooking extends Controller {

    public static Response handlePickFromRefrigerator(Request request) {
        String item = request.body.get("item");


    }

    public static Response handlePutIntoRefrigerator(Request request) {

    }

    public static Response handleCookingFood(Request request) {

    }

    public static Response handleShowCookingRecipes(Request request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cooking recipes : \n");

        Game game = App.getLoggedInUser().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();

        for (CookingRecipes recipe : currentPlayer.getUnlockedCookingRecipes()) {
            stringBuilder.append(recipe.toString()).append("\n");
        }

        return new Response(true, stringBuilder.toString());
    }

    public static Response handleEnterPlayerHome(Request request) {
        App.setCurrMenuType(MenuTypes.PlayerHomeMenu);
        return new Response(true, "Entering home menu.");
    }

    public static Response handleExitPlayerHome(Request request) {
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "Exiting home menu.");
    }
}
