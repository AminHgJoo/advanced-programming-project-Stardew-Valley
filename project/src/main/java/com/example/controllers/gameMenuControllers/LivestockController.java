package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.types.AnimalType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.types.itemTypes.ToolTypes;
import com.example.models.enums.worldEnums.Weather;
import com.example.models.items.Item;
import com.example.models.items.Misc;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Farm;
import com.example.models.mapObjects.AnimalBlock;
import com.example.models.mapObjects.EmptyCell;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LivestockController extends Controller {
    public static Response handleBuyAnimal(Request request) {
        return null;
    }

    public static Response handlePet(Request request) {
        String petName = request.body.get("name");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Animal animal = player.getAnimalByName(petName);
        if (animal == null) {
            GameRepository.saveGame(game);
            return new Response(false, "Animal not found");
        }
        animal.hasBeenPetToDay = true;
        GameRepository.saveGame(game);
        return new Response(true, "you have pet " + petName);
    }

    public static Response handleCheatSetFriendship(Request request) {
        String animalName = request.body.get("animalName");
        int amount = Integer.parseInt(request.body.get("amount"));
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Animal animal = player.getAnimalByName(animalName);
        if (animal == null) {
            GameRepository.saveGame(game);
            return new Response(false, "Animal not found");
        }
        animal.setXp(amount);
        GameRepository.saveGame(game);
        return new Response(true, "your xp is: " + animal.getXp());
    }

    public static Response handleAnimals(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        ArrayList<Animal> animals = player.getAnimals();
        if (animals.isEmpty()) {
            GameRepository.saveGame(game);
            return new Response(false, "No animals found");
        }
        StringBuilder animalString = new StringBuilder();
        for (Animal animal : animals) {
            animalString.append("name: ").
                    append(animal.getName()).append("\n").
                    append("friendship level: ").append(animal.getXp()).append("\n").
                    append("hasBeenPetToDay: ").append(animal.hasBeenPetToDay).append("\n").
                    append("hasBeenFedToDay: ").append(animal.hasBeenFedByHay || animal.hasBeenFedByGrass).append("\n").
                    append("xp: ").append(animal.getXp()).append("\n\n");
        }
        GameRepository.saveGame(game);
        return new Response(true, animals.toString());
    }

    public static Response handleShepherd(Request request) {
        String animalName = request.body.get("animalName");
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = game.getCurrentPlayer().getFarm();
        Animal animal = player.getAnimalByName(animalName);
        Cell cell = farm.findCellByCoordinate(x, y);
        if (cell == null || !(cell.getObjectOnCell() instanceof EmptyCell) || animal == null) {
            GameRepository.saveGame(game);
            return new Response(false, "cell not found or not empty or no animal found");
        }
        if (game.getWeatherToday() == Weather.SNOW || game.getWeatherToday() == Weather.STORM || game.getWeatherToday() == Weather.RAIN) {
            GameRepository.saveGame(game);
            return new Response(true, "bad weather quality");
        }
        cell.setObjectOnCell(new AnimalBlock(animal));
        animal.hasBeenFedByGrass = true;
        GameRepository.saveGame(game);
        return new Response(true, "you have shepherd");
    }

    public static Response handleFeedHay(Request request) {
        String animalName = request.body.get("animalName");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = game.getCurrentPlayer().getFarm();
        Animal animal = player.getAnimalByName(animalName);
        if (animal == null) {
            GameRepository.saveGame(game);
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
        GameRepository.saveGame(game);
        return new Response(true, "you have shepherd");
    }

    public static Response handleProduces(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        ArrayList<Animal> allAnimals = game.getCurrentPlayer().getAnimals();
        ArrayList<Animal> desiredAnimals = new ArrayList<>();
        for (Animal animal : allAnimals) {
            if (animal.product != null) {
                desiredAnimals.add(animal);
            }
        }
        if (desiredAnimals.isEmpty()) {
            GameRepository.saveGame(game);
            return new Response(false, "No products found");
        }
        StringBuilder animalString = new StringBuilder();
        for (Animal animal : desiredAnimals) {
            animalString.append("name: ").append(animal.getName())
                    .append(" product: ").append(animal.product.getName()).append(" ").
                    append(animal.product.getQuality()).append("\n");
        }
        GameRepository.saveGame(game);
        return new Response(true, animalString.toString());
    }

    public static Response handleCollectProduce(Request request) {
        String animalName = request.body.get("name");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Animal animal = player.getAnimalByName(animalName);
        Item equippedItem = player.getEquippedItem();
        Item product = animal.product;
        Backpack backpack = player.getInventory();
        Slot productSlot = null;

        if (animal == null) {
            GameRepository.saveGame(game);
            return new Response(false, "no animal found");
        }

        if (product == null) {
            return noProductFoundHandle(animal, equippedItem, player, game);
        }
        if (animal.getType().equals(AnimalType.COW) || animal.getType().equals(AnimalType.GOAT)) {
            if (equippedItem == null || !equippedItem.getName().equals(ToolTypes.MILK_PAIL.name)) {
                GameRepository.saveGame(game);
                return new Response(false, "you have to equip milk pail first");
            }
            }
        Item item = new Misc(((Misc) product).getMiscType(), ((Misc) product).getQuality());
        for(Slot slot : backpack.getSlots()) {
            if (slot.getItem().getName().equals(product.getName())) {
                productSlot = slot;
            }
        }
        if(productSlot == null) {
            return addNewSlotForProductHandle(backpack, animal, player, game, item, product);
        }
        return addToExistingSlotHandle(productSlot, animal, player, game, product);
    }

    private static @NotNull Response addToExistingSlotHandle(Slot productSlot, Animal animal, Player player, Game game, Item product) {
        productSlot.setCount(productSlot.getCount() + animal.getType().productPerDay);
        if(animal.getType().equals(AnimalType.COW) || animal.getType().equals(AnimalType.GOAT) || animal.getType().equals(AnimalType.SHEEP)) {
            player.setEnergy(player.getEnergy() - 4);
            animal.hasBeenHarvested = true;
        }
        animal.product = null;
        GameRepository.saveGame(game);
        return new Response(true, "you have collected " + animal.getType().productPerDay + " of " + product.getName());
    }

    private static @NotNull Response addNewSlotForProductHandle(Backpack backpack, Animal animal, Player player, Game game, Item item, Item product) {
        if(backpack.getSlots().size() == backpack.getType().getMaxCapacity()){
            if(animal.getType().equals(AnimalType.COW) || animal.getType().equals(AnimalType.GOAT) || animal.getType().equals(AnimalType.SHEEP)) {
                player.setEnergy(player.getEnergy() - 4);
            }
            GameRepository.saveGame(game);
            return new Response(false, "your backpack is full");
        }
        Slot newSlot = new Slot(item, animal.getType().productPerDay);
        backpack.addSlot(newSlot);
        player.setEnergy(player.getEnergy() - 4);
        animal.product = null;
        if(animal.getType().equals(AnimalType.COW) || animal.getType().equals(AnimalType.GOAT) || animal.getType().equals(AnimalType.SHEEP))
            animal.hasBeenHarvested = true;
        GameRepository.saveGame(game);
        return new Response(true, "you have collected " + animal.getType().productPerDay + " of " + product.getName());
    }

    private static @NotNull Response noProductFoundHandle(Animal animal, Item equippedItem, Player player, Game game) {
        if (animal.getType().equals(AnimalType.COW) || animal.getType().equals(AnimalType.GOAT)) {
            if (equippedItem != null && equippedItem.getName().equals(ToolTypes.MILK_PAIL.name)) {
                player.setEnergy(player.getEnergy() - 4);
            }
        }
        if (animal.getType().equals(AnimalType.SHEEP)) {
            if (equippedItem != null && equippedItem.getName().equals(ToolTypes.SHEAR.name)) {
                player.setEnergy(player.getEnergy() - 4);
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "no product found");
    }

    public static Response handleSellAnimal(Request request) {
        return null;
    }

    public static Response handleFishing(Request request) {
        return null;
    }
}
