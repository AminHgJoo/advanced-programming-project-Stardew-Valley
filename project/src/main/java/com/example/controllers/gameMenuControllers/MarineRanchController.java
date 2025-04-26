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
import org.jetbrains.annotations.NotNull;

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
        if (player.getMoney() < cost) {
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

    private static @NotNull Response buyChicken(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && ((Coop) building).coopType.equals("Coop")) {
                if (((Coop) building).animals.size() < AnimalType.Chicken.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Coop");
    }

    private static @NotNull Response buyPig(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Deluxe Barn")) {
                if (((Barn) building).animals.size() < AnimalType.PIG.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Deluxe Barn");
    }

    private static @NotNull Response buyDinosaur(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && ((Coop) building).coopType.equals("Big Coop")) {
                if (((Coop) building).animals.size() < AnimalType.DINOSAUR.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Big Coop");
    }

    private static @NotNull Response buyRabbit(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && ((Coop) building).coopType.equals("Deluxe Coop")) {
                if (((Coop) building).animals.size() < AnimalType.RABBIT.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Deluxe Coop");
    }

    private static @NotNull Response buySheep(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Deluxe Barn")) {
                if (((Barn) building).animals.size() < AnimalType.SHEEP.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Deluxe Barn");
    }

    private static @NotNull Response buyDuck(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && ((Coop) building).coopType.equals("Big Coop")) {
                if (((Coop) building).animals.size() < AnimalType.DUCK.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Big Coop");
    }

    private static @NotNull Response buyGoat(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Big Barn")) {
                if (((Barn) building).animals.size() < AnimalType.GOAT.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Big Barn");
    }

    private static @NotNull Response buyCow(Farm farm, Player player, int cost, Animal animal, Game game, String name) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Barn")) {
                if (((Barn) building).animals.size() < AnimalType.COW.capacity) {
                    player.setMoney(player.getMoney() - cost);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(new PlayerAnimal(animal, 0));
                    GameRepository.saveGame(game);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "you need to build another Barn");
    }
}
