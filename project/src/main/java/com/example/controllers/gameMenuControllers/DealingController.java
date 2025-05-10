package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.Quality;
import com.example.models.enums.recipes.CookingRecipes;
import com.example.models.enums.recipes.CraftingRecipes;
import com.example.models.enums.types.MenuTypes;
import com.example.models.enums.types.itemTypes.*;
import com.example.models.enums.types.storeProductTypes.FishProducts;
import com.example.models.items.*;
import com.example.models.mapObjects.Crop;
import com.example.utilities.MenuToStoreString;

public class DealingController extends Controller {
    public static Response handleGoToStore(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        if(!player.isInVillage()){
            GameRepository.saveGame(game);
            return new Response(false, "You are not in the village");
        }
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
        Game game = user.getCurrentGame();
        //TODO add stores and store products and fix getStore usages
        Store store = game.getMap().getVillage().getStore(MenuToStoreString
                .convertToString(App.getCurrMenuType().getMenu()));
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
        if (p.getType().getProductPrice(game.getSeason()) * n > player.getMoney(game)) {
            return new Response(false, "Not enough money");
        }
        p.setAvailableCount(p.getAvailableCount() - n);
        ItemType type = p.getType().getItemType();
        if (type == null) {
            Response res = handleBuyRecipe(productName, p, player);
            if (res.isSuccess()) {
                player.setMoney((int) (player.getMoney(game) - p.getType().getProductPrice(game.getSeason()) * n), game);
            }
            GameRepository.saveGame(game);
            return res;
        } else {
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
                item = new ForagingMineral(Quality.DEFAULT, (ForagingMineralsType) type);
            } else if (type instanceof ToolTypes) {
                Quality q = Quality.DEFAULT;
                if (p.getType().getName().equals(FishProducts.BAMBOO_POLE)) {
                    q = Quality.SILVER;
                } else if (p.getType().getName().equals(FishProducts.TRAINING_ROD)) {
                    q = Quality.COPPER;
                } else if (p.getType().getName().equals(FishProducts.FIBERGLASS_ROD)) {
                    q = Quality.GOLD;
                } else if (p.getType().getName().equals(FishProducts.IRIDIUM_ROD)) {
                    q = Quality.IRIDIUM;
                }
                item = new Tool(q, (ToolTypes) type, (int) p.getType().getProductPrice(game.getSeason()));
            }
            if (item == null) {
                return new Response(false, "No such item");
            }
            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(item.getName());
            if (slot == null) {
                if (backpack.getSlots().size() == backpack.getType().getMaxCapacity()) {
                    return new Response(false, "You don't have enough space in your backpack.");
                }
                Slot newSlot = new Slot(item, n);
                backpack.addSlot(newSlot);
            } else {
                slot.setCount(slot.getCount() + 1);
            }
            player.setMoney((int) (player.getMoney(game) - p.getType().getProductPrice(game.getSeason()) * n), game);
            GameRepository.saveGame(game);
            return new Response(true, "Purchased successfully");
        }
    }

    public static Response handleCheatAddDollars(Request request) {
        int count = Integer.parseInt(request.body.get("count"));
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        player.setMoney(player.getMoney(game) + count, game);
        return new Response(true, "Money added successfully");
    }

    public static Response handleSellProduct(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        String productName = request.body.get("productName");

        Slot productSlot = player.getInventory().getSlotByItemName(productName);
        if (productSlot == null) {
            return new Response(false, "No such product");
        }
        int n = productSlot.getCount();
        if (request.body.get("count") != null) {
            n = Integer.parseInt(request.body.get("count"));
        }
        productSlot.setCount(productSlot.getCount() - 1);
        if (productSlot.getCount() == 0) {
            player.getInventory().removeSlot(productSlot);
        }
        double money = productSlot.getItem().getValue() * n;
        if (productSlot.getItem().getQuality() == Quality.SILVER) {
            money = money * 1.25;
        } else if (productSlot.getItem().getQuality() == Quality.GOLD) {
            money = money * 1.5;
        } else if (productSlot.getItem().getQuality() == Quality.IRIDIUM) {
            money = money * 2;
        }
        player.setMoneyInNextDay(player.getMoneyInNextDay() + (int) money);
        GameRepository.saveGame(game);
        return null;
    }

    public static Response handleBuyRecipe(String name, StoreProduct p, Player player) {
        CraftingRecipes craft = CraftingRecipes.findByName(name.split(" ")[0]);
        CookingRecipes cook = CookingRecipes.findByName(name.split(" ")[0]);
        if (craft != null) {
            player.getUnlockedCraftingRecipes().add(craft);
            return new Response(true, name + " successfully added to recipes");
        }
        if (cook != null) {
            player.getUnlockedCookingRecipes().add(cook);
            return new Response(true, name + " successfully added to recipes");
        }
        return new Response(false, "Recipe not found");
    }

    public static Response handleLeaveShop(Request request) {
        String name = App.getCurrMenuType().name();
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "Leaving " + name + " ...");
    }
}
