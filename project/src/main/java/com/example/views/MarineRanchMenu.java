package com.example.views;

import com.example.controllers.gameMenuControllers.MarineRanchController;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.GameMenuCommands;

public class MarineRanchMenu implements Menu {

    @Override
    public void handleMenu(String input) {
        Response response = null;
        if (GameMenuCommands.BUY_ANIMAL.matches(input)) {
            buyAnimal(input);
        } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
            response = leaveRanch(input);
        } else {
            response = getInvalidCommand();
        }

        printResponse(response);
    }

    private static void buyAnimal(String input) {
        Request request = new Request(input);
        MarineRanchController.buyAnimal(request);
    }

    private static Response leaveRanch(String input) {
        Request request = new Request(input);
        return MarineRanchController.leaveRanch(request);
    }
}
