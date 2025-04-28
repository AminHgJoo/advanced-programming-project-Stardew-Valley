package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.Quality;
import com.example.models.enums.types.MenuTypes;
import com.example.models.enums.types.itemTypes.*;
import com.example.models.items.*;
import com.example.models.mapObjects.Crop;

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
        return new Response(true, store.productsToString(game.getSeason()));
    }

    public static Response handleShowAvailableProducts(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        String name = request.body.get("storeName");
        Store store = game.getMap().getVillage().getStore(name);
        return new Response(true, store.availableProductsToString(game.getSeason()));
    }

    public static Response handlePurchase(Request request) {
        User user = App.getLoggedInUser();
        Game game = App.getLoggedInUser().getCurrentGame();
        // TODO handle finding store
        Store store = game.getMap().getVillage().getStore("");
        Player player = game.getCurrentPlayer();
        String productName = request.body.get("productName");
        int n = 1;
        if (request.body.get("count") != null) {
            n = Integer.parseInt(request.body.get("count"));
        }
        StoreProduct p = store.getProduct(productName);
        if (p == null) {
            return new Response(false, "No such product");
        }
        if (p.getAvailableCount() < n) {
            return new Response(false, "Not enough available products");
        }
        if (p.getType().getProductPrice(game.getSeason()) * n > player.getMoney()) {
            return new Response(false, "Not enough money");
        }
        p.setAvailableCount(p.getAvailableCount() - n);
        ItemType type = p.getType().getItemType();
        Item item = null;
        if (type instanceof FoodTypes) {
            item = new Food((FoodTypes) type);
        } else if (type instanceof CropSeedsType) {
            item = new Seed((CropSeedsType) type);
        } else if (type instanceof TreeSeedsType) {
            item = new TreeSeed((TreeSeedsType) type);
        } else if (type instanceof FishType) {
            item = new Fish(Quality.DEFAULT, (FishType) type);
        } else if (type instanceof MiscType) {
            item = new Misc((MiscType) type);
        } else if (type instanceof ForagingMineralsType) {
            item = new ForagingMineral(Quality.DEFAULT,(ForagingMineralsType) type);
        } else if (type instanceof ToolTypes) {
            type = (ToolTypes) type;
//            item = new Tool(Quality.DEFAULT,type,(ToolTypes) type);
            // TODO idk how to handle this
        }
        if(item == null){
            return new Response(false, "No such item");
        }
        // TODO add to slot
        return null;
    }

    public static Response handleCheatAddDollars(Request request) {
        int count = Integer.parseInt(request.body.get("count"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        player.setMoney(player.getMoney() + count);
        return new Response(true, "Money added successfully");
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
