package com.example.views.gameViews.shops;

import com.example.controllers.gameMenuControllers.DealingController;
import com.example.controllers.gameMenuControllers.LivestockController;
import com.example.controllers.gameMenuControllers.MarineRanchController;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.GameMenuCommands;
import com.example.views.Menu;
import com.example.views.gameViews.GameMenu;

public class MarnieRanchMenu implements Menu {

    @Override
    public void handleMenu(String input) {
        Response response = null;
        if (GameMenuCommands.BUY_ANIMAL.matches(input)) {
            response = getBuyAnimalResponse(input);
        } else if (GameMenuCommands.SHOW_ALL_PRODUCTS.matches(input)) {
            response = getShowAllProductsResponse(input);
        } else if (GameMenuCommands.SHOW_ALL_AVAILABLE_PRODUCTS.matches(input)) {
            response = getShowAvailableProductsResponse(input);
        } else if (GameMenuCommands.PURCHASE.matches(input)) {
            response = getPurchaseResponse(input);
        } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
            response = leaveRanch(input);
        }

        if (response != null)
            printResponse(response);
        else new GameMenu().handleMenu(input);
    }

    private static Response getBuyAnimalResponse(String input) {
        Request request = new Request(input);
        request.body.put("animal", GameMenuCommands.BUY_ANIMAL.getGroup(input, "animal"));
        request.body.put("name", GameMenuCommands.BUY_ANIMAL.getGroup(input, "name"));
        return LivestockController.handleBuyAnimal(request);
    }

    private static Response leaveRanch(String input) {
        Request request = new Request(input);
        return MarineRanchController.leaveRanch(request);
    }

    public static Response getShowAllProductsResponse(String input) {
        Request request = new Request(input);
        Response response = DealingController.handleShowAllProducts(request);
        return response;
    }

    public static Response getShowAvailableProductsResponse(String input) {
        Request request = new Request(input);
        Response response = DealingController.handleShowAvailableProducts(request);
        return response;
    }

    public static Response getPurchaseResponse(String input) {
        Request request = new Request(input);
        request.body.put("productName", GameMenuCommands.PURCHASE.getGroup(input, "productName"));
        request.body.put("count", GameMenuCommands.PURCHASE.getGroup(input, "count"));
        Response response = DealingController.handlePurchase(request);
        return response;
    }
}
