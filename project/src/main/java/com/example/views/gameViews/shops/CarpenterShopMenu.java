package com.example.views.gameViews.shops;

import com.example.controllers.gameMenuControllers.CarpenterShopController;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.GameMenuCommands;
import com.example.views.Menu;

public class CarpenterShopMenu implements Menu {
    @Override
    public void handleMenu(String input) {
        Response response = null;
        if (GameMenuCommands.BUILD.matches(input)) {
            response = build(input);
        } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
            response = leaveShop(input);
        } else {
            response = getInvalidCommand();
        }

        printResponse(response);
    }

    private static Response build(String input) {
        Request request = new Request(input);
        return CarpenterShopController.build(request);
    }

    private static Response leaveShop(String input) {
        Request request = new Request(input);
        return CarpenterShopController.leaveCarpenterShop(request);
    }
}
