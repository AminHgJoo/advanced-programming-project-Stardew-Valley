package models;

import models.enums.MenuTypes;

import java.util.ArrayList;

public class App {
    final private static ArrayList<User> users = new ArrayList<>();
    private static MenuTypes currMenuType;

    public static MenuTypes getCurrMenuType() {
        return currMenuType;
    }

    public static void setCurrMenuType(MenuTypes currMenuType) {
        App.currMenuType = currMenuType;
    }
}
