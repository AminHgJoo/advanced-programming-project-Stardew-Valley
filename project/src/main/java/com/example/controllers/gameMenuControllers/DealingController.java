package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.Store;
import com.example.models.enums.types.MenuTypes;

public class DealingController extends Controller {
    public static Response handleGoToStore(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        String name = request.body.get("storeName");
        boolean check = false;
        if (name.compareToIgnoreCase("Blacksmith") == 0) {
            Store store = game.getMap().getVillage().getStore(name);
            if (store.isOpen(game.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.BlacksmithShopMenu);
            }
        } else if (name.compareToIgnoreCase("JojaMart") == 0) {
            Store store = game.getMap().getVillage().getStore(name);
            if (store.isOpen(game.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.JojaMartShopMenu);
            }
        } else if (name.compareToIgnoreCase("Pierre's General Store") == 0) {
            Store store = game.getMap().getVillage().getStore(name);
            if (store.isOpen(game.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.PierreGeneralStoreMenu);
            }
        } else if (name.compareToIgnoreCase("Carpenter's Shop") == 0) {
            Store store = game.getMap().getVillage().getStore(name);
            if (store.isOpen(game.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.CarpenterShopMenu);
            }
        } else if (name.compareToIgnoreCase("Fish Shop") == 0) {
            Store store = game.getMap().getVillage().getStore(name);
            if (store.isOpen(game.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.FishShopMenu);
            }
        } else if (name.compareToIgnoreCase("The Stardrop Saloon") == 0) {
            Store store = game.getMap().getVillage().getStore(name);
            if (store.isOpen(game.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.TheStardropSaloonMenu);
            }
        } else {
            return new Response(false, "Invalid shop name");
        }
        if (!check) {
            return new Response(false, "Shop is closed now :(");
        }
        return new Response(true, "Going to " + name + " ...");
    }

    public static Response handleShowAllProducts(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        String name = request.body.get("storeName");
        Store store = game.getMap().getVillage().getStore(name);
        return new Response(true , store.productsToString());
    }

    public static Response handleShowAvailableProducts(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        String name = request.body.get("storeName");
        Store store = game.getMap().getVillage().getStore(name);
        return new Response(true , store.availableProductsToString());    }

    public static Response handlePurchase(Request request) {
        return null;
    }

    public static Response handleCheatAddDollars(Request request) {
        int count  = Integer.parseInt(request.body.get("count"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        player.setMoney(player.getMoney() + count);
        return new Response(true , "Money added successfully");
    }

    public static Response handleSellProduct(Request request) {
        return null;
    }

    public static Response handleLeaveShop(Request request) {
        String name = App.getCurrMenuType().name();
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "Leaving " + name + " ...");
    }
}
