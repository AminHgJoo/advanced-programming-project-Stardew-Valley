package com.common.models.enums.types;

import com.server.views.*;
import com.server.views.gameViews.GameMenu;
import com.server.views.gameViews.PlayerHomeMenu;
import com.server.views.gameViews.TradeMenuView;
import com.server.views.gameViews.shops.*;

public enum MenuTypes {
    ExitMenu(new ExitMenu()),
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
    PlayerHomeMenu(new PlayerHomeMenu()),
    TradeMenu(new TradeMenuView());

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
        } else if (this == MenuTypes.TradeMenu) {
            return "Trade Menu";
        } else {
            return "";
        }
    }
}
