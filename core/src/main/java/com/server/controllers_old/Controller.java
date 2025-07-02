package com.server.controllers_old;

import com.common.models.App;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.types.MenuTypes;

public class Controller {
    public static Response handleEnterMenu(Request request) {
        String targetMenu = request.body.get("menuName");

        if (App.getCurrMenuType() == MenuTypes.SignInMenu) {
            return new Response(false, "Sign in first to navigate menus.");
        } else if (App.getCurrMenuType() == MenuTypes.MainMenu) {
            if (targetMenu.compareToIgnoreCase("GameMenu") == 0) {
                App.setCurrMenuType(MenuTypes.GameMenu);
                return new Response(true, "Going to game menu...");
            } else if (targetMenu.compareToIgnoreCase("ProfileMenu") == 0) {
                App.setCurrMenuType(MenuTypes.ProfileMenu);
                return new Response(true, "Going to profile menu...");
            } else {
                return new Response(false, "Invalid target menu.");
            }
        } else if (App.getCurrMenuType() == MenuTypes.GameMenu) {
            if (targetMenu.compareToIgnoreCase("MainMenu") == 0) {
                App.setCurrMenuType(MenuTypes.MainMenu);
                return new Response(true, "Going to main menu...");
            } else {
                return new Response(false, "Invalid target menu.");
            }
        } else if (App.getCurrMenuType() == MenuTypes.ProfileMenu) {
            if (targetMenu.compareToIgnoreCase("MainMenu") == 0) {
                App.setCurrMenuType(MenuTypes.MainMenu);
                return new Response(true, "Going to main menu...");
            } else {
                return new Response(false, "Invalid target menu.");
            }
        } else {
            return new Response(false, "Invalid Operation.");
        }
    }

    public static Response handleExitMenu(Request request) {
        if (App.getCurrMenuType() == MenuTypes.ProfileMenu) {
            App.setCurrMenuType(MenuTypes.MainMenu);
            return new Response(true, "Exiting to Main Menu...");
        } else if (App.getCurrMenuType() == MenuTypes.MainMenu) {
            App.setCurrMenuType(MenuTypes.ExitMenu);
            return new Response(true, "Exiting app...");
        } else if (App.getCurrMenuType() == MenuTypes.GameMenu) {
            App.setCurrMenuType(MenuTypes.MainMenu);
            return new Response(true, "Exiting to Main Menu...");
        } else if (App.getCurrMenuType() == MenuTypes.SignInMenu) {
            App.setCurrMenuType(MenuTypes.ExitMenu);
            return new Response(true, "Exiting app...");
        } else {
            return new Response(false, "Invalid Operation.");
        }

        //TODO : Later on the avatar menu becomes operational.
    }

    public static Response handleShowMenu(Request request) {
        return new Response(true, App.getCurrMenuType().toString());
    }
}
