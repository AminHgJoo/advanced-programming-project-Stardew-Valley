package com.example.models.enums.types;

import com.example.views.*;

public enum MenuTypes {
    ExitMenu(new ExitMenu()),
    AvatarMenu(new AvatarMenu()),
    GameMenu(new GameMenu()),
    MainMenu(new GameMenu()),
    ProfileMenu(new ProfileMenu()),
    SignInMenu(new SignInMenu());

    private final views.Menu menu;

    MenuTypes(views.Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
