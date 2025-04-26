package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;

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
        PlayerAnimal animalFriendship = player.getAnimalFriendshipByName(petName);
        if (animalFriendship == null) {
            GameRepository.saveGame(game);
            return new Response(false, "Animal not found");
        }
        animalFriendship.hasBeenPetToDay = true;
        GameRepository.saveGame(game);
        return new Response(true, "you have pet " + petName);
    }

    public static Response handleCheatSetFriendship(Request request) {
        String animalName = request.body.get("animalName");
        int amount = Integer.parseInt(request.body.get("amount"));
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        PlayerAnimal friendship = player.getAnimalFriendshipByName(animalName);
        if (friendship == null) {
            GameRepository.saveGame(game);
            return new Response(false, "Animal not found");
        }
        friendship.setXp(amount);
        GameRepository.saveGame(game);
        return new Response(true, "your xp is: " + friendship.getXp());
    }

    public static Response handleAnimals(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        ArrayList<PlayerAnimal> friendship = player.getAnimalFriendship();
        if (friendship.isEmpty()) {
            GameRepository.saveGame(game);
            return new Response(false, "No animals found");
        }
        StringBuilder animals = new StringBuilder();
        for (PlayerAnimal animal : friendship) {
            animals.append("name: ").
                    append(animal.getAnimal().getName()).append("\n").
                    append("friendship level: ").append(animal.getXp()).append("\n").
                    append("hasBeenPetToDay: ").append(animal.hasBeenPetToDay).append("\n").
                    append("hasBeenFedToDay: ").append(animal.hasBeenFed).append("\n").
                    append("xp: ").append(animal.getXp()).append("\n\n");
        }
        GameRepository.saveGame(game);
        return new Response(true, animals.toString());
    }

    public static Response handleShepherd(Request request) {
        return null;
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
