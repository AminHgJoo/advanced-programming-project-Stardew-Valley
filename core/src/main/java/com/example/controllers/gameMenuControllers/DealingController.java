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
import com.example.models.enums.types.inventoryEnums.BackpackType;
import com.example.models.enums.types.inventoryEnums.TrashcanType;
import com.example.models.enums.types.itemTypes.*;
import com.example.models.enums.types.storeProductTypes.BlackSmithProducts;
import com.example.models.enums.types.storeProductTypes.FishProducts;
import com.example.models.items.*;
import com.example.utilities.MenuToStoreString;

// TODO backpack item overflow

public class DealingController extends Controller {
    public static Response handleGoToStore(Request request) {
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        if (!player.isInVillage()) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You are not in the village");
        }
        String name = request.body.get("storeName");
        boolean check = false;
        if (name.compareToIgnoreCase("Blacksmith") == 0) {
            Store store = gameData.getMap().getVillage().getStore(name);
            if (store.isOpen(gameData.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.BlacksmithShopMenu);
            }
        } else if (name.compareToIgnoreCase("JojaMart") == 0) {
            Store store = gameData.getMap().getVillage().getStore(name);
            if (store.isOpen(gameData.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.JojaMartShopMenu);
            }
        } else if (name.compareToIgnoreCase("Pierre's General Store") == 0) {
            Store store = gameData.getMap().getVillage().getStore(name);
            if (store.isOpen(gameData.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.PierreGeneralStoreMenu);
            }
        } else if (name.compareToIgnoreCase("Carpenter's Shop") == 0) {
            Store store = gameData.getMap().getVillage().getStore(name);
            if (store.isOpen(gameData.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.CarpenterShopMenu);
            }
        } else if (name.compareToIgnoreCase("Fish Shop") == 0) {
            Store store = gameData.getMap().getVillage().getStore(name);
            if (store.isOpen(gameData.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.FishShopMenu);
            }
        } else if (name.compareToIgnoreCase("The Stardrop Saloon") == 0) {
            Store store = gameData.getMap().getVillage().getStore(name);
            if (store.isOpen(gameData.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.TheStardropSaloonMenu);
            }
        } else if (name.compareToIgnoreCase("Marnie's Ranch") == 0) {
            Store store = gameData.getMap().getVillage().getStore(name);
            if (store.isOpen(gameData.getDate().getHour())) {
                check = true;
                App.setCurrMenuType(MenuTypes.MarnieRanchMenu);
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
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Store store = gameData.getMap().getVillage().getStore(MenuToStoreString
                .convertToString(App.getCurrMenuType().getMenu()));
        return new Response(true, store.productsToString(gameData.getSeason()));
    }

    public static Response handleShowAvailableProducts(Request request) {
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Store store = gameData.getMap().getVillage().getStore(MenuToStoreString
                .convertToString(App.getCurrMenuType().getMenu()));
        return new Response(true, store.availableProductsToString(gameData.getSeason()));
    }

    public static Response handlePurchase(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Store store = gameData.getMap().getVillage().getStore(MenuToStoreString
                .convertToString(App.getCurrMenuType().getMenu()));
        Player player = gameData.getCurrentPlayer();
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
        if (p.getType().getProductPrice(gameData.getSeason()) * n > player.getMoney(gameData)) {
            return new Response(false, "Not enough money");
        }
        ItemType type = p.getType().getItemType();
        if (type == null && p.getType().getIngredient() == null) {
            if (p.getType().getName().equals("Large Pack")) {
                player.getInventory().setType(BackpackType.GIANT);
                player.setMoney((int) (player.getMoney(gameData) - p.getType().getProductPrice(gameData.getSeason())), gameData);
                p.setAvailableCount(p.getAvailableCount() - n);
                return new Response(true, "Your backpack is Large now");
            } else if (p.getType().getName().equals("Deluxe Pack")) {
                if (player.getInventory().getType() != BackpackType.DEFAULT) {
                    player.getInventory().setType(BackpackType.DELUXE);
                    player.setMoney((int) (player.getMoney(gameData) - p.getType().getProductPrice(gameData.getSeason())), gameData);
                } else {
                    return new Response(false, "You must first by Large Pack");
                }
                p.setAvailableCount(p.getAvailableCount() - n);
                return new Response(true, "Your backpack is Deluxe now");
            }
            Response res = handleBuyRecipe(productName, p, player);
            if (res.isSuccess()) {
                p.setAvailableCount(p.getAvailableCount() - n);
                player.setMoney((int) (player.getMoney(gameData) - p.getType().getProductPrice(gameData.getSeason()) * n), gameData);
            }
            GameRepository.saveGame(gameData);
            return res;
        } else if (type == null && p.getType().getIngredient() != null) {
            Response res = handleUpgradeTool(productName, p, player);
            if (res.isSuccess()) {
                player.setMoney((int) (player.getMoney(gameData) - p.getType().getProductPrice(gameData.getSeason())), gameData);
            }
            GameRepository.saveGame(gameData);
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
                item = new ForagingMineralItem(Quality.DEFAULT, (ForagingMineralsType) type);
            } else if (type instanceof ToolTypes) {
                Quality q = Quality.DEFAULT;
                if (p.getType().getName().equals(FishProducts.BAMBOO_POLE.getName())) {
                    q = Quality.SILVER;
                } else if (p.getType().getName().equals(FishProducts.TRAINING_ROD.getName())) {
                    q = Quality.COPPER;
                } else if (p.getType().getName().equals(FishProducts.FIBERGLASS_ROD.getName())) {
                    q = Quality.GOLD;
                } else if (p.getType().getName().equals(FishProducts.IRIDIUM_ROD.getName())) {
                    q = Quality.IRIDIUM;
                }
                item = new Tool(q, (ToolTypes) type, (int) p.getType().getProductPrice(gameData.getSeason()));
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
            player.setMoney((int) (player.getMoney(gameData) - p.getType().getProductPrice(gameData.getSeason()) * n), gameData);
            p.setAvailableCount(p.getAvailableCount() - n);
            GameRepository.saveGame(gameData);
            return new Response(true, "Purchased successfully");
        }
    }

    public static Response handleCheatAddDollars(Request request) {
        int count = Integer.parseInt(request.body.get("count"));
        GameData gameData = App.getLoggedInUser().getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        player.setMoney(player.getMoney(gameData) + count, gameData);
        return new Response(true, "Money added successfully");
    }

    public static Response handleSellProduct(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        String productName = request.body.get("productName");


        if (!player.isNearShippingBin()) {
            return new Response(false, "You must be near a shipping bin");
        }
        Slot productSlot = player.getInventory().getSlotByItemName(productName);
        if (productSlot == null) {
            return new Response(false, "No such product");
        }
        int n = productSlot.getCount();
        if (request.body.get("count") != null) {
            n = Integer.parseInt(request.body.get("count"));
        }
        if (n > productSlot.getCount()) {
            return new Response(false, "You don't have enough item");
        }
        productSlot.setCount(productSlot.getCount() - n);
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
        GameRepository.saveGame(gameData);
        return new Response(true, "Item was successfully sold");
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

    public static Response handleUpgradeTool(String name, StoreProduct p, Player player) {
        BlackSmithProducts trashCan = BlackSmithProducts.findTrashCanUpgrade(name);
        BlackSmithProducts tool = BlackSmithProducts.findSteelToolUpgrade(name);
        if (trashCan != null) {
            TrashcanType type = trashCan.getTrashcan();
            player.setTrashcanType(type);
            return new Response(true, "Trashcan successfully updated");
        } else if (tool != null) {
            Quality q = tool.getTool();
            if (player.getEquippedItem() instanceof Tool t) {
                p.setAvailableCount(p.getAvailableCount() - 1);
                Slot slot = player.getInventory().getSlotByItemName(p.getType().getIngredient().getName());
                if (slot == null || slot.getCount() < 5) {
                    return new Response(false, "you don't have enough ingredients");
                }
                slot.setCount(slot.getCount() - 5);
                if (slot.getCount() == 0) {
                    player.getInventory().removeSlot(slot);
                }
                Slot s = player.getInventory().getSlotByItemName(t.getName());
                t.setQuality(q);
                ((Tool)s.getItem()).setQuality(q);
                return new Response(true, "Tool successfully updated");
            } else {
                return new Response(false, "Your equipped item must be a tool");
            }
        }
        return new Response(false, "Upgrade option not found");
    }

    public static Response handleLeaveShop(Request request) {
        String name = App.getCurrMenuType().name();
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "Leaving " + name + " ...");
    }
}
