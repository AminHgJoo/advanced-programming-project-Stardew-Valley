package com.example.views;

import com.example.models.App;
import com.example.models.enums.types.MenuTypes;

import java.util.Scanner;

public class AppView {
    public final static Scanner scanner = new Scanner(System.in);

    static {
        if (App.getLoggedInUser() != null) {
            App.setCurrMenuType(MenuTypes.MainMenu);
        }
    }

    public void run() {
        while (App.getCurrMenuType() != MenuTypes.ExitMenu) {
            String input = scanner.nextLine().trim();
            App.getCurrMenuType().getMenu().handleMenu(input);
        }
    }
}
