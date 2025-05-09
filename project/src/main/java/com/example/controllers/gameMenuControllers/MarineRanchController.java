package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.buildings.Barn;
import com.example.models.buildings.Building;
import com.example.models.buildings.Coop;
import com.example.models.enums.types.AnimalType;
import com.example.models.enums.types.MenuTypes;
import com.example.models.mapModels.Farm;

public class MarineRanchController extends Controller {
    public static Response leaveRanch(Request request) {
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "leaving Marine Ranch");
    }

    public static Response buyAnimal(Request request) {
        String animalTypeName = request.body.get("animal");
        String name = request.body.get("name");
        AnimalType type = AnimalType.Chicken;
        int cost = 800;
        if (animalTypeName.equals("Cow")) {
            cost = 1500;
            type = AnimalType.COW;
        } else if (animalTypeName.equals("Goat")) {
            cost = 4000;
            type = AnimalType.GOAT;
        } else if (animalTypeName.equals("Duck")) {
            cost = 1200;
            type = AnimalType.DUCK;
        } else if (animalTypeName.equals("Sheep")) {
            cost = 8000;
            type = AnimalType.SHEEP;
        } else if (animalTypeName.equals("Rabbit")) {
            cost = 8000;
            type = AnimalType.RABBIT;
        } else if (animalTypeName.equals("Dinosaur")) {
            cost = 14000;
            type = AnimalType.DINOSAUR;
        } else if (animalTypeName.equals("Pig")) {
            cost = 16000;
            type = AnimalType.PIG;
        }
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getFarm();
        Animal animal = new Animal(name, type, cost);
        Player player = game.getCurrentPlayer();
        if (farm.doesAnimalExist(name)) {
            GameRepository.saveGame(game);
            return new Response(false, "Name already exists");
        }
        if (player.getMoney(game) < cost) {
            GameRepository.saveGame(game);
            return new Response(false, "You don't have enough money");
        }
        if (animalTypeName.equals("Cow")) {
            return buyCow(farm, player, cost, animal, game, name);
        } else if (animalTypeName.equals("Goat")) {
            return buyGoat(farm, player, cost, animal, game, name);
        } else if (animalTypeName.equals("Duck")) {
            return buyDuck(farm, player, cost, animal, game, name);
        } else if (animalTypeName.equals("Sheep")) {
            return buySheep(farm, player, cost, animal, game, name);
        } else if (animalTypeName.equals("Rabbit")) {
            return buyRabbit(farm, player, cost, animal, game, name);
        } else if (animalTypeName.equals("Dinosaur")) {
            return buyDinosaur(farm, player, cost, animal, game, name);
        } else if (animalTypeName.equals("Pig")) {
            return buyPig(farm, player, cost, animal, game, name);
        } else if (animalTypeName.equals("Chicken")) {
            return buyChicken(farm, player, cost, animal, game, name);
        }
        GameRepository.saveGame(game);
        return new Response(false, "invalid animal");
    }

    private static Response buyChicken(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Coop) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Coop");
    }

    private static Response buyPig(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Deluxe Barn")) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Barn) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Deluxe Barn");
    }

    private static Response buyDinosaur(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && (((Coop) building).coopType.equals("Big Coop") || ((Coop) building).coopType.equals("Deluxe Coop"))) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Coop) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Big Coop");
    }

    private static Response buyRabbit(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && ((Coop) building).coopType.equals("Deluxe Coop")) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Coop) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Deluxe Coop");
    }

    private static Response buySheep(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Deluxe Barn")) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Barn) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Deluxe Barn");
    }

    private static Response buyDuck(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && (((Coop) building).coopType.equals("Big Coop") || ((Coop) building).coopType.equals("Deluxe Coop"))) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Coop) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Big Coop");
    }

    private static Response buyGoat(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && (((Barn) building).barnType.equals("Big Barn") || ((Barn) building).barnType.equals("Deluxe Barn"))) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Barn) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Big Barn");
    }

    private static Response buyCow(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(game) - cost, game);
                    ((Barn) building).animals.add(animal);
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Barn");
    }
}
