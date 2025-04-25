package com.example.views;

import com.example.controllers.gameMenuControllers.CarpenterShopController;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.GameMenuCommands;

public class CarpenterShopMenu implements Menu {
    @Override
    public void handleMenu(String input) {
        Response response = null;
        if (GameMenuCommands.BUILD.matches(input)) {
            build(input);
        } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
            leaveShop(input);
        } else {
            response = getInvalidCommand();
        }

        printResponse(response);
    }

    private static void build(String input) {
        Request request = new Request(input);
        CarpenterShopController.build(request);
    }

    private static void leaveShop(String input) {
        Request request = new Request(input);
        CarpenterShopController.leaveCarpenterShop(request);
    }
}
