package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.buildings.Barn;
import com.example.models.buildings.Coop;
import com.example.models.buildings.Well;
import com.example.models.enums.types.mapObjectTypes.ArtisanBlockType;
import com.example.models.enums.types.MenuTypes;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.mapModels.Farm;
import com.example.models.mapObjects.ArtisanBlock;
import com.example.models.mapObjects.BuildingBlock;
import com.example.models.mapObjects.EmptyCell;

public class CarpenterShopController extends Controller {
    public static Response leaveCarpenterShop(Request request) {
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "leaving carpenter shop");
    }

    public static Response build(Request request) {
        String buildingName = request.body.get("buildingName");
        int x = Integer.parseInt(request.body.get("x"));
        int y = Integer.parseInt(request.body.get("y"));
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getFarm();
        Backpack backpack = game.getCurrentPlayer().getInventory();
        Slot wood = null;
        Slot stone = null;

        for (Slot slot : backpack.getSlots()) {
            if (slot.getItem().getName().equals(MiscType.WOOD.name))
                wood = slot;
        }
        for (Slot slot : backpack.getSlots()) {
            if (slot.getItem().getName().equals("Stone"))
                stone = slot;
        }
        if (!buildCheck(buildingName, farm, x, y)) {
            GameRepository.saveGame(game);
            return new Response(false, "You cannot build on the selected cell");
        }
        //TODO daily limit
        if (buildingName.equals("Barn")) {
            return buildBarn(wood, stone, game, x, y, farm, buildingName, 350, 150, 7, 4, 6000, backpack);
        } else if (buildingName.equals("Big Barn")) {
            return buildBarn(wood, stone, game, x, y, farm, buildingName, 450, 200, 7, 4, 12000, backpack);
        } else if (buildingName.equals("Deluxe Barn")) {
            return buildBarn(wood, stone, game, x, y, farm, buildingName, 550, 300, 7, 4, 25000, backpack);
        } else if (buildingName.equals("Coop")) {
            return buildCoop(wood, stone, game, x, y, farm, buildingName, 300, 100, 6, 3, 4000, backpack);
        } else if (buildingName.equals("Big Coop")) {
            return buildCoop(wood, stone, game, x, y, farm, buildingName, 400, 150, 6, 3, 10000, backpack);
        } else if (buildingName.equals("Deluxe Coop")) {
            return buildCoop(wood, stone, game, x, y, farm, buildingName, 500, 200, 6, 3, 20000, backpack);
        } else if (buildingName.equals("Well")) {
            return buildWell(wood, stone, game, x, y, farm, buildingName, 75, 75, 3, 3, 1000, backpack);
        }
        return buildShipping(wood, stone, game, x, y, farm, buildingName, 150, 150, 1, 1, 250, backpack);


    }

    private static Response buildBarn(Slot wood, Slot stone, Game game, int x, int y, Farm farm, String buildingName, int woodPrice, int stonePrice, int width, int height, int goldPrice, Backpack backpack) {
        if (wood != null && stone != null) {
            if (wood.getCount() >= woodPrice && stone.getCount() >= stonePrice && game.getCurrentPlayer().getMoney() >= goldPrice) {
                wood.setCount(wood.getCount() - woodPrice);
                stone.setCount(stone.getCount() - stonePrice);
                if (wood.getCount() == 0) {
                    backpack.removeSlot(wood);
                }
                if (stone.getCount() == 0) {
                    backpack.removeSlot(stone);
                }
                game.getCurrentPlayer().setMoney(game.getCurrentPlayer().getMoney() - goldPrice);
                Barn barn = new Barn();
                barn.barnType = buildingName;
                for (int i = x; i < x + width; i++) {
                    for (int j = y; j < y + height; j++) {
                        farm.findCellByCoordinate(i, j).setObjectOnCell(new BuildingBlock(true, buildingName));
                        barn.buildingCells.add(farm.findCellByCoordinate(i, j));
                    }
                }
                farm.getBuildings().add(barn);
                GameRepository.saveGame(game);
                return new Response(true, "You built a " + buildingName);
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "You don't have enough money.");
    }

    private static Response buildShipping(Slot wood, Slot stone, Game game, int x, int y, Farm farm, String buildingName, int woodPrice, int stonePrice, int width, int height, int goldPrice, Backpack backpack) {
        if (wood != null && stone != null) {
            if (wood.getCount() >= woodPrice && stone.getCount() >= stonePrice && game.getCurrentPlayer().getMoney() >= goldPrice) {
                wood.setCount(wood.getCount() - woodPrice);
                stone.setCount(stone.getCount() - stonePrice);
                if (wood.getCount() == 0) {
                    backpack.removeSlot(wood);
                }
                if (stone.getCount() == 0) {
                    backpack.removeSlot(stone);
                }
                game.getCurrentPlayer().setMoney(game.getCurrentPlayer().getMoney() - goldPrice);
                farm.findCellByCoordinate(x, y).setObjectOnCell(new ArtisanBlock(ArtisanBlockType.SHIPPING_BIN));
                GameRepository.saveGame(game);
                return new Response(true, "You built a " + buildingName);
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "You don't have enough money.");
    }

    private static Response buildWell(Slot wood, Slot stone, Game game, int x, int y, Farm farm, String buildingName, int woodPrice, int stonePrice, int width, int height, int goldPrice, Backpack backpack) {
        if (wood != null && stone != null) {
            if (wood.getCount() >= woodPrice && stone.getCount() >= stonePrice && game.getCurrentPlayer().getMoney() >= goldPrice) {
                wood.setCount(wood.getCount() - woodPrice);
                stone.setCount(stone.getCount() - stonePrice);
                if (wood.getCount() == 0) {
                    backpack.removeSlot(wood);
                }
                if (stone.getCount() == 0) {
                    backpack.removeSlot(stone);
                }
                game.getCurrentPlayer().setMoney(game.getCurrentPlayer().getMoney() - goldPrice);
                Well well = new Well();
                well.wellType = buildingName;
                for (int i = x; i < x + width; i++) {
                    for (int j = y; j < y + height; j++) {
                        farm.findCellByCoordinate(i, j).setObjectOnCell(new BuildingBlock(true, buildingName));
                        well.buildingCells.add(farm.findCellByCoordinate(i, j));
                    }
                }
                farm.getBuildings().add(well);
                GameRepository.saveGame(game);
                return new Response(true, "You built a " + buildingName);
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "You don't have enough money.");
    }

    private static Response buildCoop(Slot wood, Slot stone, Game game, int x, int y, Farm farm, String buildingName, int woodPrice, int stonePrice, int width, int height, int goldPrice, Backpack backpack) {
        if (wood != null && stone != null) {
            if (wood.getCount() >= woodPrice && stone.getCount() >= stonePrice && game.getCurrentPlayer().getMoney() >= goldPrice) {
                wood.setCount(wood.getCount() - woodPrice);
                stone.setCount(stone.getCount() - stonePrice);
                if (wood.getCount() == 0) {
                    backpack.removeSlot(wood);
                }
                if (stone.getCount() == 0) {
                    backpack.removeSlot(stone);
                }
                game.getCurrentPlayer().setMoney(game.getCurrentPlayer().getMoney() - goldPrice);
                Coop coop = new Coop();
                coop.coopType = buildingName;
                for (int i = x; i < x + width; i++) {
                    for (int j = y; j < y + height; j++) {
                        farm.findCellByCoordinate(i, j).setObjectOnCell(new BuildingBlock(true, buildingName));
                        coop.buildingCells.add(farm.findCellByCoordinate(i, j));
                    }
                }
                farm.getBuildings().add(coop);
                GameRepository.saveGame(game);
                return new Response(true, "You built a " + buildingName);
            }
        }
        GameRepository.saveGame(game);
        return new Response(false, "You don't have enough money.");
    }

    private static boolean buildCheck(String buildingName, Farm farm, int x, int y) {
        int width = 1;
        int height = 1;
        if (buildingName.equals("Barn") || buildingName.equals("Big Barn") || buildingName.equals("Deluxe Barn")) {
            width = 7;
            height = 4;
        } else if (buildingName.equals("Coop") || buildingName.equals("Big Coop") || buildingName.equals("Deluxe Coop")) {
            width = 6;
            height = 3;
        } else if (buildingName.equals("Well")) {
            width = 3;
            height = 3;
        }
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                if (farm.findCellByCoordinate(i, j) == null || !(farm.findCellByCoordinate(i, j).getObjectOnCell() instanceof EmptyCell)) {
                    return false;
                }
            }
        }
        return true;
    }
}
