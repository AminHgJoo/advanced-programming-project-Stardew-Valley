package com.server.views.gameViews.shops;

import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.commands.GameMenuCommands;
import com.server.views.Menu;
import com.server.views.gameViews.GameMenu;
import com.server.controllers_old.gameMenuControllers.CarpenterShopController;
import com.server.controllers_old.gameMenuControllers.DealingController;

public class CarpenterShopMenu implements Menu {
    private static Response build(String input) {
        Request request = new Request(input);
        request.body.put("buildingName", GameMenuCommands.BUILD.getGroup(input, "buildingName"));
        request.body.put("x", GameMenuCommands.BUILD.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.BUILD.getGroup(input, "y"));
        return CarpenterShopController.build(request);
    }

    private static Response leaveShop(String input) {
        Request request = new Request(input);
        return CarpenterShopController.leaveCarpenterShop(request);
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

    @Override
    public void handleMenu(String input) {
        Response response = null;
        if (GameMenuCommands.BUILD.matches(input)) {
            response = build(input);
        } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
            response = leaveShop(input);
        } else if (GameMenuCommands.SHOW_ALL_PRODUCTS.matches(input)) {
            response = getShowAllProductsResponse(input);
        } else if (GameMenuCommands.SHOW_ALL_AVAILABLE_PRODUCTS.matches(input)) {
            response = getShowAvailableProductsResponse(input);
        } else if (GameMenuCommands.PURCHASE.matches(input)) {
            response = getPurchaseResponse(input);
        }

        if (response != null)
            printResponse(response);
        else new GameMenu().handleMenu(input);
    }
}
