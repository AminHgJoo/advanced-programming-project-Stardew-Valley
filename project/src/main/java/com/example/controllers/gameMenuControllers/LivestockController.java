package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Farm;
import com.example.models.mapObjects.AnimalBlock;
import com.example.models.mapObjects.EmptyCell;

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
                    append("hasBeenFedToDay: ").append(animal.hasBeenFed).append("\n").
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
        if(cell == null|| !(cell.getObjectOnCell() instanceof EmptyCell)) {
            GameRepository.saveGame(game);
            return new Response(false, "cell not found or not empty");
        }
        cell.setObjectOnCell(new AnimalBlock(animal));
        animal.hasBeenFed = true;
        GameRepository.saveGame(game);
        return new Response(true, "you have shepherd");
    }

    public static Response handleFeedHay(Request request) {
        return null;
    }

    public static Response handleProduces(Request request) {
        return null;
    }

    public static Response handleCollectProduce(Request request) {
        return null;
    }

    public static Response handleSellAnimal(Request request) {
        return null;
    }

    public static Response handleFishing(Request request) {
        return null;
    }
}
