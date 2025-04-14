package com.example.views;

import com.example.controllers.MainMenuController;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.MainMenuCommands;

public class MainMenu implements Menu {
    MainMenuController mainMenuController = new MainMenuController();

    public void handleMenu(String input) {
        Response response = null;
        if (MainMenuCommands.ENTER_MENU.matches(input)) {
            response = getEnterMenuResponse(input);
        } else if (MainMenuCommands.EXIT_MENU.matches(input)) {
            response = getExitMenuResponse(input);
        } else if (MainMenuCommands.SHOW_MENU.matches(input)) {
            response = getShowMenuResponse(input);
        } else if (MainMenuCommands.USER_LOGOUT.matches(input)) {
            response = getUserLogoutResponse(input);
        } else {
            response = getInvalidCommand();
        }
        printResponse(response);
    }

    private static Response getEnterMenuResponse(String input) {
        Request request = new Request(input);
        request.body.put("menu_name", MainMenuCommands.ENTER_MENU.getGroup(input, "menu_name"));
        Response response = MainMenuController.handleEnterMenu(request);
        return response;
    }

    private static Response getExitMenuResponse(String input) {
        Request request = new Request(input);
        Response response = MainMenuController.handleExitMenu(request);
        return response;
    }

    private static Response getShowMenuResponse(String input) {
        Request request = new Request(input);
        Response response = MainMenuController.handleShowMenu(request);
        return response;
    }

    private static Response getUserLogoutResponse(String input) {
        Request request = new Request(input);
        Response response = MainMenuController.handleLogout(request);
        return response;
    }
}
