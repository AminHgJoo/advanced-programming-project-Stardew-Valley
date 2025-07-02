package com.server.controllers.gameMenuControllers;

import com.common.models.*;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.buildings.Barn;
import com.common.models.buildings.Building;
import com.common.models.buildings.Coop;
import com.common.models.enums.types.AnimalType;
import com.common.models.enums.types.MenuTypes;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.AnimalBlock;
import com.server.repositories.GameRepository;
import com.server.controllers.Controller;

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
        GameData gameData = user.getCurrentGame();
        Farm farm = gameData.getCurrentPlayer().getFarm();
        Animal animal = new Animal(name, type, cost);
        Player player = gameData.getCurrentPlayer();
        if (farm.doesAnimalExist(name)) {
            GameRepository.saveGame(gameData);
            return new Response(false, "Name already exists");
        }
        if (player.getMoney(gameData) < cost) {
            GameRepository.saveGame(gameData);
            return new Response(false, "You don't have enough money");
        }
        Store store = gameData.getMap().getVillage().getStore("Marnie's Ranch");
        StoreProduct storeProduct = store.getProduct(animalTypeName);
        if (storeProduct.getAvailableCount() <= 0) {
            GameRepository.saveGame(gameData);
            return new Response(false, "daily limit reached");
        }
        if (animalTypeName.equals("Cow")) {
            return buyCow(farm, player, cost, animal, gameData, name, storeProduct);
        } else if (animalTypeName.equals("Goat")) {
            return buyGoat(farm, player, cost, animal, gameData, name, storeProduct);
        } else if (animalTypeName.equals("Duck")) {
            return buyDuck(farm, player, cost, animal, gameData, name, storeProduct);
        } else if (animalTypeName.equals("Sheep")) {
            return buySheep(farm, player, cost, animal, gameData, name, storeProduct);
        } else if (animalTypeName.equals("Rabbit")) {
            return buyRabbit(farm, player, cost, animal, gameData, name, storeProduct);
        } else if (animalTypeName.equals("Dinosaur")) {
            return buyDinosaur(farm, player, cost, animal, gameData, name, storeProduct);
        } else if (animalTypeName.equals("Pig")) {
            return buyPig(farm, player, cost, animal, gameData, name, storeProduct);
        } else if (animalTypeName.equals("Chicken")) {
            return buyChicken(farm, player, cost, animal, gameData, name, storeProduct);
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "invalid animal");
    }

    private static Response buyChicken(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Coop");
    }

    private static void addAnimalToBuilding(Animal animal, Building building) {
        for (Cell cell : building.buildingCells) {
            if (!(cell.getObjectOnCell() instanceof AnimalBlock)) {
                cell.setObjectOnCell(new AnimalBlock(animal));
                break;
            }
        }
    }

    private static Response buyPig(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Deluxe Barn")) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Deluxe Barn");
    }

    private static Response buyDinosaur(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && (((Coop) building).coopType.equals("Big Coop") || ((Coop) building).coopType.equals("Deluxe Coop"))) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Big Coop");
    }

    private static Response buyRabbit(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && ((Coop) building).coopType.equals("Deluxe Coop")) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Deluxe Coop");
    }

    private static Response buySheep(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && ((Barn) building).barnType.equals("Deluxe Barn")) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Deluxe Barn");
    }

    private static Response buyDuck(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Coop && (((Coop) building).coopType.equals("Big Coop") || ((Coop) building).coopType.equals("Deluxe Coop"))) {
                if (((Coop) building).animals.size() < ((Coop) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Coop) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Big Coop");
    }

    private static Response buyGoat(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn && (((Barn) building).barnType.equals("Big Barn") || ((Barn) building).barnType.equals("Deluxe Barn"))) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Big Barn");
    }

    private static Response buyCow(Farm farm, Player player, int cost, Animal animal, GameData gameData, String name, StoreProduct storeProduct) {
        for (Building building : farm.getBuildings()) {
            if (building instanceof Barn) {
                if (((Barn) building).animals.size() < ((Barn) building).capacity) {
                    player.setMoney(player.getMoney(gameData) - cost, gameData);
                    ((Barn) building).animals.add(animal);
                    player.getAnimals().add(animal);
                    addAnimalToBuilding(animal, building);
                    storeProduct.setAvailableCount(storeProduct.getAvailableCount() - 1);
                    GameRepository.saveGame(gameData);
                    return new Response(true, "you have bought " + name);
                }
            }
        }
        GameRepository.saveGame(gameData);
        return new Response(false, "you need to build another Barn");
    }
}
