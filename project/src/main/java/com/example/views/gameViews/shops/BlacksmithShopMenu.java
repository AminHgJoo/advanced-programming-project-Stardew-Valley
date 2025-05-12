package com.example.views.gameViews.shops;

import com.example.controllers.gameMenuControllers.DealingController;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.GameMenuCommands;
import com.example.views.Menu;

public class BlacksmithShopMenu implements Menu {
    @Override
    public void handleMenu(String input) {
        Response response = null;
        if (GameMenuCommands.SHOW_ALL_PRODUCTS.matches(input)) {
            response = getShowAllProductsResponse(input);
        } else if (GameMenuCommands.SHOW_ALL_AVAILABLE_PRODUCTS.matches(input)) {
            response = getShowAvailableProductsResponse(input);
        } else if (GameMenuCommands.PURCHASE.matches(input)) {
            response = getPurchaseResponse(input);
        } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
            response = getExitShopResponse(input);
        } else {
            response = getInvalidCommand();
        }
        printResponse(response);
    }

    public static Response getShowAllProductsResponse(String input) {
        Request request = new Request(input);
        return DealingController.handleShowAllProducts(request);
    }

    public static Response getShowAvailableProductsResponse(String input) {
        Request request = new Request(input);
        return DealingController.handleShowAvailableProducts(request);
    }

    public static Response getPurchaseResponse(String input) {
        Request request = new Request(input);
        request.body.put("productName", GameMenuCommands.PURCHASE.getGroup(input, "productName"));
        request.body.put("count", GameMenuCommands.PURCHASE.getGroup(input, "count"));
        Response response = DealingController.handlePurchase(request);
        return response;
    }

    public static Response getExitShopResponse(String input) {
        Request request = new Request(input);
        Response response = DealingController.handleLeaveShop(request);
        return response;
    }
}
