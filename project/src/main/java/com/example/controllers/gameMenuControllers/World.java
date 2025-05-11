package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.Quality;
import com.example.models.enums.SkillLevel;
import com.example.models.enums.types.AnimalType;
import com.example.models.enums.types.itemTypes.*;
import com.example.models.enums.types.mapObjectTypes.TreeType;
import com.example.models.enums.worldEnums.Season;
import com.example.models.enums.worldEnums.Weather;
import com.example.models.items.Fish;
import com.example.models.items.Item;
import com.example.models.items.Misc;
import com.example.models.items.Tool;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Farm;
import com.example.models.mapObjects.*;
import com.example.utilities.DateUtility;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.example.controllers.gameMenuControllers.LivestockController.handleCollectProducts;
import static com.example.controllers.gameMenuControllers.LivestockController.noProductFoundHandle;

public class World extends Controller {
    public static Response showMoney(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        String money = String.valueOf(player.getMoney(game));
        GameRepository.saveGame(game);
        return new Response(true, money);
    }

    public static Response handleTimeQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(App.getLoggedInUser().getCurrentGame().getDate().toLocalTime().toString());
        return response;
    }

    public static Response handleDateQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(App.getLoggedInUser().getCurrentGame().getDate().toLocalDate().toString());
        return response;
    }

    public static Response handleDatetimeQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(App.getLoggedInUser().getCurrentGame()
                .getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")).toString());
        return response;
    }

    public static Response handleDayOfWeekQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        LocalDateTime currentDateTime = App.getLoggedInUser().getCurrentGame().getDate();
        int currentDay = currentDateTime.getDayOfMonth();
        int dayOfWeek = (currentDay - 1) % 7;
        response.setMessage(DayOfWeek.values()[dayOfWeek].toString().toLowerCase());
        return response;
    }

    public static Response handleCheatAdvanceTime(Request request) {
        int amountOfHours = Integer.parseInt(request.body.get("X"));
        LocalDateTime currentDateTime = App.getLoggedInUser().getCurrentGame().getDate();
        LocalDateTime nextDateTime;
        Game currentGame = App.getLoggedInUser().getCurrentGame();
        int howManyDays = amountOfHours / 24;
        int howManyHours = amountOfHours % 24;
        int howManyMonths = howManyDays / 28;
        howManyDays %= 28;
        int currentHour = currentDateTime.getHour();
        int currentDay = currentDateTime.getDayOfMonth();
        if (howManyHours + currentHour > 22) {
            howManyHours = 22 - currentHour;
        }
        if (howManyDays + currentDay > 28) {
            howManyMonths++;
            howManyDays -= 28;
        }
        nextDateTime = currentDateTime.plusDays(howManyDays);
        nextDateTime = nextDateTime.plusHours(howManyHours);
        nextDateTime = nextDateTime.plusMonths(howManyMonths);
        boolean check = nextDateTime.getMonthValue() - currentDateTime.getMonthValue() > 0
                || nextDateTime.getDayOfMonth() - currentDateTime.getDayOfMonth() > 0;
        currentGame.setDate(nextDateTime);
        currentGame.checkSeasonChange();
        if (check) {
            currentGame.newDayBackgroundChecks();
        }
        GameRepository.saveGame(currentGame);
        return new Response(true, "Date and time set successfully.");
    }

    public static Response handleCheatAdvanceDate(Request request) {
        int amountOfDays = Integer.parseInt(request.body.get("X"));
        LocalDateTime currentDateTime = App.getLoggedInUser().getCurrentGame().getDate();
        LocalDateTime nextDateTime;
        Game currentGame = App.getLoggedInUser().getCurrentGame();
        int howManyDays = amountOfDays % 28;
        int howManyMonths = amountOfDays / 28;
        int currentDay = currentDateTime.getDayOfMonth();
        if (howManyDays + currentDay > 28) {
            howManyMonths++;
            howManyDays -= 28;
        }
        nextDateTime = currentDateTime.plusDays(howManyDays);
        nextDateTime = nextDateTime.plusMonths(howManyMonths);
        boolean check = (nextDateTime.getMonthValue() - currentDateTime.getMonthValue() > 0)
                || (nextDateTime.getDayOfMonth() - currentDateTime.getDayOfMonth() > 0);
        currentGame.setDate(nextDateTime);
        if (check) {
            currentGame.newDayBackgroundChecks();
        }
        currentGame.checkSeasonChange();
        GameRepository.saveGame(currentGame);
        return new Response(true, "Date set successfully.");
    }


    public static Response handleSeasonQuery(Request request) {
        return new Response(true, App.getLoggedInUser().getCurrentGame().getSeason().toString());
    }

    public static Response handleCheatThor(Request request) {
        int targetX = Integer.parseInt(request.body.get("x"));
        int targetY = Integer.parseInt(request.body.get("y"));

        if (targetX >= 75 || targetY >= 50 || targetX < 0 || targetY < 0) {
            return new Response(false, "Coordinates out of bounds.");
        }

        Game currentGame = App.getLoggedInUser().getCurrentGame();
        currentGame.getCurrentPlayer().getCurrentFarm(currentGame).strikeLightning(targetX, targetY, currentGame.getDate());
        GameRepository.saveGame(currentGame);
        return new Response(true, "Lightning summoned at target coordinates.");
    }

    public static Response handleWeatherQuery(Request request) {
        return new Response(true, App.getLoggedInUser().getCurrentGame().getWeatherToday().toString());
    }

    public static Response handleWeatherForecastQuery(Request request) {
        return new Response(true, "Tomorrow's weather forecast is: "
                + App.getLoggedInUser().getCurrentGame().getWeatherTomorrow().toString());
    }

    public static Response handleSetWeatherCheat(Request request) {
        String type = request.body.get("Type");
        Weather weather = Weather.getWeatherByName(type);
        Game game = App.getLoggedInUser().getCurrentGame();
        if (weather == null) {
            return new Response(false, "Weather type is invalid.");
        } else {
            game.setWeatherTomorrow(weather);
            GameRepository.saveGame(game);
        }
        return new Response(true, "Tomorrow's weather set successfully.");
    }

    public static Response handleGreenhouseBuilding(Request request) {
        Game game = App.getLoggedInUser().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getFarm();
        Backpack backpack = player.getInventory();

        Cell testCell = Farm.getCellByCoordinate(25, 4, farm.getCells());

        if (testCell.getObjectOnCell() instanceof Water) {
            return new Response(false, "Greenhouse already built.");
        }

        Slot slot = backpack.getSlotByItemName(MiscType.WOOD.name);

        if (slot == null) {
            return new Response(false, "You don't have any wood.");
        }

        if (slot.getCount() < 500) {
            return new Response(false, "You don't have enough wood.");
        }

        if (player.getMoney(game) < 1000) {
            return new Response(false, "You don't have enough money.");
        }

        slot.setCount(slot.getCount() - 500);

        if (slot.getCount() == 0) {
            backpack.getSlots().remove(slot);
        }

        player.setMoney(player.getMoney(game) - 1000, game);


        //Greenhouse runs from x : [22, 28] & y : [3, 10]
        for (int i = 23; i < 28; i++) {
            for (int j = 4; j < 10; j++) {
                Cell cell = Farm.getCellByCoordinate(i, j, farm.getCells());
                cell.setObjectOnCell(new EmptyCell());
            }
        }

        Cell cell = Farm.getCellByCoordinate(25, 10, farm.getCells());
        cell.setObjectOnCell(new EmptyCell());

        Cell cell1 = Farm.getCellByCoordinate(25, 4, farm.getCells());
        cell1.setObjectOnCell(new Water());

        GameRepository.saveGame(game);

        return new Response(true, "Greenhouse built successfully.");
    }

    public static Response handleToolUse(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        if (player.getEquippedItem() == null) {
            return new Response(false, "You have no equipped item.");
        }
        if (!(player.getEquippedItem() instanceof Tool)) {
            return new Response(false, "You have no equipped tools.");
        }
        if (getXAndYIncrement(request.body.get("direction")) == null) {
            return new Response(false, "Invalid direction.");
        }

        Tool equippedTool = (Tool) player.getEquippedItem();
        ToolTypes toolType = equippedTool.getType();

        if (toolType == ToolTypes.HOE) {
            return handleHoeUse(request, equippedTool.getQuality()
                    , player.getFarmingSkill().getLevel().energyCostDiscount);
        } else if (toolType == ToolTypes.PICKAXE) {
            return handlePickaxeUse(request
                    , player.getMiningSkill().getLevel()
                    , equippedTool.getQuality()
                    , player.getMiningSkill().getLevel().energyCostDiscount);
        } else if (toolType == ToolTypes.AXE) {
            return handleAxeUse(request, equippedTool.getQuality()
                    , player.getForagingSkill().getLevel().energyCostDiscount);
        } else if (toolType == ToolTypes.WATERING_CAN_DEFAULT) {
            return handleWateringCanUse(request, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
        } else if (toolType == ToolTypes.WATERING_CAN_COPPER) {
            return handleWateringCanUse(request, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
        } else if (toolType == ToolTypes.WATERING_CAN_IRON) {
            return handleWateringCanUse(request, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
        } else if (toolType == ToolTypes.WATERING_CAN_GOLD) {
            return handleWateringCanUse(request, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
        } else if (toolType == ToolTypes.WATERING_CAN_IRIDIUM) {
            return handleWateringCanUse(request, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
        } else if (toolType == ToolTypes.FISHING_ROD) {
            return handleFishingRodUse(request, equippedTool.getQuality()
                    , player.getFishingSkill().getLevel().energyCostDiscount);
        } else if (toolType == ToolTypes.SCYTHE) {
            return handleScytheUse(request);
        } else if (toolType == ToolTypes.MILK_PAIL) {
            return handleMilkPailUse(request);
        } else if (toolType == ToolTypes.SHEAR) {
            return handleShearUse(request);
        } else {
            return new Response(false, "Tool type is invalid.");
        }
    }

    private static int[] getXAndYIncrement(String direction) {
        if (direction.compareToIgnoreCase("up") == 0) {
            return new int[]{0, -1};
        } else if (direction.compareToIgnoreCase("down") == 0) {
            return new int[]{0, 1};
        } else if (direction.compareToIgnoreCase("right") == 0) {
            return new int[]{1, 0};
        } else if (direction.compareToIgnoreCase("left") == 0) {
            return new int[]{-1, 0};
        } else if (direction.compareToIgnoreCase("up_right") == 0) {
            return new int[]{1, -1};
        } else if (direction.compareToIgnoreCase("up_left") == 0) {
            return new int[]{-1, -1};
        } else if (direction.compareToIgnoreCase("down_right") == 0) {
            return new int[]{1, 1};
        } else if (direction.compareToIgnoreCase("down_left") == 0) {
            return new int[]{-1, 1};
        } else {
            return new int[]{10000, 10000};
        }
    }

    private static double calculateEnergyCostForHoeAxePickaxeWaterCan(int discount, Quality quality) {
        double answer = 5 - quality.getQualityLevel() - discount;
        if (answer < 0) {
            answer = 0;
        }

        Game game = App.getLoggedInUser().getCurrentGame();
        if (game.getWeatherToday() == Weather.SNOW) {
            answer *= 2;
        }
        if (game.getWeatherToday() == Weather.RAIN) {
            answer *= 1.5;
        }

        return answer;
    }

    private static Response handleHoeUse(Request request, Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null) {
            return new Response(false, "Target cell not found.");
        }

        player.setEnergy(player.getEnergy() - energyCost);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energyCost);

        if (!(targetCell.getObjectOnCell() instanceof EmptyCell)) {
            GameRepository.saveGame(game);
            return new Response(false, "Target cell is invalid.");
        }

        targetCell.setTilled(true);
        GameRepository.saveGame(game);
        return new Response(true, "Target cell has been tilled.");
    }

    private static boolean canPickaxeMineThis(ForagingMineralsType target, Quality pickaxeQuality) {
        if (pickaxeQuality == Quality.DEFAULT) {
            if (target == ForagingMineralsType.STONE) {
                return true;
            } else if (target == ForagingMineralsType.COPPER_ORE) {
                return true;
            } else if (target == ForagingMineralsType.COAL) {
                return true;
            }
            return false;
        }
        if (pickaxeQuality == Quality.COPPER) {
            if (target == ForagingMineralsType.STONE) {
                return true;
            } else if (target == ForagingMineralsType.COPPER_ORE) {
                return true;
            } else if (target == ForagingMineralsType.COAL) {
                return true;
            } else if (target == ForagingMineralsType.IRON_ORE) {
                return true;
            }
            return false;
        }
        if (pickaxeQuality == Quality.SILVER) {
            if (target == ForagingMineralsType.GOLD_ORE) {
                return false;
            } else if (target == ForagingMineralsType.IRIDIUM_ORE) {
                return false;
            }
            return true;
        }
        return true;
    }

    private static Response handlePickaxeUse(Request request, SkillLevel skillLevel
            , Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. " +
                    "You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null) {
            return new Response(false, "Target cell not found.");
        }

        player.setEnergy(player.getEnergy() - energyCost);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energyCost);

        //Mine Cells : x : [0, 9] y : [0, 11]
        if (targetCell.getObjectOnCell() instanceof ForagingMineral) {

            ForagingMineralsType type = ((ForagingMineral) targetCell.getObjectOnCell()).getFMType();

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(type.name);

            if (!canPickaxeMineThis(type, quality)) {
                GameRepository.saveGame(game);
                return new Response(false, "Pickaxe is too weak to mine this block.");
            }

            player.getMiningSkill().setXp(player.getMiningSkill().getXp() + 10);

            int cellX = targetCell.getCoordinate().getX();
            int cellY = targetCell.getCoordinate().getY();

            boolean check = cellX <= 9 && cellY <= 11;

            targetCell.setObjectOnCell(check ? new BuildingBlock(true, "Mine") : new EmptyCell());

            int count = 1;
            if (skillLevel.ordinal() <= 2) {
                count++;
            }

            if (backpack.getSlots().size() == backpack.getType().getMaxCapacity()) {
                if (slot == null) {
                    System.out.println("No space in backpack.");
                } else {
                    slot.setCount(Math.min(slot.getCount() + count, slot.getItem().getMaxStackSize()));
                    System.out.println("Added " + count + " to backpack.");
                }
            } else {
                if (slot == null) {
                    backpack.getSlots().add(new
                            Slot(new com.example.models.items.ForagingMineral(Quality.DEFAULT, type), count));
                } else {
                    slot.setCount(Math.min(slot.getItem().getMaxStackSize(), slot.getCount() + count));
                }
                System.out.println("Added " + count + " to backpack.");
            }

            GameRepository.saveGame(game);
            return new Response(true, "Mined stone/mineral at target cell.");
        }

        if (targetCell.getObjectOnCell() instanceof DroppedItem) {

            DroppedItem droppedItem = (DroppedItem) targetCell.getObjectOnCell();
            Slot droppedSlot = new Slot(droppedItem.getItem(), droppedItem.getQuantity());
            Backpack backpack = player.getInventory();
            Slot backpackSlot = backpack.getSlotByItemName(droppedSlot.getItem().getName());

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (backpackSlot == null) {
                    GameRepository.saveGame(game);
                    return new Response(false, "Backpack was full! Couldn't retrieve item from the ground.");
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                    GameRepository.saveGame(game);
                    return new Response(true, "Item has been added to the backpack: " + droppedSlot.getItem().getName() + " x(" + droppedItem.getQuantity() + ").");
                }
            } else {
                if (backpackSlot == null) {
                    backpack.getSlots().add(droppedSlot);
                    targetCell.setObjectOnCell(new EmptyCell());
                    GameRepository.saveGame(game);
                    return new Response(true, "Item has been added to the backpack: " + droppedSlot.getItem().getName() + " x(" + droppedItem.getQuantity() + ").");
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                    GameRepository.saveGame(game);
                    return new Response(true, "Item has been added to the backpack: " + droppedSlot.getItem().getName() + " x(" + droppedItem.getQuantity() + ").");
                }
            }
        }

        if (targetCell.getObjectOnCell() instanceof ArtisanBlock) {
            ArtisanBlock block = (ArtisanBlock) targetCell.getObjectOnCell();
            Slot droppedSlot = new Slot(new Misc(MiscType.getMiscTypeByName(block.getArtisanType().name), Quality.DEFAULT), 1);

            Backpack backpack = player.getInventory();
            Slot backpackSlot = backpack.getSlotByItemName(droppedSlot.getItem().getName());

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (backpackSlot == null) {
                    GameRepository.saveGame(game);
                    return new Response(false, "Backpack was full! Couldn't retrieve item from the ground.");
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                    GameRepository.saveGame(game);
                    return new Response(true, "Item has been added to the backpack: " + droppedSlot.getItem().getName() + " x(" + 1 + ").");
                }
            } else {
                if (backpackSlot == null) {
                    backpack.getSlots().add(droppedSlot);
                    targetCell.setObjectOnCell(new EmptyCell());
                    GameRepository.saveGame(game);
                    return new Response(true, "Item has been added to the backpack: " + droppedSlot.getItem().getName() + " x(" + 1 + ").");
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                    GameRepository.saveGame(game);
                    return new Response(true, "Item has been added to the backpack: " + droppedSlot.getItem().getName() + " x(" + 1 + ").");
                }
            }
        }

        if (targetCell.getObjectOnCell() instanceof EmptyCell) {
            targetCell.setTilled(false);
            GameRepository.saveGame(game);
            return new Response(true, "Target cell has been untilled.");
        }

        GameRepository.saveGame(game);
        return new Response(false, "No operation was performed.");
    }

    private static Response handleAxeUse(Request request, Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. " +
                    "You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null) {
            return new Response(false, "Target cell not found.");
        }

        player.setEnergy(player.getEnergy() - energyCost);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energyCost);

        if (!(targetCell.getObjectOnCell() instanceof Tree tree)) {
            GameRepository.saveGame(game);
            return new Response(false, "Target cell is invalid.");
        }

        if (tree.getTreeType() == TreeType.NORMAL_TREE) {
            int amountOfWood = (int) (Math.random() * 4 + 2);
            targetCell.setObjectOnCell(new Tree(TreeType.TREE_BARK, game.getDate()));

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName("Wood");

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (slot == null) {
                    GameRepository.saveGame(game);
                    return new Response(false,
                            "Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack.");
                }
                slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
                GameRepository.saveGame(game);
                return new Response(true, "You received " + amountOfWood + " wood.");
            } else {
                if (slot == null) {
                    backpack.getSlots().add(new Slot(new Misc(MiscType.WOOD, Quality.DEFAULT), amountOfWood));
                } else {
                    slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
                }
                GameRepository.saveGame(game);
                return new Response(true, "You received " + amountOfWood + " wood.");
            }
        } else if (tree.getTreeType() == TreeType.TREE_BARK) {
            int amountOfWood = (int) (Math.random() * 2 + 1);
            targetCell.setObjectOnCell(new EmptyCell());

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName("Wood");

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (slot == null) {
                    GameRepository.saveGame(game);
                    return new Response(false,
                            "Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack.");
                }
                slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
                GameRepository.saveGame(game);
                return new Response(true, "You received " + amountOfWood + " wood.");
            } else {
                if (slot == null) {
                    backpack.getSlots().add(new Slot(new Misc(MiscType.WOOD, Quality.DEFAULT), amountOfWood));
                } else {
                    slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
                }
                GameRepository.saveGame(game);
                return new Response(true, "You received " + amountOfWood + " wood.");
            }
        } else if (tree.getTreeType() == TreeType.BURNT_TREE) {
            targetCell.setObjectOnCell(new EmptyCell());

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(ForagingMineralsType.COAL.name);

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                    GameRepository.saveGame(game);
                    return new Response(false,
                            "Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack.");
                }

                Slot slotToAdd = tree.getTreeType().fruitItem.createAmountOfItem(1);
                backpack.getSlots().add(slotToAdd);
                GameRepository.saveGame(game);
                return new Response(true, "You received " + 1 + " coal.");
            } else {
                slot.setCount(Math.min(slot.getCount() + 1, slot.getItem().getMaxStackSize()));
                GameRepository.saveGame(game);
                return new Response(true, "You received " + 1 + " coal.");
            }
        } else {
            targetCell.setObjectOnCell(new EmptyCell());

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(TreeSeedsType.findTreeTypeByName(tree.getTreeType().source).name);

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                    GameRepository.saveGame(game);
                    return new Response(false
                            , "Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack.");
                }

                Slot slotToAdd = TreeSeedsType.findTreeTypeByName(tree.getTreeType().source).createAmountOfItem(2);
                backpack.getSlots().add(slotToAdd);

                GameRepository.saveGame(game);
                return new Response(true, "You received " + 2 + " tree seeds.");
            } else {
                slot.setCount(Math.min(slot.getCount() + 2, slot.getItem().getMaxStackSize()));
                GameRepository.saveGame(game);
                return new Response(true, "You received " + 2 + " tree seeds.");
            }
        }
    }

    private static Response handleWateringCanUse(Request request, ToolTypes wateringCanType
            , int skillEnergyDiscount, Quality quality, Tool equippedTool) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. " +
                    "You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null) {
            return new Response(false, "Target cell not found.");
        }

        player.setEnergy(player.getEnergy() - energyCost);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energyCost);

        if (targetCell.getObjectOnCell() instanceof Water) {
            equippedTool.setWaterReserve(wateringCanType.waterCapacity);
            GameRepository.saveGame(game);
            return new Response(true, "Water filled successfully.");
        }

        if (targetCell.getObjectOnCell() instanceof Tree) {
            Tree tree = (Tree) targetCell.getObjectOnCell();
            if (equippedTool.getWaterReserve() == 0) {
                GameRepository.saveGame(game);
                return new Response(false, "Watering can is empty.");
            }
            tree.setHasBeenWateredToday(true);
            GameRepository.saveGame(game);
            return new Response(true, "Tree watered successfully.");
        }

        if (targetCell.getObjectOnCell() instanceof Crop) {
            Crop crop = (Crop) targetCell.getObjectOnCell();
            if (equippedTool.getWaterReserve() == 0) {
                GameRepository.saveGame(game);
                return new Response(false, "Watering can is empty.");
            }
            crop.setHasBeenWateredToday(true);
            GameRepository.saveGame(game);
            return new Response(true, "Crop watered successfully.");
        }

        GameRepository.saveGame(game);
        return new Response(false, "No operation was performed.");
    }

    private static double calculateFishingEnergyCost(int discount, Quality quality) {
        double answer = 0;
        if (quality == Quality.COPPER) {
            answer = 8;
        } else if (quality == Quality.SILVER) {
            answer = 8;
        } else if (quality == Quality.GOLD) {
            answer = 6;
        } else if (quality == Quality.IRIDIUM) {
            answer = 4;
        } else {
            answer = 1;
        }
        answer -= discount;

        if (answer < 0) {
            answer = 0;
        }

        Game game = App.getLoggedInUser().getCurrentGame();
        if (game.getWeatherToday() == Weather.SNOW) {
            answer *= 2;
        }
        if (game.getWeatherToday() == Weather.RAIN) {
            answer *= 1.5;
        }

        return answer;
    }

    private static Response handleFishingRodUse(Request request, Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);


        double energyCost = calculateFishingEnergyCost(skillEnergyDiscount, quality);
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. " +
                    "You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null) {
            return new Response(false, "Target cell not found.");
        }

        player.setEnergy(player.getEnergy() - energyCost);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energyCost);

        if (targetCell.getObjectOnCell() instanceof Water) {
            int randomNumber = (int) (Math.random() * 2);
            double weatherModifier = setWeatherModifierFishing(game);
            int playerLevel = player.getFishingSkill().getLevel().levelNumber;
            int numberOfFishes = (int) (((double) randomNumber)
                    * weatherModifier * (double) (playerLevel + 2));
            ArrayList<FishType> values = getValidFishTypes(game.getSeason(), playerLevel);
            int randomFishNumber = (int) (Math.random() * values.size());
            FishType fishType = values.get(randomFishNumber);

            double qualityNumber = 0;
            double pole = setPoleModifier(quality);
            qualityNumber = (randomNumber * (double) (playerLevel + 2) * pole) / (7.0 - weatherModifier);
            Quality fishQuality = setFishQuality(qualityNumber);
            int price = fishType.price;

            Fish fish = new Fish(fishQuality, fishType);
            Backpack backpack = player.getInventory();
            player.getFishingSkill().setXp(player.getFishingSkill().getXp() + 5);

            if (backpack.getType().getMaxCapacity() >= backpack.getSlots().size()) {
                GameRepository.saveGame(game);
                return new Response(false, "You didn't have enough space. But caught a fish anyways.");
            }

            addFishes(fish, backpack, numberOfFishes);

            GameRepository.saveGame(game);
            return new Response(true, "Fishing done! You caught " + numberOfFishes + " of " + fishType.name);
        }

        GameRepository.saveGame(game);
        return new Response(false, "Target cell isn't water.");
    }

    private static void addFishes(Fish fish, Backpack backpack, int numberOfFishes) {
        for (Slot slot : backpack.getSlots()) {
            if (slot.getItem().getName().compareToIgnoreCase(fish.getName()) == 0) {
                slot.setCount(slot.getCount() + numberOfFishes);
                return;
            }
        }
        Slot newSlot = new Slot(fish, numberOfFishes);
        backpack.addSlot(newSlot);
    }

    private static ArrayList<FishType> getValidFishTypes(Season season, int playerLevel) {
        if (playerLevel == 4) {
            FishType[] values = FishType.values();
            ArrayList<FishType> finalValues = new ArrayList<>();
            for (int i = 0; i < values.length; i++) {
                if (values[i].season == season) {
                    finalValues.add(values[i]);
                }
            }
            return finalValues;
        }
        FishType[] values = FishType.values();
        ArrayList<FishType> finalValues = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            if (values[i].season == season && !values[i].isLegendary) {
                finalValues.add(values[i]);
            }
        }
        return finalValues;
    }

    private static Quality setFishQuality(double qualityNumber) {
        if (qualityNumber >= 0.5 && qualityNumber < 0.7)
            return Quality.SILVER;
        else if (qualityNumber >= 0.7 && qualityNumber < 0.9)
            return Quality.GOLD;
        else if (qualityNumber >= 0.9)
            return Quality.IRIDIUM;
        return Quality.COPPER;
    }

    private static double setPoleModifier(Quality quality) {
        if (quality == Quality.COPPER)
            return 0.1;
        else if (quality == Quality.SILVER)
            return 0.5;
        else if (quality == Quality.GOLD)
            return 0.9;
        return 1.2;
    }

    private static double setWeatherModifierFishing(Game game) {
        double weatherModifier;
        if (game.getWeatherToday().equals(Weather.SUNNY))
            weatherModifier = 1.5;
        else if (game.getWeatherToday().equals(Weather.RAIN))
            weatherModifier = 1.2;
        else if (game.getWeatherToday().equals(Weather.STORM))
            weatherModifier = 0.5;
        else
            weatherModifier = 1.0;
        return weatherModifier;
    }

    private static Response handleScytheUse(Request request) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = getScytheEnergyCost();
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. " +
                    "You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null) {
            return new Response(false, "Target cell not found.");
        }

        player.setEnergy(player.getEnergy() - energyCost);
        player.setUsedEnergyInTurn(player.getUsedEnergyInTurn() + energyCost);

        if (targetCell.getObjectOnCell() instanceof ForagingCrop crop) {

            targetCell.setObjectOnCell(new EmptyCell());

            Backpack backpack = player.getInventory();
            ItemType itemType = crop.getForagingCropsType().getHarvestedItemType();
            Slot slot = null;
            String name = null;

            if (itemType instanceof MiscType) {
                slot = backpack.getSlotByItemName(((MiscType) itemType).name);
                name = ((MiscType) itemType).name;
            } else if (itemType instanceof FoodTypes) {
                slot = backpack.getSlotByItemName(((FoodTypes) itemType).name);
                name = ((FoodTypes) itemType).name;
            }

            int randomInt = (int) (Math.random() * 3) + 1;

            if (player.getInventory().getType().getMaxCapacity() == player.getInventory().getSlots().size()) {
                if (slot == null) {
                    System.out.println("You had no inventory space to collect the materials.");
                } else {
                    slot.setCount(Math.min(slot.getCount() + randomInt, slot.getItem().getMaxStackSize()));
                    System.out.println("Added x(" + randomInt + ") " + name + " to your backpack.");
                }
            } else {
                if (slot == null) {
                    backpack.getSlots().add(itemType.createAmountOfItem(randomInt));
                } else {
                    slot.setCount(Math.min(slot.getCount() + randomInt, slot.getItem().getMaxStackSize()));
                }
                System.out.println("Added x(" + randomInt + ") " + name + " to your backpack.");
            }

            player.getForagingSkill().setXp(player.getForagingSkill().getXp() + 10);
            GameRepository.saveGame(game);
            return new Response(true, "Removed " + name + "from tile.");
        } else if (targetCell.getObjectOnCell() instanceof Crop crop) {

            if (crop.getHarvestDeadLine() == null || crop.getHarvestDeadLine().isAfter(game.getDate())) {
                return new Response(false, "Crop isn't ready for harvest.");
            }

            int amountToHarvest = crop.isGiant() ? 10 : 1;

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(crop.cropSeedsType.name);

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == player.getInventory().getSlots().size()) {
                    GameRepository.saveGame(game);
                    return new Response(false, "Not enough inventory space.");
                }

                Slot newSlot = new Slot(FoodTypes.getFoodTypeByName(crop.cropSeedsType.name), amountToHarvest);
                backpack.getSlots().add(newSlot);

                if (crop.cropSeedsType.oneTime) {
                    targetCell.setObjectOnCell(new EmptyCell());
                } else {
                    crop.setHarvestDeadLine(DateUtility.getLocalDate(game.getDate(), crop.cropSeedsType.regrowthTime));
                }

                player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);

                GameRepository.saveGame(game);
                return new Response(true, "Added x(" + amountToHarvest + ") of " + crop.cropSeedsType.name + " to your backpack.");
            }

            if (crop.cropSeedsType.oneTime) {
                targetCell.setObjectOnCell(new EmptyCell());
            } else {
                crop.setHarvestDeadLine(DateUtility.getLocalDate(game.getDate(), crop.cropSeedsType.regrowthTime));
            }

            player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);

            slot.setCount(Math.min(slot.getCount() + amountToHarvest, slot.getItem().getMaxStackSize()));
            GameRepository.saveGame(game);

            return new Response(true, "Added x(" + amountToHarvest + ") of " + crop.cropSeedsType.name + " to your backpack.");
        } else if (targetCell.getObjectOnCell() instanceof Tree tree) {

            if (tree.getHarvestDeadLine() == null || tree.getHarvestDeadLine().isAfter(game.getDate())) {
                return new Response(false, "Tree isn't ready for harvest.");
            }

            int amountToHarvest = 1;

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(tree.getTreeType().fruitItem.getName());

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == player.getInventory().getSlots().size()) {
                    GameRepository.saveGame(game);
                    return new Response(false, "Not enough inventory space.");
                }

                if (tree.getTreeType() == TreeType.NORMAL_TREE
                        || tree.getTreeType() == TreeType.TREE_BARK
                        || tree.getTreeType() == TreeType.BURNT_TREE) {
                    return new Response(false, "Tree isn't harvestable.");
                } else {
                    tree.setHarvestDeadLine(DateUtility.getLocalDate(game.getDate(), tree.getTreeType().harvestCycleTime));
                }

                Slot newSlot = tree.getTreeType().fruitItem.createAmountOfItem(amountToHarvest);
                backpack.getSlots().add(newSlot);

                player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);

                GameRepository.saveGame(game);
                return new Response(true, "Added x(" + amountToHarvest + ") of " + tree.getTreeType().fruitItem.getName() + " to your backpack.");
            }

            if (tree.getTreeType() == TreeType.NORMAL_TREE
                    || tree.getTreeType() == TreeType.TREE_BARK
                    || tree.getTreeType() == TreeType.BURNT_TREE) {
                return new Response(false, "Can't harvest a normal, burnt tree or bark.");
            } else {
                tree.setHarvestDeadLine(DateUtility.getLocalDate(game.getDate(), tree.getTreeType().harvestCycleTime));
            }

            slot.setCount(Math.min(slot.getCount() + amountToHarvest, slot.getItem().getMaxStackSize()));

            player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);

            GameRepository.saveGame(game);
            return new Response(true, "Added x(" + amountToHarvest + ") of " + tree.getTreeType().fruitItem.getName() + " to your backpack.");

        } else {
            GameRepository.saveGame(game);
            return new Response(false, "Target cell isn't a valid use case of the scythe.");
        }
    }

    private static double getScytheEnergyCost() {
        Game game = App.getLoggedInUser().getCurrentGame();

        double energyCost = 2;
        if (game.getWeatherToday() == Weather.SNOW) {
            energyCost *= 2;
        }
        if (game.getWeatherToday() == Weather.RAIN) {
            energyCost *= 1.5;
        }
        return energyCost;
    }

    private static Response handleMilkPailUse(Request request) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);
        Item equippedItem = player.getEquippedItem();
        Backpack backpack = player.getInventory();
        Slot productSlot = null;

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);
        double energyCost = 4;
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. " +
                    "You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null || !(targetCell.getObjectOnCell() instanceof AnimalBlock)) {
            return new Response(false, "Target cell not found.");
        }
        Animal animal = ((AnimalBlock) targetCell.getObjectOnCell()).animal;
        if (animal == null) {
            GameRepository.saveGame(game);
            return new Response(false, "no animal found");
        }
        if (!animal.getType().equals(AnimalType.COW) && !animal.getType().equals(AnimalType.GOAT)) {
            GameRepository.saveGame(game);
            return new Response(false, "wrong cell selected");
        }
        Item product = animal.product;
        if (product == null) {
            return noProductFoundHandle(animal, equippedItem, player, game);
        }
        return handleCollectProducts(product, backpack, productSlot, animal, player, game);
    }

    private static Response handleShearUse(Request request) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getCurrentFarm(game);
        Item equippedItem = player.getEquippedItem();
        Backpack backpack = player.getInventory();
        Slot productSlot = null;

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);
        double energyCost = 4;
        double currentEnergyUsed = player.getUsedEnergyInTurn();
        double playerEnergy = player.getEnergy();

        if (energyCost + currentEnergyUsed > 50) {
            return new Response(false, "You can't perform this activity. " +
                    "You will exceed your energy usage limit.");
        }

        if (playerEnergy - energyCost < 0) {
            return new Response(false, "You don't have enough energy.");
        }

        if (targetCell == null || !(targetCell.getObjectOnCell() instanceof AnimalBlock)) {
            return new Response(false, "Target cell not found.");
        }
        Animal animal = ((AnimalBlock) targetCell.getObjectOnCell()).animal;
        if (animal == null) {
            GameRepository.saveGame(game);
            return new Response(false, "no animal found");
        }
        if (!animal.getType().equals(AnimalType.SHEEP)) {
            GameRepository.saveGame(game);
            return new Response(false, "wrong cell selected");
        }
        Item product = animal.product;
        if (product == null) {
            return noProductFoundHandle(animal, equippedItem, player, game);
        }
        return handleCollectProducts(product, backpack, productSlot, animal, player, game);
    }
}