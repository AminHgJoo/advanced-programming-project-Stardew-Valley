package com.server.controllers.gameMenuControllers;

import com.common.models.*;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.buildings.Barn;
import com.common.models.buildings.Building;
import com.common.models.buildings.Coop;
import com.common.models.enums.types.AnimalType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.enums.worldEnums.Weather;
import com.common.models.items.Item;
import com.common.models.items.Misc;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.AnimalBlock;
import com.common.models.mapObjects.BuildingBlock;
import com.common.models.mapObjects.EmptyCell;
import com.server.repositories.GameRepository;
import com.server.controllers.Controller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LivestockController extends Controller {
    public static Response handleBuyAnimal(Request request) {
        return MarineRanchController.buyAnimal(request);
    }

    public static Response handlePet(Request request) {
        String petName = request.body.get("name");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Animal animal = player.getAnimalByName(petName);
        if (animal == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "Animal not found");
        }
        animal.hasBeenPetToDay = true;
        animal.setXp(animal.getXp() + 15);
        GameRepository.saveGame(gameData);
        return new Response(true, "you have pet " + petName);
    }

    public static Response handleCheatSetFriendship(Request request) {
        String animalName = request.body.get("animalName");
        int amount = Integer.parseInt(request.body.get("amount"));
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Animal animal = player.getAnimalByName(animalName);
        if (animal == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "Animal not found");
        }
        animal.setXp(amount);
        GameRepository.saveGame(gameData);
        return new Response(true, "your xp is: " + animal.getXp());
    }

    public static Response handleAnimals(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        ArrayList<Animal> animals = player.getAnimals();
        if (animals.isEmpty()) {
            GameRepository.saveGame(gameData);
            return new Response(false, "No animals found");
        }
        StringBuilder animalString = new StringBuilder();
        for (Animal animal : animals) {
            animalString.append("name: ").
                append(animal.getName()).append("\n").
                append("friendship level: ").append(animal.getXp()).append("\n").
                append("has Been Pet Today: ").append(animal.hasBeenPetToDay).append("\n").
                append("has Been Fed Today: ").append(animal.hasBeenFedByHay || animal.hasBeenFedByGrass).append("\n").
                append("friendship xp: ").append(animal.getXp()).append("\n\n");
        }
        GameRepository.saveGame(gameData);
        return new Response(true, animalString.toString());
    }

    public static Response handleShepherd(Request request) {
        String animalName = request.body.get("animalName");
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Farm farm = gameData.getCurrentPlayer().getFarm();
        Animal animal = player.getAnimalByName(animalName);
        Cell cell = farm.findCellByCoordinate(x, y);
        Cell animalBlock = farm.getAnimalBlock(animal);

        if (!animal.isInside) {
            for (Building b : farm.getBuildings()) {
                if (b instanceof Barn && b.buildingCells.contains(cell)) {
                    if (((Barn) b).barnType.equals("Barn") && animal.getType().equals(AnimalType.COW)) {
                        animalBlock.setObjectOnCell(new EmptyCell());
                        cell.setObjectOnCell(new AnimalBlock(animal));
                        animal.isInside = true;
                        GameRepository.saveGame(gameData);
                        return new Response(true, "you have shepherd " + animal.getName());
                    } else if (((Barn) b).barnType.equals("Big Barn") && (animal.getType().equals(AnimalType.COW) ||
                        animal.getType().equals(AnimalType.GOAT))) {
                        cell.setObjectOnCell(new AnimalBlock(animal));
                        animalBlock.setObjectOnCell(new EmptyCell());
                        animal.isInside = true;
                        GameRepository.saveGame(gameData);
                        return new Response(true, "you have shepherd " + animal.getName());
                    } else if (((Barn) b).barnType.equals("Deluxe Barn") && (animal.getType().equals(AnimalType.COW) ||
                        animal.getType().equals(AnimalType.GOAT) || animal.getType().equals(AnimalType.SHEEP) ||
                        animal.getType().equals(AnimalType.PIG))) {
                        cell.setObjectOnCell(new AnimalBlock(animal));
                        animalBlock.setObjectOnCell(new EmptyCell());
                        animal.isInside = true;
                        GameRepository.saveGame(gameData);
                        return new Response(true, "you have shepherd " + animal.getName());
                    }
                } else if (b instanceof Coop && b.buildingCells.contains(cell)) {
                    if (((Coop) b).coopType.equals("Coop") && animal.getType().equals(AnimalType.Chicken)) {
                        cell.setObjectOnCell(new AnimalBlock(animal));
                        animalBlock.setObjectOnCell(new EmptyCell());
                        animal.isInside = true;
                        GameRepository.saveGame(gameData);
                        return new Response(true, "you have shepherd " + animal.getName());
                    } else if (((Coop) b).coopType.equals("Big Coop") && (animal.getType().equals(AnimalType.Chicken) ||
                        animal.getType().equals(AnimalType.DUCK) || animal.getType().equals(AnimalType.DINOSAUR))) {
                        cell.setObjectOnCell(new AnimalBlock(animal));
                        animalBlock.setObjectOnCell(new EmptyCell());
                        animal.isInside = true;
                        GameRepository.saveGame(gameData);
                        return new Response(true, "you have shepherd " + animal.getName());
                    } else if (((Coop) b).coopType.equals("Deluxe Coop") && (animal.getType().equals(AnimalType.Chicken) ||
                        animal.getType().equals(AnimalType.DUCK) || animal.getType().equals(AnimalType.RABBIT) ||
                        animal.getType().equals(AnimalType.DINOSAUR))) {
                        cell.setObjectOnCell(new AnimalBlock(animal));
                        animalBlock.setObjectOnCell(new EmptyCell());
                        animal.isInside = true;
                        GameRepository.saveGame(gameData);
                        return new Response(true, "you have shepherd " + animal.getName());
                    }
                }
            }
        }

        if (cell == null || !(cell.getObjectOnCell() instanceof EmptyCell) || animal == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "cell not found or not empty or no animal found");
        }
        if (!animal.isInside) {
            GameRepository.saveGame(gameData);
            return new Response(false, "animal is already outside");
        }
        if (gameData.getWeatherToday() == Weather.SNOW || gameData.getWeatherToday() == Weather.STORM || gameData.getWeatherToday() == Weather.RAIN) {
            GameRepository.saveGame(gameData);
            return new Response(true, "bad weather quality");
        }

        Building building = farm.getAnimalBuilding(animal);
        if (building instanceof Coop)
            animalBlock.setObjectOnCell(new BuildingBlock(true, ((Coop) building).coopType));
        else if (building instanceof Barn)
            animalBlock.setObjectOnCell(new BuildingBlock(true, ((Barn) building).barnType));
        cell.setObjectOnCell(new AnimalBlock(animal));
        animal.hasBeenFedByGrass = true;
        animal.isInside = false;
        animal.setXp(animal.getXp() + 8);
        GameRepository.saveGame(gameData);
        return new Response(true, "you have shepherd " + animalName);
    }

    public static Response handleFeedHay(Request request) {
        String animalName = request.body.get("animalName");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Farm farm = gameData.getCurrentPlayer().getCurrentFarm(gameData);
        Animal animal = player.getAnimalByName(animalName);
        if (animal == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "no animal found");
        }
        Backpack backpack = player.getInventory();
        Slot haySlot = null;
        for (Slot slot : backpack.getSlots()) {
            if (slot.getItem().getName().equals(MiscType.HAY.name))
                haySlot = slot;
        }
        if (haySlot == null) {
            return new Response(false, "no hay found");
        }
        haySlot.setCount(haySlot.getCount() - 1);
        if (haySlot.getCount() == 0) {
            backpack.removeSlot(haySlot);
        }
        animal.hasBeenFedByHay = true;
        GameRepository.saveGame(gameData);
        return new Response(true, "you have fed " + animalName);
    }

    public static Response handleProduces(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        ArrayList<Animal> allAnimals = gameData.getCurrentPlayer().getAnimals();
        ArrayList<Animal> desiredAnimals = new ArrayList<>();
        for (Animal animal : allAnimals) {
            if (animal.product != null) {
                desiredAnimals.add(animal);
            }
        }
        if (desiredAnimals.isEmpty()) {
            GameRepository.saveGame(gameData);
            return new Response(false, "No products found");
        }
        StringBuilder animalString = new StringBuilder();
        for (Animal animal : desiredAnimals) {
            animalString.append("name: ").append(animal.getName())
                .append(" product: ").append(animal.product.getName()).append(" ").
                append(animal.product.getQuality()).append("\n");
        }
        GameRepository.saveGame(gameData);
        return new Response(true, animalString.toString());
    }

    public static Response handleCollectProduce(Request request) {
        String animalName = request.body.get("name");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Animal animal = player.getAnimalByName(animalName);
        Item equippedItem = player.getEquippedItem();
        Backpack backpack = player.getInventory();
        Slot productSlot = null;

        if (animal == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "no animal found");
        }
        Item product = animal.product;
        if (product == null) {
            return noProductFoundHandle(animal, equippedItem, player, gameData);
        }
        return handleCollectProducts(product, backpack, productSlot, animal, player, gameData);
    }

    public static @NotNull Response handleCollectProducts(Item product, Backpack backpack, Slot productSlot, Animal animal, Player player, GameData gameData) {
        Item item = new Misc(((Misc) product).getMiscType(), ((Misc) product).getQuality());
        for (Slot slot : backpack.getSlots()) {
            if (slot.getItem().getName().equals(product.getName())) {
                productSlot = slot;
            }
        }
        if (productSlot == null) {
            return addNewSlotForProductHandle(backpack, animal, player, gameData, item, product);
        }
        return addToExistingSlotHandle(productSlot, animal, player, gameData, product);
    }

    private static @NotNull Response addToExistingSlotHandle(Slot productSlot, Animal animal, Player player, GameData gameData, Item product) {
        productSlot.setCount(productSlot.getCount() + animal.getType().productPerDay);
        if (animal.getType().equals(AnimalType.SHEEP) || animal.getType().equals(AnimalType.COW) || animal.getType().equals(AnimalType.GOAT)) {
            animal.hasBeenHarvested = true;
            animal.setXp(animal.getXp() + 15);
        }
        animal.product = null;
        GameRepository.saveGame(gameData);
        return new Response(true, "you have collected " + animal.getType().productPerDay + " of " + product.getName());
    }

    private static @NotNull Response addNewSlotForProductHandle(Backpack backpack, Animal animal, Player player, GameData gameData, Item item, Item product) {
        if (backpack.getSlots().size() == backpack.getType().getMaxCapacity()) {
            GameRepository.saveGame(gameData);
            return new Response(false, "your backpack is full");
        }
        Slot newSlot = new Slot(item, animal.getType().productPerDay);
        backpack.addSlot(newSlot);
        if (animal.getType().equals(AnimalType.SHEEP) || animal.getType().equals(AnimalType.COW) || animal.getType().equals(AnimalType.GOAT)) {
            animal.hasBeenHarvested = true;
            animal.setXp(animal.getXp() + 5);
        }
        animal.product = null;
        GameRepository.saveGame(gameData);
        return new Response(true, "you have collected " + animal.getType().productPerDay + " of " + product.getName());
    }

    public static @NotNull Response noProductFoundHandle(Animal animal, Item equippedItem, Player player, GameData gameData) {
        GameRepository.saveGame(gameData);
        return new Response(false, "no product found");
    }

    public static Response handleSellAnimal(Request request) {
        String animalName = request.body.get("name");
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        Animal animal = player.getAnimalByName(animalName);
        if (animal == null) {
            GameRepository.saveGame(gameData);
            return new Response(false, "no animal found");
        }
        int price = (int) (((double) animal.getXp() / 1000 + 0.3) * (double) animal.getType().price);
        player.setMoney(player.getMoney(gameData) + price, gameData);
        player.getAnimals().remove(animal);
        player.getFarm().removeAnimalFromBuilding(animal);
        GameRepository.saveGame(gameData);
        return new Response(true, "you have sold " + animalName + " for " + price);
    }
}
