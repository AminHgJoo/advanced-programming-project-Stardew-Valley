package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.items.Food;
import com.example.models.mapModels.Farm;
import com.example.models.mapObjects.ArtisanBlock;
import org.jetbrains.annotations.NotNull;

public class ArtisanController extends Controller {
    public static Response handleArtisanUse(Request request) {
        String artisanName = request.body.get("artisanName");
        String item1Name = request.body.get("item1Name");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getFarm();
        Player player = game.getCurrentPlayer();
        Backpack backpack = player.getInventory();
        ArtisanBlock block = farm.getArtisanBlock(artisanName);
        if (block == null) {
            GameRepository.saveGame(game);
            return new Response(false, "Artisan does not exist");
        }
        if (artisanName.equals("Bee House")) {
            return honeyHandle(player, game, block);
        } else if (artisanName.equals("Cheese Press")) {
            return cheesePress(item1Name, player, game, block, backpack);
        } else if (artisanName.equals("Keg")) {
            return keg(item1Name, player, game, backpack, block);
        }

        return null;
    }

    private static @NotNull Response keg(String item1Name, Player player, Game game, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals("Wheat"))
            return artisanHandle(player, game, backpack, item1Name, block, 50, 24, FoodTypes.BEER, 1, 1, FoodTypes.BEER.value);
        else if (item1Name.equals("Rice"))
            return artisanHandle(player, game, backpack, item1Name, block, 13, 10, FoodTypes.VINEGAR, 1, 1, FoodTypes.VINEGAR.value);
        else if (item1Name.equals("Coffee Bean"))
            return artisanHandle(player, game, backpack, item1Name, block, 75, 2, FoodTypes.COFFEE, 5, 1, FoodTypes.COFFEE.value);
        else if (item1Name.equals("Honey"))
            return artisanHandle(player, game, backpack, item1Name, block, 100, 10, FoodTypes.MEAD, 5, 1, 300);
        else if (item1Name.equals("Hops"))
            return artisanHandle(player, game, backpack, item1Name, block, 50, 72, FoodTypes.PALE_ALE, 1, 1, 300);
        else if (isFruit(item1Name))
            return artisanHandle(player, game, backpack, item1Name, block, 7 * FoodTypes.getEnergy(item1Name) / 4, 168, FoodTypes.WINE, 5, 1, FoodTypes.getPrice(item1Name) * 3);
        else if (isVegetable(item1Name))
            return artisanHandle(player, game, backpack, item1Name, block, 2 * FoodTypes.getEnergy(item1Name), 168, FoodTypes.JUICE, 5, 1, 9 * FoodTypes.getPrice(item1Name) / 4);
        else{
            GameRepository.saveGame(game);
            return new Response(true, "wrong item selected");
        }
    }

    private static boolean isFruit(String itemName) {
        return itemName.equals(FoodTypes.ANCIENT_FRUIT.name) || itemName.equals(FoodTypes.APPLE.name) ||
                itemName.equals(FoodTypes.APRICOT.name) || itemName.equals(FoodTypes.BANANA.name) ||
                itemName.equals(FoodTypes.BLACKBERRY.name) || itemName.equals(FoodTypes.CHERRY.name) ||
                itemName.equals(FoodTypes.CRANBERRIES.name) || itemName.equals(FoodTypes.CRYSTAL_FRUIT.name) ||
                itemName.equals(FoodTypes.GRAPE.name) || itemName.equals(FoodTypes.BLUEBERRY.name) ||
                itemName.equals(FoodTypes.HOT_PEPPER.name) || itemName.equals(FoodTypes.MANGO.name) ||
                itemName.equals(FoodTypes.MELON.name) || itemName.equals(FoodTypes.ORANGE.name) ||
                itemName.equals(FoodTypes.PEACH.name) || itemName.equals(FoodTypes.POMEGRANATE.name) ||
                itemName.equals(FoodTypes.POWDER_MELON.name) || itemName.equals(FoodTypes.RHUBARB.name) ||
                itemName.equals(FoodTypes.SALMON_BERRY.name) || itemName.equals(FoodTypes.SPICE_BERRY.name) ||
                itemName.equals(FoodTypes.STARFRUIT.name) || itemName.equals(FoodTypes.STRAWBERRY.name) ||
                itemName.equals(FoodTypes.WILD_PLUM.name)
                ;
    }

    private static boolean isVegetable(String itemName) {
        return itemName.equals(FoodTypes.AMARANTH.name) || itemName.equals(FoodTypes.ARTICHOKE.name) ||
                itemName.equals(FoodTypes.BEET.name) || itemName.equals(FoodTypes.BOK_CHOY.name) ||
                itemName.equals(FoodTypes.BROCCOLI.name) || itemName.equals(FoodTypes.CARROT.name) ||
                itemName.equals(FoodTypes.CAULIFLOWER.name) || itemName.equals(FoodTypes.CORN.name) ||
                itemName.equals(FoodTypes.EGGPLANT.name) || itemName.equals(FoodTypes.FIDDLE_HEAD_FERN.name) ||
                itemName.equals(FoodTypes.GARLIC.name) || itemName.equals(FoodTypes.GREEN_BEAN.name) ||
                itemName.equals(FoodTypes.HOPS.name) || itemName.equals(FoodTypes.KALE.name) ||
                itemName.equals(FoodTypes.PUMPKIN.name) || itemName.equals(FoodTypes.RADISH.name) ||
                itemName.equals(FoodTypes.RED_CABBAGE.name) || itemName.equals(FoodTypes.SUMMER_SQUASH.name) ||
                itemName.equals(FoodTypes.TOMATO.name) || itemName.equals(FoodTypes.UNMILLED_RICE.name) ||
                itemName.equals(FoodTypes.WHEAT.name) || itemName.equals(FoodTypes.YAM.name);
    }

    private static @NotNull Response artisanHandle(Player player, Game game, Backpack backpack, String item1Name, ArtisanBlock block, int energy, int hours, FoodTypes foodTypes, int itemCount, int productCount, int price) {
        if (player.getEnergy() < energy) {
            GameRepository.saveGame(game);
            return new Response(false, "you don't have enough energy");
        }

        Slot slot = backpack.getSlotByItemName(item1Name);
        if (slot == null || slot.getCount() < itemCount) {
            GameRepository.saveGame(game);
            return new Response(false, "You don't have enough " + item1Name);
        }
        slot.setCount(slot.getCount() - itemCount);
        if (slot.getCount() == 0) {
            backpack.removeSlot(slot);
        }
        player.setEnergy(player.getEnergy() - energy);
        block.beingUsed = true;
        block.PrepTime = game.getDate().plusHours(hours);
        block.canBeCollected = false;
        block.productSlot = new Slot(new Food(Quality.DEFAULT, foodTypes, price), productCount);
        GameRepository.saveGame(game);
        return new Response(true, foodTypes.name + " will be ready to collect in " + hours + " hours");
    }

    private static @NotNull Response cheesePress(String item1Name, Player player, Game game, ArtisanBlock block, Backpack backpack) {
        if (item1Name.equals("Milk") || item1Name.equals("Big Milk"))
            return cheeseHandle(player, game, item1Name, block, backpack);
        else if (item1Name.equals("Goat Milk") || item1Name.equals("Big Goat Milk"))
            return goatCheeseHandle(player, game, item1Name, backpack, block);
        else {
            GameRepository.saveGame(game);
            return new Response(true, "wrong item selected");
        }
    }

    private static @NotNull Response goatCheeseHandle(Player player, Game game, String item1Name, Backpack backpack, ArtisanBlock block) {
        if (player.getEnergy() < 100) {
            GameRepository.saveGame(game);
            return new Response(false, "You don't have enough energy");
        }
        if (item1Name.equals("Goat Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have enough Goat Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            player.setEnergy(player.getEnergy() - 100);
            block.beingUsed = true;
            block.PrepTime = game.getDate().plusHours(3);
            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.GOAT_CHEESE, 400), 1);
            GameRepository.saveGame(game);
            return new Response(true, "goat cheese will be ready to collect in 3 hours");
        } else if (item1Name.equals("Big Goat Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have enough Big Goat Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            player.setEnergy(player.getEnergy() - 100);
            block.beingUsed = true;
            block.PrepTime = game.getDate().plusHours(3);
            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.GOAT_CHEESE, 600), 1);
            GameRepository.saveGame(game);
            return new Response(true, "goat cheese will be ready to collect in 3 hours");
        } else {
            GameRepository.saveGame(game);
            return new Response(true, "wrong item selected");
        }
    }

    private static @NotNull Response honeyHandle(Player player, Game game, ArtisanBlock block) {
        if (player.getEnergy() < 75) {
            GameRepository.saveGame(game);
            return new Response(false, "You don't have enough energy");
        }
        player.setEnergy(player.getEnergy() - 75);
        block.beingUsed = true;
        block.PrepTime = game.getDate().plusDays(4);
        block.canBeCollected = false;
        block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.HONEY), 1);
        GameRepository.saveGame(game);
        return new Response(true, "honey will be ready to collect in 4 days");
    }

    private static @NotNull Response cheeseHandle(Player player, Game game, String item1Name, ArtisanBlock block, Backpack backpack) {
        if (player.getEnergy() < 100) {
            GameRepository.saveGame(game);
            return new Response(false, "You don't have enough energy");
        }
        if (item1Name.equals("Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have enough Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            player.setEnergy(player.getEnergy() - 100);
            block.beingUsed = true;
            block.PrepTime = game.getDate().plusHours(3);
            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.CHEESE, 230), 1);
            GameRepository.saveGame(game);
            return new Response(true, "cheese will be ready to collect in 3 hours");
        } else if (item1Name.equals("Big Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(game);
                return new Response(false, "You don't have enough Big Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            player.setEnergy(player.getEnergy() - 100);
            block.beingUsed = true;
            block.PrepTime = game.getDate().plusHours(3);
            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.CHEESE, 345), 1);
            GameRepository.saveGame(game);
            return new Response(true, "cheese will be ready to collect in 3 hours");
        } else {
            GameRepository.saveGame(game);
            return new Response(true, "wrong item selected");
        }
    }

    public static Response handleArtisanGet(Request request) {
        return null;
    }
}
