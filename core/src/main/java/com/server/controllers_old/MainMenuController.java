package com.server.controllers_old;

import com.common.models.App;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.types.MenuTypes;
import com.server.repositories.UserRepository;

public class MainMenuController extends Controller {
    public static Response handleLogout(Request request) {
        App.setLoggedInUser(null);
        App.setCurrMenuType(MenuTypes.SignInMenu);
        UserRepository.removeStayLoggedInUser();
        return new Response(true, "You are now logged out!");
    }
}
