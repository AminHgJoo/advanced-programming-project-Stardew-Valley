package com.example.models.enums.types;

import com.example.views.*;
import com.example.views.gameViews.GameMenu;
import com.example.views.gameViews.PlayerHomeMenu;
import com.example.views.gameViews.shops.*;

public enum MenuTypes {
    ExitMenu(new ExitMenu()),
    AvatarMenu(new AvatarMenu()),
    GameMenu(new GameMenu()),
    MainMenu(new MainMenu()),
    ProfileMenu(new ProfileMenu()),
    SignInMenu(new SignInMenu()),
    CarpenterShopMenu(new CarpenterShopMenu()),
    MarnieRanchMenu(new MarnieRanchMenu()),
    BlacksmithShopMenu(new BlacksmithShopMenu()),
    JojaMartShopMenu(new JojaMartShopMenu()),
    PierreGeneralStoreMenu(new PierreGeneralStoreMenu()),
    FishShopMenu(new FishShopMenu()),
    TheStardropSaloonMenu(new TheStardropSaloonMenu()),
    PlayerHomeMenu(new PlayerHomeMenu());

    private final Menu menu;

    MenuTypes(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public String toString() {
        if (this == MenuTypes.ExitMenu) {
            return "Exit Menu";
        } else if (this == MenuTypes.AvatarMenu) {
            return "Avatar Menu";
        } else if (this == MenuTypes.GameMenu) {
            return "Game Menu";
        } else if (this == MenuTypes.MainMenu) {
            return "Main Menu";
        } else if (this == MenuTypes.ProfileMenu) {
            return "Profile Menu";
        } else if (this == MenuTypes.SignInMenu) {
            return "Sign In Menu";
        } else if (this == MenuTypes.CarpenterShopMenu) {
            return "Carpenter Shop Menu";
        } else if (this == MenuTypes.MarnieRanchMenu) {
            return "Marnie Ranch Menu";
        } else if (this == MenuTypes.BlacksmithShopMenu) {
            return "Blacksmith Shop Menu";
        } else if (this == MenuTypes.JojaMartShopMenu) {
            return "Joja Mart Shop Menu";
        } else if (this == MenuTypes.PierreGeneralStoreMenu) {
            return "Pierre General Store Menu";
        } else if (this == MenuTypes.FishShopMenu) {
            return "Fish Shop Menu";
        } else if (this == MenuTypes.TheStardropSaloonMenu) {
            return "The Stardrop Saloon Menu";
        } else if (this == MenuTypes.PlayerHomeMenu) {
            return "Player Home Menu";
        } else {
            return "";
        }
    }
}
