package com.example.views.gameViews;

import com.example.controllers.gameMenuControllers.Cooking;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.GameMenuCommands;
import com.example.views.Menu;

public class PlayerHomeMenu implements Menu {
    @Override
    public void handleMenu(String input) {
        Response response = null;

        if (GameMenuCommands.COOKING_REFRIGERATOR_PICK.matches(input)) {
            response = getCookingPickRefrigeratorResponse(input);
        } else if (GameMenuCommands.COOKING_REFRIGERATOR_PUT.matches(input)) {
            response = getCookingRefrigeratorPutResponse(input);
        } else if (GameMenuCommands.COOKING_SHOW_RECIPES.matches(input)) {
            response = getShowCookingRecipesResponse(input);
        } else if (GameMenuCommands.COOKING_PREPARE.matches(input)) {
            response = getCookingPrepareResponse(input);
        } else if (GameMenuCommands.EXIT_HOME.matches(input)) {
            response = getExitPlayerHomeResponse(input);
        } else if (GameMenuCommands.SHOW_MENU.matches(input)) {
            response = getShowMenuResponse(input);
        } else {
            response = getInvalidCommand();
        }

        printResponse(response);
    }

    private static Response getCookingPrepareResponse(String input) {
        Request request = new Request(input);
        request.body.put("itemName", GameMenuCommands.COOKING_PREPARE.getGroup(input, "itemName"));
        return Cooking.handleCookingFood(request);
    }

    private static Response getExitPlayerHomeResponse(String input) {
        Request request = new Request(input);
        return Cooking.handleExitPlayerHome(request);
    }

    private static Response getShowMenuResponse(String input) {
        Request request = new Request(input);
        Response response = Cooking.handleShowMenu(request);
        return response;
    }

    private static Response getCookingRefrigeratorPutResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("item", GameMenuCommands.COOKING_REFRIGERATOR_PUT.getGroup(input, "item"));
        response = Cooking.handlePutIntoRefrigerator(request);
        return response;
    }

    private static Response getCookingPickRefrigeratorResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("item", GameMenuCommands.COOKING_REFRIGERATOR_PICK.getGroup(input, "item"));
        response = Cooking.handlePickFromRefrigerator(request);
        return response;
    }

    private static Response getShowCookingRecipesResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = Cooking.handleShowCookingRecipes(request);
        return response;
    }
}
