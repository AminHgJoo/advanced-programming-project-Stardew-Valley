package com.example.models;

import com.example.Repositories.UserRepository;
import com.example.models.enums.types.MenuTypes;

import java.util.ArrayList;

public class App {
    final private static ArrayList<User> users = new ArrayList<>();
    private static User loggedInUser = UserRepository.getStayLoggedInUser();
    private static MenuTypes currMenuType;

    public static MenuTypes getCurrMenuType() {
        return currMenuType;
    }

    public static void setCurrMenuType(MenuTypes currMenuType) {
        App.currMenuType = currMenuType;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        App.loggedInUser = loggedInUser;
    }
}
