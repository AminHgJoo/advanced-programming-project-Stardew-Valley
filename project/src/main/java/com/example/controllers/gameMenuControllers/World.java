package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.Quality;
import com.example.models.enums.Weather;
import com.example.models.enums.types.ForagingCropsType;
import com.example.models.enums.types.MiscType;
import com.example.models.enums.types.ToolTypes;
import com.example.models.items.Misc;
import com.example.models.items.Tool;
import com.example.models.mapModels.Cell;
import com.example.models.mapModels.Farm;
import com.example.models.mapObjects.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//TODO: Handle all skills.
public class World extends Controller {
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
        currentGame.setDate(nextDateTime);
        currentGame.checkSeasonChange();
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
        currentGame.setDate(nextDateTime);
        currentGame.checkSeasonChange();
        GameRepository.saveGame(currentGame);
        return new Response(true, "Date set successfully.");
    }

    public static Response handleSeasonQuery(Request request) {
        return new Response(true, App.getLoggedInUser().getCurrentGame().getSeason().toString());
    }

    public static Response handleCheatThor(Request request) {
        return null;
        //TODO: lightning doesn't do anything right now. implement later on.
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

    //TODO: Implement.
    public static Response handleGreenhouseBuilding(Request request) {
        //TODO: once items are added, implement.
        return null;
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
            return handlePickaxeUse(request, equippedTool.getQuality()
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
        if (direction.compareToIgnoreCase("u") == 0) {
            return new int[]{0, -1};
        } else if (direction.compareToIgnoreCase("d") == 0) {
            return new int[]{0, 1};
        } else if (direction.compareToIgnoreCase("r") == 0) {
            return new int[]{1, 0};
        } else if (direction.compareToIgnoreCase("l") == 0) {
            return new int[]{-1, 0};
        } else if (direction.compareToIgnoreCase("ur") == 0) {
            return new int[]{1, -1};
        } else if (direction.compareToIgnoreCase("ul") == 0) {
            return new int[]{-1, -1};
        } else if (direction.compareToIgnoreCase("dr") == 0) {
            return new int[]{1, 1};
        } else if (direction.compareToIgnoreCase("dl") == 0) {
            return new int[]{-1, 1};
        } else {
            return null;
        }
    }

    private static int calculateEnergyCostForHoeAxePickaxeWaterCan(int discount, Quality quality) {
        int answer = 5 - quality.getQualityLevel() - discount;

        if (answer < 0) {
            answer = 0;
        }
        return answer;
    }

    //TODO: IMPORTANT: SAVE GAME!!!!!!!

    private static Response handleHoeUse(Request request, Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getFarm();

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        int energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
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

    //TODO: HANDLE MINING MINERALS AND STONES.
    private static Response handlePickaxeUse(Request request, Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getFarm();

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        int energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
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

        if (targetCell.getObjectOnCell() instanceof ForagingMineral) {
            //TODO: MINE TILE AND ADD STUFF TO INVENTORY BASED ON SKILL.


            GameRepository.saveGame(game);
            return new Response(true, "Mined stone/mineral at target cell.");
        }

        if (targetCell.getObjectOnCell() instanceof DroppedItem) {
            targetCell.setObjectOnCell(new EmptyCell());
            GameRepository.saveGame(game);
            return new Response(true, "Destroyed item on target cell.");
        }

        if (targetCell.getObjectOnCell() instanceof EmptyCell) {
            targetCell.setTilled(false);
            GameRepository.saveGame(game);
            return new Response(true, "Target cell has been untilled.");
        }

        GameRepository.saveGame(game);
        return new Response(false, "No operation was performed.");
    }

    //TODO: HANDLE CUTTING TREES AND ADDING WOOD.
    private static Response handleAxeUse(Request request, Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getFarm();

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        int energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
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

        if (!(targetCell.getObjectOnCell() instanceof Tree)) {
            GameRepository.saveGame(game);
            return new Response(false, "Target cell is invalid.");
        }

        //TODO: Handle wood chopping and syrup extraction.

        GameRepository.saveGame(game);
        return new Response(true, "Axe used successfully.");
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
        Farm farm = player.getFarm();

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        int energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality);
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


    private static int calculateFishingEnergyCost(int discount, Quality quality) {
        int answer = 0;
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
        return answer - discount;
    }

    //TODO: Implement fishing once reached that part in the document.
    private static Response handleFishingRodUse(Request request, Quality quality, int skillEnergyDiscount) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getFarm();

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        int energyCost = calculateFishingEnergyCost(skillEnergyDiscount, quality);
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
            //TODO: Perform fishing.

            GameRepository.saveGame(game);
            return new Response(true, "Fishing done! You caught <quantity> of <fish>");
        }

        GameRepository.saveGame(game);
        return new Response(false, "Target cell isn't water.");
    }

    private static Response handleScytheUse(Request request) {
        String direction = request.body.get("direction");
        int[] dxAndDy = getXAndYIncrement(direction);
        int dx = dxAndDy[0];
        int dy = dxAndDy[1];

        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        Farm farm = player.getFarm();

        int playerX = player.getCoordinate().getX();
        int playerY = player.getCoordinate().getY();
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        int energyCost = 2;
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

        if (targetCell.getObjectOnCell() instanceof ForagingCrop) {
            ForagingCrop crop = (ForagingCrop) targetCell.getObjectOnCell();

            if (crop.type.equals(ForagingCropsType.GRASS)) {

                targetCell.setObjectOnCell(new EmptyCell());

                if (player.getInventory().getType().getMaxCapacity() == player.getInventory().getSlots().size()) {
                    System.out.println("You had no inventory space to collect the materials.");
                } else {
                    Backpack backpack = player.getInventory();
                    Slot slot = backpack.getSlotByItemName("Fiber");
                    if (slot == null) {
                        backpack.getSlots()
                                .add(new Slot(new Misc(Quality.DEFAULT, 1000, 0, 0, "Fiber", MiscType.FIBER), 1));
                    } else {
                        slot.setCount(slot.getCount() + 1);
                    }
                    System.out.println("Added one Fiber to your backpack.");
                }

                GameRepository.saveGame(game);
                return new Response(true, "Grass removed from tile.");
            }
        }

        GameRepository.saveGame(game);
        return new Response(false, "Target cell isn't grass.");
    }

    //TODO: Implement later.
    private static Response handleMilkPailUse(Request request) {
        return null;
    }

    //TODO: Implement later.
    private static Response handleShearUse(Request request) {
        return null;
    }

    //TODO: Implement later.
    public static Response handleBuildBuilding(Request request) {
        return null;
    }
}
