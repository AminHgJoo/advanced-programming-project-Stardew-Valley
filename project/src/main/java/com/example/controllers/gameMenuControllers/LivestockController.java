package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.mapModels.Farm;

public class LivestockController extends Controller {
    public static Response handleBuyAnimal(Request request) {
        return null;
    }

    public static Response handlePet(Request request) {
        String petName = request.body.get("name");
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = game.getCurrentPlayer().getFarm();
        Animal animal = farm.findAnimal(petName);
        if (animal == null) {
            GameRepository.saveGame(game);
            return new Response(false, "Animal not found");
        }
        PlayerAnimal playerAnimal = new PlayerAnimal(animal, 0);
        player.getAnimals().add(playerAnimal);
        GameRepository.saveGame(game);
        return new Response(true, "you are now friend with " + petName);
    }

    public static Response handleCheatSetFriendship(Request request) {
        String animalName = request.body.get("animalName");
        int amount = Integer.parseInt(request.body.get("amount"));

        return null;
    }

    public static Response handleAnimals(Request request) {
        return null;
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
