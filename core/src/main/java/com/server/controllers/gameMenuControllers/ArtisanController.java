package com.server.controllers.gameMenuControllers;

import com.common.models.*;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.FishType;
import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.items.Food;
import com.common.models.items.ForagingMineralItem;
import com.common.models.items.Misc;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.ArtisanBlock;
import com.common.repositories.GameRepository;
import com.server.controllers.Controller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class ArtisanController extends Controller {

    public static Response handleArtisanUse(Request request) {
        String artisanName = request.body.get("artisanName");
        String item1Name = request.body.getOrDefault("item1Name", " ");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Farm farm = gameData.getCurrentPlayer().getCurrentFarm(gameData);
        Player player = gameData.getCurrentPlayer();
        Backpack backpack = player.getInventory();
        ArtisanBlock block = farm.getArtisanBlock(artisanName);
        if (block == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "Artisan does not exist");
        }
        if (block.beingUsed) {
            GameRepository.saveGame(gameData);
            return new Response(false, "Artisan already used");
        }
        if (artisanName.equals("Bee House")) {
            return honeyHandle(player, gameData, block);
        } else if (artisanName.equals("Cheese Press")) {
            return cheesePress(item1Name, player, gameData, block, backpack);
        } else if (artisanName.equals("Keg")) {
            return keg(item1Name, player, gameData, backpack, block);
        } else if (artisanName.equals("Dehydrator")) {
            return Dehydrator(item1Name, player, gameData, backpack, block);
        } else if (artisanName.equals("Charcoal Klin")) {
            return charCoalKiln(item1Name, player, gameData, backpack, block);
        } else if (artisanName.equals("Loom")) {
            loom(item1Name, player, gameData, backpack, block);
        } else if (artisanName.equals("Mayonnaise Machine")) {
            return mayonnaiseMachine(item1Name, player, gameData, backpack, block);
        } else if (artisanName.equals("Oil Maker")) {
            return oilMaker(item1Name, player, gameData, backpack, block);
        } else if (artisanName.equals("Preserves Jar")) {
            return preservesJar(item1Name, player, gameData, backpack, block);
        } else if (artisanName.equals("Fish Smoker")) {
            return fishSmoker(item1Name, player, gameData, backpack, block);
        }
        return furnace(item1Name, player, gameData, backpack, block);

    }

    private static @Nullable Response furnace(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals(ForagingMineralsType.COPPER_ORE.name)) {
            return artisanMiscHandleCoal(player, gameData, backpack, item1Name, block, 0, 4, MiscType.COPPER_BAR, 5, 1, 10 * ForagingMineralsType.getPriceByName(item1Name));
        } else if (item1Name.equals(ForagingMineralsType.GOLD_ORE.name)) {
            return artisanMiscHandleCoal(player, gameData, backpack, item1Name, block, 0, 4, MiscType.GOLD_BAR, 5, 1, 10 * ForagingMineralsType.getPriceByName(item1Name));
        } else if (item1Name.equals(ForagingMineralsType.IRON_ORE.name)) {
            return artisanMiscHandleCoal(player, gameData, backpack, item1Name, block, 0, 4, MiscType.IRON_BAR, 5, 1, 10 * ForagingMineralsType.getPriceByName(item1Name));
        } else if (item1Name.equals(ForagingMineralsType.IRIDIUM_ORE.name)) {
            return artisanMiscHandleCoal(player, gameData, backpack, item1Name, block, 0, 4, MiscType.IRIDIUM_BAR, 5, 1, 10 * ForagingMineralsType.getPriceByName(item1Name));
        }
        return wrongItem(gameData);
    }

    private static @Nullable Response fishSmoker(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (FishType.isFish(item1Name)) {
            // fish energy ?
            return artisanFoodHandleCoal(player, gameData, backpack, item1Name, block, 20, 1, FoodTypes.SMOKED_FISH, 1, 1, 2 * Objects.requireNonNull(FishType.getFishType(item1Name)).price);
        } else {
            wrongItem(gameData);
        }
        return null;
    }

    private static @NotNull Response preservesJar(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (isVegetable(item1Name)) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 7 * FoodTypes.getEnergy(item1Name) / 4, 6, FoodTypes.PICKLES, 1, 1, 2 * FoodTypes.getPrice(item1Name) + 50);
        } else if (isFruit(item1Name)) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 2 * FoodTypes.getEnergy(item1Name), 72, FoodTypes.JELLY, 1, 1, 2 * FoodTypes.getPrice(item1Name) + 50);
        } else {
            return wrongItem(gameData);
        }
    }

    private static @NotNull Response wrongItem(GameData gameData) {
        GameRepository.saveGame(gameData);
        return new Response(false, "wrong item selected");
    }

    private static @NotNull Response oilMaker(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals("Truffle")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 38, 6, FoodTypes.TRUFFLE_OIL, 1, 1, 1065);
        } else if (item1Name.equals("Corn")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 13, 6, FoodTypes.OIL, 1, 1, 100);
        } else if (item1Name.equals("Sunflower Seeds")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 13, 48, FoodTypes.OIL, 1, 1, 100);
        } else if (item1Name.equals("Sunflower")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 13, 1, FoodTypes.OIL, 1, 1, 100);
        } else {
            return wrongItem(gameData);
        }
    }

    private static @NotNull Response mayonnaiseMachine(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals("Egg")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 50, 3, FoodTypes.MAYONNAISE, 1, 1, 190);
        } else if (item1Name.equals("Big Egg")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 50, 3, FoodTypes.MAYONNAISE, 1, 1, 237);
        } else if (item1Name.equals("Duck Egg")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 75, 3, FoodTypes.DUCK_MAYONNAISE, 1, 1, 37);
        } else if (item1Name.equals("Dinosaur Egg")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 125, 3, FoodTypes.DINOSAUR_MAYONNAISE, 1, 1, 800);
        } else {
            return wrongItem(gameData);
        }
    }

    private static void loom(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals("Wool")) {
            artisanMiscHandle(player, gameData, backpack, item1Name, block, 0, 4, MiscType.CLOTH, 1, 1, 470);
        } else {
            GameRepository.saveGame(gameData);
        }
    }

    private static @NotNull Response artisanMiscHandle(Player player, GameData gameData, Backpack backpack, String item1Name, ArtisanBlock block, int energy, int hours, MiscType miscType, int itemCount, int productCount, int price) {

        Slot slot = backpack.getSlotByItemName(item1Name);
        if (slot == null || slot.getCount() < itemCount) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough " + item1Name);
        }

        slot.setCount(slot.getCount() - itemCount);
        if (slot.getCount() == 0) {
            backpack.removeSlot(slot);
        }
        block.beingUsed = true;
        block.prepTime = gameData.getDate().plusHours(hours);

        if (block.prepTime.getDayOfMonth() >= 29) {
            block.prepTime = block.prepTime.minusDays(28);
            block.prepTime = block.prepTime.plusMonths(1);
        }

        block.canBeCollected = false;
        block.productSlot = new Slot(new Misc(Quality.DEFAULT, miscType, price), productCount);
        GameRepository.saveGame(gameData);
        return new Response(true, miscType.name + " will be ready to collect in " + hours + " hours");
    }

    private static @NotNull Response charCoalKiln(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals("Wood")) {
            return artisanForagingMineralHandle(player, gameData, backpack, item1Name, block, 0, 1, ForagingMineralsType.COAL, 10, 1, 50);
        } else {
            GameRepository.saveGame(gameData);
            return new Response(true, "wrong item selected");
        }
    }

    private static @NotNull Response Dehydrator(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (isMushroom(item1Name)) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 50, Duration.between(gameData.getDate(), gameData.getDate().plusDays(1).with(LocalTime.of(0, 0))).toHoursPart(), FoodTypes.DRIED_MUSHROOMS, 5, 1, FoodTypes.getPrice(item1Name) * 75 / 100 + 25);
        } else if (isFruit(item1Name) && !item1Name.equals("Grapes")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 75, Duration.between(gameData.getDate(), gameData.getDate().plusDays(1).with(LocalTime.of(0, 0))).toHoursPart(), FoodTypes.DRIED_FRUIT, 5, 1, FoodTypes.getPrice(item1Name) * 75 / 100 + 25);
        } else if (item1Name.equals("Grapes")) {
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 125, Duration.between(gameData.getDate(), gameData.getDate().plusDays(1).with(LocalTime.of(0, 0))).toHoursPart(), FoodTypes.RAISINS, 5, 1, 600);
        } else {
            GameRepository.saveGame(gameData);
            return new Response(true, "wrong item selected");
        }
    }

    private static boolean isMushroom(String itemName) {
        return itemName.equals(FoodTypes.CHANTERELLE.name) || itemName.equals(FoodTypes.COMMON_MUSHROOM.name) ||
            itemName.equals(FoodTypes.MOREL.name) || itemName.equals(FoodTypes.PURPLE_MUSHROOM.name) ||
            itemName.equals(FoodTypes.RED_MUSHROOM.name);
    }

    private static @NotNull Response keg(String item1Name, Player player, GameData gameData, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals("Wheat"))
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 50, 24, FoodTypes.BEER, 1, 1, FoodTypes.BEER.value);
        else if (item1Name.equals("Rice"))
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 13, 10, FoodTypes.VINEGAR, 1, 1, FoodTypes.VINEGAR.value);
        else if (item1Name.equals("Coffee Bean"))
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 75, 2, FoodTypes.COFFEE, 5, 1, FoodTypes.COFFEE.value);
        else if (item1Name.equals("Honey"))
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 100, 10, FoodTypes.MEAD, 1, 1, 300);
        else if (item1Name.equals("Hops"))
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 50, 72, FoodTypes.PALE_ALE, 1, 1, 300);
        else if (isFruit(item1Name))
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 7 * FoodTypes.getEnergy(item1Name) / 4, 168, FoodTypes.WINE, 1, 1, FoodTypes.getPrice(item1Name) * 3);
        else if (isVegetable(item1Name))
            return artisanFoodHandle(player, gameData, backpack, item1Name, block, 2 * FoodTypes.getEnergy(item1Name), 168, FoodTypes.JUICE, 1, 1, 9 * FoodTypes.getPrice(item1Name) / 4);
        else {
            GameRepository.saveGame(gameData);
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

    private static @NotNull Response artisanForagingMineralHandle(Player player, GameData gameData, Backpack backpack, String item1Name, ArtisanBlock block, int energy, int hours, ForagingMineralsType foragingMineralsType, int itemCount, int productCount, int price) {

        Slot slot = backpack.getSlotByItemName(item1Name);
        if (slot == null || slot.getCount() < itemCount) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough " + item1Name);
        }
        slot.setCount(slot.getCount() - itemCount);
        if (slot.getCount() == 0) {
            backpack.removeSlot(slot);
        }
        block.beingUsed = true;
        block.prepTime = gameData.getDate().plusHours(hours);

        if (block.prepTime.getDayOfMonth() >= 29) {
            block.prepTime = block.prepTime.minusDays(28);
            block.prepTime = block.prepTime.plusMonths(1);
        }

        block.canBeCollected = false;
        block.productSlot = new Slot(new ForagingMineralItem(Quality.DEFAULT, foragingMineralsType, price), productCount);
        GameRepository.saveGame(gameData);
        return new Response(true, foragingMineralsType.name + " will be ready to collect in " + hours + " hours");
    }

    private static @NotNull Response artisanFoodHandle(Player player, GameData gameData, Backpack backpack, String item1Name, ArtisanBlock block, int energy, int hours, FoodTypes foodTypes, int itemCount, int productCount, int price) {

        Slot slot = backpack.getSlotByItemName(item1Name);
        if (slot == null || slot.getCount() < itemCount) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough " + item1Name);
        }
        slot.setCount(slot.getCount() - itemCount);
        if (slot.getCount() == 0) {
            backpack.removeSlot(slot);
        }
        block.beingUsed = true;
        block.prepTime = gameData.getDate().plusHours(hours);

        if (block.prepTime.getDayOfMonth() >= 29) {
            block.prepTime = block.prepTime.minusDays(28);
            block.prepTime = block.prepTime.plusMonths(1);
        }

        block.canBeCollected = false;
        block.productSlot = new Slot(new Food(Quality.DEFAULT, foodTypes, price, energy), productCount);
        GameRepository.saveGame(gameData);
        return new Response(true, foodTypes.name + " will be ready to collect in " + hours + " hours");
    }

    private static @NotNull Response artisanFoodHandleCoal(Player player, GameData gameData, Backpack backpack, String item1Name, ArtisanBlock block, int energy, int hours, FoodTypes foodTypes, int itemCount, int productCount, int price) {

        Slot slot = backpack.getSlotByItemName(item1Name);
        if (slot == null || slot.getCount() < itemCount) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough " + item1Name);
        }
        Slot secondSlot = backpack.getSlotByItemName("Coal");
        if (secondSlot == null || secondSlot.getCount() < itemCount) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough coal");
        }
        slot.setCount(slot.getCount() - itemCount);
        if (slot.getCount() == 0) {
            backpack.removeSlot(slot);
        }
        secondSlot.setCount(secondSlot.getCount() - itemCount);
        if (secondSlot.getCount() == 0) {
            backpack.removeSlot(secondSlot);
        }

        block.beingUsed = true;
        block.prepTime = gameData.getDate().plusHours(hours);

        if (block.prepTime.getDayOfMonth() >= 29) {
            block.prepTime = block.prepTime.minusDays(28);
            block.prepTime = block.prepTime.plusMonths(1);
        }

        block.canBeCollected = false;
        block.productSlot = new Slot(new Food(Quality.DEFAULT, foodTypes, price, energy), productCount);
        GameRepository.saveGame(gameData);
        return new Response(true, foodTypes.name + " will be ready to collect in " + hours + " hours");
    }

    private static @NotNull Response artisanMiscHandleCoal(Player player, GameData gameData, Backpack backpack, String item1Name, ArtisanBlock block, int energy, int hours, MiscType miscType, int itemCount, int productCount, int price) {

        Slot slot = backpack.getSlotByItemName(item1Name);
        if (slot == null || slot.getCount() < itemCount) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough " + item1Name);
        }
        Slot secondSlot = backpack.getSlotByItemName("Coal");
        if (secondSlot == null || secondSlot.getCount() < 1) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough coal");
        }
        slot.setCount(slot.getCount() - itemCount);
        if (slot.getCount() == 0) {
            backpack.removeSlot(slot);
        }
        secondSlot.setCount(secondSlot.getCount() - 1);
        if (secondSlot.getCount() == 0) {
            backpack.removeSlot(secondSlot);
        }

        block.beingUsed = true;
        block.prepTime = gameData.getDate().plusHours(hours);

        if (block.prepTime.getDayOfMonth() >= 29) {
            block.prepTime = block.prepTime.minusDays(28);
            block.prepTime = block.prepTime.plusMonths(1);
        }

        block.canBeCollected = false;
        block.productSlot = new Slot(new Misc(Quality.DEFAULT, miscType, price), productCount);
        GameRepository.saveGame(gameData);
        return new Response(true, miscType.name + " will be ready to collect in " + hours + " hours");
    }

    private static @NotNull Response cheesePress(String item1Name, Player player, GameData gameData, ArtisanBlock block, Backpack backpack) {
        if (item1Name.equals("Milk") || item1Name.equals("Big Milk"))
            return cheeseHandle(player, gameData, item1Name, block, backpack);
        else if (item1Name.equals("Goat Milk") || item1Name.equals("Big Goat Milk"))
            return goatCheeseHandle(player, gameData, item1Name, backpack, block);
        else {
            GameRepository.saveGame(gameData);
            return new Response(true, "wrong item selected");
        }
    }

    private static @NotNull Response goatCheeseHandle(Player player, GameData gameData, String item1Name, Backpack backpack, ArtisanBlock block) {
        if (item1Name.equals("Goat Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(gameData);
                return new Response(false, "You don't have enough Goat Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            block.beingUsed = true;
            block.prepTime = gameData.getDate().plusHours(3);

            if (block.prepTime.getDayOfMonth() >= 29) {
                block.prepTime = block.prepTime.minusDays(28);
                block.prepTime = block.prepTime.plusMonths(1);
            }

            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.GOAT_CHEESE, 400), 1);
            GameRepository.saveGame(gameData);
            return new Response(true, "goat cheese will be ready to collect in 3 hours");
        } else if (item1Name.equals("Big Goat Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(gameData);
                return new Response(false, "You don't have enough Big Goat Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            block.beingUsed = true;
            block.prepTime = gameData.getDate().plusHours(3);

            if (block.prepTime.getDayOfMonth() >= 29) {
                block.prepTime = block.prepTime.minusDays(28);
                block.prepTime = block.prepTime.plusMonths(1);
            }

            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.GOAT_CHEESE, 600), 1);
            GameRepository.saveGame(gameData);
            return new Response(true, "goat cheese will be ready to collect in 3 hours");
        } else {
            GameRepository.saveGame(gameData);
            return new Response(true, "wrong item selected");
        }
    }

    private static @NotNull Response honeyHandle(Player player, GameData gameData, ArtisanBlock block) {
        block.beingUsed = true;
        block.prepTime = gameData.getDate().plusDays(4);

        if (block.prepTime.getDayOfMonth() >= 29) {
            block.prepTime = block.prepTime.minusDays(28);
            block.prepTime = block.prepTime.plusMonths(1);
        }

        block.canBeCollected = false;
        block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.HONEY), 1);
        GameRepository.saveGame(gameData);
        return new Response(true, "honey will be ready to collect in 4 days");
    }

    private static @NotNull Response cheeseHandle(Player player, GameData gameData, String item1Name, ArtisanBlock block, Backpack backpack) {
        if (item1Name.equals("Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(gameData);
                return new Response(false, "You don't have enough Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            block.beingUsed = true;
            block.prepTime = gameData.getDate().plusHours(3);

            if (block.prepTime.getDayOfMonth() >= 29) {
                block.prepTime = block.prepTime.minusDays(28);
                block.prepTime = block.prepTime.plusMonths(1);
            }

            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.CHEESE, 230), 1);
            GameRepository.saveGame(gameData);
            return new Response(true, "cheese will be ready to collect in 3 hours");
        } else if (item1Name.equals("Big Milk")) {
            Slot slot = backpack.getSlotByItemName(item1Name);
            if (slot == null) {
                GameRepository.saveGame(gameData);
                return new Response(false, "You don't have enough Big Milk");
            }
            slot.setCount(slot.getCount() - 1);
            if (slot.getCount() == 0) {
                backpack.removeSlot(slot);
            }
            block.beingUsed = true;
            block.prepTime = gameData.getDate().plusHours(3);

            if (block.prepTime.getDayOfMonth() >= 29) {
                block.prepTime = block.prepTime.minusDays(28);
                block.prepTime = block.prepTime.plusMonths(1);
            }

            block.canBeCollected = false;
            block.productSlot = new Slot(new Food(Quality.DEFAULT, FoodTypes.CHEESE, 345), 1);
            GameRepository.saveGame(gameData);
            return new Response(true, "cheese will be ready to collect in 3 hours");
        } else {
            GameRepository.saveGame(gameData);
            return new Response(true, "wrong item selected");
        }
    }

    public static Response handleArtisanGet(Request request) {
        String artisanName = request.body.get("artisanName");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Farm farm = gameData.getCurrentPlayer().getCurrentFarm(gameData);
        Player player = gameData.getCurrentPlayer();
        Backpack backpack = player.getInventory();
        ArtisanBlock block = farm.getArtisanBlock(artisanName);
        if (block == null) {
            GameRepository.saveGame(gameData);
            return new Response(true, "artisan not found");
        }
        if (!block.beingUsed) {
            GameRepository.saveGame(gameData);
            return new Response(true, "no product found");
        }
        if (!block.canBeCollected) {
            GameRepository.saveGame(gameData);
            return new Response(true, "The product is not ready for collection");
        }
        Slot slot = block.productSlot;
        Slot backPackSlot = backpack.getSlotByItemName(artisanName);
        if (backPackSlot == null) {
            if (backpack.getSlots().size() == backpack.getType().getMaxCapacity()) {
                GameRepository.saveGame(gameData);
                return new Response(true, "your backpack is full");
            }
            Slot slotToAdd = new Slot(slot.getItem(), slot.getCount());
            backpack.addSlot(slotToAdd);
            block.beingUsed = false;
            block.canBeCollected = false;
            block.productSlot = null;
            GameRepository.saveGame(gameData);
            return new Response(true, "you have collected " + slotToAdd.getCount() + " of " + slotToAdd.getItem().getName());
        }
        backPackSlot.setCount(backPackSlot.getCount() + slot.getCount());
        int count = slot.getCount();
        String itemName = backPackSlot.getItem().getName();
        block.beingUsed = false;
        block.canBeCollected = false;
        block.productSlot = null;
        GameRepository.saveGame(gameData);
        return new Response(true, "you have collected " + count + " of " + itemName);
    }
}
