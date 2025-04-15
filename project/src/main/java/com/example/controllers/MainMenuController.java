package com.example.controllers;

import com.example.models.App;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.types.MenuTypes;

public class MainMenuController extends Controller {
    public static Response handleLogout(Request request) {
        App.setLoggedInUser(null);
        App.setCurrMenuType(MenuTypes.SignInMenu);
        return new Response(true, "You are now logged out!");
    }
}
