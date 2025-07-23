package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.*;
import com.common.models.enums.Quality;
import com.common.models.enums.SkillLevel;
import com.common.models.enums.types.AnimalType;
import com.common.models.enums.types.itemTypes.*;
import com.common.models.enums.types.mapObjectTypes.TreeType;
import com.common.models.enums.worldEnums.Weather;
import com.common.models.items.*;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.*;
import com.server.GameServers.GameServer;
import com.server.utilities.DateUtility;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.HashMap;

public class WorldController extends Controller {
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

    private static void handleHoeUse(Context ctx, GameData game, Player player, String direction, Quality quality, int skillEnergyDiscount) {
        float[] dxAndDy = getXAndYIncrement(direction);
        float dx = dxAndDy[0];
        float dy = dxAndDy[1];

        Farm farm = player.getCurrentFarm(game);

        float playerX = player.getCoordinate().getX() / 32;
        float playerY = 49 - player.getCoordinate().getY() / 32;
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality, game);
        double playerEnergy = player.getEnergy();

        if (playerEnergy - energyCost < 0) {
            ctx.json(Response.BAD_REQUEST.setMessage("You don't have enough energy."));
            return;
        }

        if (targetCell == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Cell not found"));
            return;
        }

        player.setEnergy(player.getEnergy() - energyCost);

        if (!(targetCell.getObjectOnCell() instanceof EmptyCell)) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid cell"));
            return;
        }

        targetCell.setTilled(true);
    }

    private static void handlePickaxeUse(Context ctx, GameData game, Player player, String direction, SkillLevel skillLevel
        , Quality quality, int skillEnergyDiscount) {
        float[] dxAndDy = getXAndYIncrement(direction);
        float dx = dxAndDy[0] / 60;
        float dy = dxAndDy[1] / 60;

        Farm farm = player.getCurrentFarm(game);

        float playerX = (player.getCoordinate().getX() + dx) / 32;
        float playerY = 50 - (player.getCoordinate().getY() + dy) / 32;
        Cell targetCell = farm.findCellByCoordinate(playerX, playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality, game);
        double playerEnergy = player.getEnergy();

        if (playerEnergy - energyCost < 0) {
            ctx.json(Response.BAD_REQUEST.setMessage("Out of energy"));
            return;
        }

        if (targetCell == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Cell not found"));
            return;
        }

        player.setEnergy(player.getEnergy() - energyCost);

        //Mine Cells : x : [0, 9] y : [0, 11]
        if (targetCell.getObjectOnCell() instanceof ForagingMineral) {

            ForagingMineralsType type = ((ForagingMineral) targetCell.getObjectOnCell()).getFMType();

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(type.name);

            if (!canPickaxeMineThis(type, quality)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Can not mine this"));
                return;
            }

            player.getUnbuffedMiningSkill().setXp(player.getUnbuffedMiningSkill().getXp() + 10);

            float cellX = targetCell.getCoordinate().getX();
            float cellY = targetCell.getCoordinate().getY();

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
                        Slot(new ForagingMineralItem(Quality.DEFAULT, type), count));
                } else {
                    slot.setCount(Math.min(slot.getItem().getMaxStackSize(), slot.getCount() + count));
                }
                System.out.println("Added " + count + " to backpack.");
            }
        } else if (targetCell.getObjectOnCell() instanceof DroppedItem) {

            DroppedItem droppedItem = (DroppedItem) targetCell.getObjectOnCell();
            Slot droppedSlot = new Slot(droppedItem.getItem(), droppedItem.getQuantity());
            Backpack backpack = player.getInventory();
            Slot backpackSlot = backpack.getSlotByItemName(droppedSlot.getItem().getName());

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (backpackSlot == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Can not drop this"));
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                }
            } else {
                if (backpackSlot == null) {
                    backpack.getSlots().add(droppedSlot);
                    targetCell.setObjectOnCell(new EmptyCell());
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                }
            }
        } else if (targetCell.getObjectOnCell() instanceof ArtisanBlock) {
            ArtisanBlock block = (ArtisanBlock) targetCell.getObjectOnCell();
            Slot droppedSlot = new Slot(new Misc(MiscType.getMiscTypeByName(block.getArtisanType().name), Quality.DEFAULT), 1);

            Backpack backpack = player.getInventory();
            Slot backpackSlot = backpack.getSlotByItemName(droppedSlot.getItem().getName());

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (backpackSlot == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("slot is null"));
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                }
            } else {
                if (backpackSlot == null) {
                    backpack.getSlots().add(droppedSlot);
                    targetCell.setObjectOnCell(new EmptyCell());
                } else {
                    backpackSlot.setCount(Math.min(backpackSlot.getCount() + droppedSlot.getCount(), backpackSlot.getItem().getMaxStackSize()));
                    targetCell.setObjectOnCell(new EmptyCell());
                }
            }
        } else if (targetCell.getObjectOnCell() instanceof EmptyCell) {
            targetCell.setTilled(false);
        } else {
            ctx.json(Response.BAD_REQUEST.setMessage("No operation was performed."));
        }

    }

    private static void handleAxeUse(Context ctx, GameData game, Player player, String direction, Quality quality, int skillEnergyDiscount) {
        float[] dxAndDy = getXAndYIncrement(direction);
        float dx = dxAndDy[0];
        float dy = dxAndDy[1];

        Farm farm = player.getCurrentFarm(game);

        float playerX = player.getCoordinate().getX() / 32;
        float playerY = 49 - player.getCoordinate().getY() / 32;
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality, game);
        double playerEnergy = player.getEnergy();

        if (playerEnergy - energyCost < 0) {
            ctx.json(Response.BAD_REQUEST.setMessage("No energy"));
            return;
        }

        if (targetCell == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Target cell is null"));
            return;
        }

        player.setEnergy(player.getEnergy() - energyCost);

        if (!(targetCell.getObjectOnCell() instanceof Tree tree)) {
            ctx.json(Response.BAD_REQUEST.setMessage("Target cell is not a tree"));
            return;
        }

        if (tree.getTreeType() == TreeType.NORMAL_TREE) {
            int amountOfWood = (int) (Math.random() * 4 + 2);
            targetCell.setObjectOnCell(new Tree());

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName("Wood");

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (slot == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack."));
                    return;
                }
                slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
            } else {
                if (slot == null) {
                    backpack.getSlots().add(new Slot(new Misc(MiscType.WOOD, Quality.DEFAULT), amountOfWood));
                } else {
                    slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
                }
            }
        } else if (tree.getTreeType() == TreeType.TREE_BARK) {
            int amountOfWood = (int) (Math.random() * 2 + 1);
            targetCell.setObjectOnCell(new EmptyCell());

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName("Wood");

            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                if (slot == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack."));
                    return;
                }
                slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
            } else {
                if (slot == null) {
                    backpack.getSlots().add(new Slot(new Misc(MiscType.WOOD, Quality.DEFAULT), amountOfWood));
                } else {
                    slot.setCount(Math.min(slot.getCount() + amountOfWood, slot.getItem().getMaxStackSize()));
                }
            }
        } else if (tree.getTreeType() == TreeType.BURNT_TREE) {
            targetCell.setObjectOnCell(new EmptyCell());

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(ForagingMineralsType.COAL.name);

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack."));
                    return;
                }

                Slot slotToAdd = tree.getTreeType().fruitItem.createAmountOfItem(1, Quality.DEFAULT);
                backpack.getSlots().add(slotToAdd);
            } else {
                slot.setCount(Math.min(slot.getCount() + 1, slot.getItem().getMaxStackSize()));
            }
        } else {
            targetCell.setObjectOnCell(new EmptyCell());

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(TreeSeedsType.findTreeTypeByName(tree.getTreeType().source).name);

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Tree was chopped; however, your backpack was full. Wood wasn't added to your backpack."));
                    return;
                }

                Slot slotToAdd = TreeSeedsType.findTreeTypeByName(tree.getTreeType().source).createAmountOfItem(2, Quality.DEFAULT);
                backpack.getSlots().add(slotToAdd);

            } else {
                slot.setCount(Math.min(slot.getCount() + 2, slot.getItem().getMaxStackSize()));
            }
        }
    }

    private static void handleWateringCanUse(Context ctx, GameData game, Player player, String direction, ToolTypes wateringCanType
        , int skillEnergyDiscount, Quality quality, Tool equippedTool) {
        float[] dxAndDy = getXAndYIncrement(direction);
        float dx = dxAndDy[0];
        float dy = dxAndDy[1];

        Farm farm = player.getCurrentFarm(game);

        float playerX = player.getCoordinate().getX() / 32;
        float playerY = 49 - player.getCoordinate().getY() / 32;
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = calculateEnergyCostForHoeAxePickaxeWaterCan(skillEnergyDiscount, quality, game);
        double playerEnergy = player.getEnergy();

        if (playerEnergy - energyCost < 0) {
            ctx.json(Response.BAD_REQUEST.setMessage("No energy"));
            return;
        }

        if (targetCell == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Target cell is null"));
            return;
        }

        player.setEnergy(player.getEnergy() - energyCost);

        if (targetCell.getObjectOnCell() instanceof Water) {
            equippedTool.setWaterReserve(wateringCanType.waterCapacity);
        } else if (targetCell.getObjectOnCell() instanceof Tree tree) {
            if (equippedTool.getWaterReserve() == 0) {
                ctx.json(Response.BAD_REQUEST.setMessage("Can is empty"));
                return;
            }
            tree.setHasBeenWateredToday(true);
        } else if (targetCell.getObjectOnCell() instanceof Crop crop) {
            if (equippedTool.getWaterReserve() == 0) {
                ctx.json(Response.BAD_REQUEST.setMessage("Can is empty"));
                return;
            }
            crop.setHasBeenWateredToday(true);
            crop.setLastWateringDate(game.getDate());
        } else {
            ctx.json(Response.BAD_REQUEST.setMessage("No operation was found"));
            return;
        }
    }

    private static void handleScytheUse(Context ctx, GameData game, Player player, String direction) {
        float[] dxAndDy = getXAndYIncrement(direction);
        float dx = dxAndDy[0];
        float dy = dxAndDy[1];

        Farm farm = player.getCurrentFarm(game);

        float playerX = player.getCoordinate().getX() / 32;
        float playerY = 49 - player.getCoordinate().getY() / 32;
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);

        double energyCost = getScytheEnergyCost(game);
        double playerEnergy = player.getEnergy();

        if (playerEnergy - energyCost < 0) {
            ctx.json(Response.BAD_REQUEST.setMessage("No energy"));
            return;
        }

        if (targetCell == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Target cell is null"));
            return;
        }

        player.setEnergy(player.getEnergy() - energyCost);

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
                    backpack.getSlots().add(itemType.createAmountOfItem(randomInt, Quality.DEFAULT));
                } else {
                    slot.setCount(Math.min(slot.getCount() + randomInt, slot.getItem().getMaxStackSize()));
                }
                System.out.println("Added x(" + randomInt + ") " + name + " to your backpack.");
            }

            player.getUnbuffedForagingSkill().setXp(player.getUnbuffedForagingSkill().getXp() + 10);
        } else if (targetCell.getObjectOnCell() instanceof Crop crop) {

            if (crop.getHarvestDeadLine() == null || crop.getHarvestDeadLine().isAfter(game.getDate())) {
                ctx.json(Response.BAD_REQUEST.setMessage("Crop isn't ready for harvest."));
                return;
            }

            int amountToHarvest = crop.isGiant() ? 10 : 1;

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(crop.cropSeedsType.name);

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == player.getInventory().getSlots().size()) {
                    ctx.json(Response.BAD_REQUEST.setMessage("No enough space"));
                    return;
                }

                Slot newSlot = new Slot(FoodTypes.getFoodTypeByName(crop.cropSeedsType.name), amountToHarvest);
                backpack.getSlots().add(newSlot);

                if (crop.cropSeedsType.oneTime) {
                    targetCell.setObjectOnCell(new EmptyCell());
                } else {
                    crop.setHarvestDeadLine(DateUtility.getLocalDateTime(game.getDate(), crop.cropSeedsType.regrowthTime));
                }

                player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);

            }

            if (crop.cropSeedsType.oneTime) {
                targetCell.setObjectOnCell(new EmptyCell());
            } else {
                crop.setHarvestDeadLine(DateUtility.getLocalDateTime(game.getDate(), crop.cropSeedsType.regrowthTime));
            }

            player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);

            slot.setCount(Math.min(slot.getCount() + amountToHarvest, slot.getItem().getMaxStackSize()));

        } else if (targetCell.getObjectOnCell() instanceof Tree tree) {

            if (tree.getHarvestDeadLine() == null || tree.getHarvestDeadLine().isAfter(game.getDate())) {
                ctx.json(Response.BAD_REQUEST.setMessage("Tree isn't ready for harvest."));
                return;
            }

            int amountToHarvest = 1;

            Backpack backpack = player.getInventory();
            Slot slot = backpack.getSlotByItemName(tree.getTreeType().fruitItem.getName());

            if (slot == null) {
                if (backpack.getType().getMaxCapacity() == player.getInventory().getSlots().size()) {
                    ctx.json(Response.BAD_REQUEST.setMessage("No enough space"));
                    return;
                }

                if (tree.getTreeType() == TreeType.NORMAL_TREE
                    || tree.getTreeType() == TreeType.TREE_BARK
                    || tree.getTreeType() == TreeType.BURNT_TREE) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Tree isn't harvestable"));
                    return;
                } else {
                    tree.setHarvestDeadLine(DateUtility.getLocalDateTime(game.getDate(), tree.getTreeType().harvestCycleTime));
                }

                Slot newSlot = tree.getTreeType().fruitItem.createAmountOfItem(amountToHarvest, Quality.DEFAULT);
                backpack.getSlots().add(newSlot);

                player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);
            }

            if (tree.getTreeType() == TreeType.NORMAL_TREE
                || tree.getTreeType() == TreeType.TREE_BARK
                || tree.getTreeType() == TreeType.BURNT_TREE) {
                ctx.json(Response.BAD_REQUEST.setMessage("Tree isn't harvestable"));
                return;
            } else {
                tree.setHarvestDeadLine(DateUtility.getLocalDateTime(game.getDate(), tree.getTreeType().harvestCycleTime));
            }

            slot.setCount(Math.min(slot.getCount() + amountToHarvest, slot.getItem().getMaxStackSize()));

            player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + 5);

        } else {
            ctx.json(Response.BAD_REQUEST.setMessage("No operation was found"));
        }
    }

    private static double getScytheEnergyCost(GameData gameData) {

        double energyCost = 2;
        if (gameData.getWeatherToday() == Weather.SNOW) {
            energyCost *= 2;
        }
        if (gameData.getWeatherToday() == Weather.RAIN) {
            energyCost *= 1.5;
        }
        return energyCost;
    }

    private static void handleMilkPailUse(Context ctx, GameData game, Player player, String direction) {
        float[] dxAndDy = getXAndYIncrement(direction);
        float dx = dxAndDy[0];
        float dy = dxAndDy[1];

        Farm farm = player.getCurrentFarm(game);
        Item equippedItem = player.getEquippedItem();
        Backpack backpack = player.getInventory();
        Slot productSlot = null;

        float playerX = player.getCoordinate().getX() / 32;
        float playerY = 49 - player.getCoordinate().getY() / 32;
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);
        double energyCost = 4;
        double playerEnergy = player.getEnergy();

        if (playerEnergy - energyCost < 0) {
            ctx.json(Response.BAD_REQUEST.setMessage("No enough energy"));
            return;
        }

        if (targetCell == null || !(targetCell.getObjectOnCell() instanceof AnimalBlock)) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid cell"));
            return;
        }
        Animal animal = ((AnimalBlock) targetCell.getObjectOnCell()).animal;
        if (animal == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid animal"));
            return;
        }
        if (!animal.getType().equals(AnimalType.COW) && !animal.getType().equals(AnimalType.GOAT)) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid cell"));
            return;
        }
        Item product = animal.product;
        if (product == null) {
//            return noProductFoundHandle(animal, equippedItem, player, game);
        }
//        return handleCollectProducts(product, backpack, productSlot, animal, player, gameData);
    }

    private static void handleShearUse(Context ctx, GameData game, Player player, String direction) {
        float[] dxAndDy = getXAndYIncrement(direction);
        float dx = dxAndDy[0];
        float dy = dxAndDy[1];

        Farm farm = player.getCurrentFarm(game);
        Item equippedItem = player.getEquippedItem();
        Backpack backpack = player.getInventory();
        Slot productSlot = null;

        float playerX = player.getCoordinate().getX() / 32;
        float playerY = 49 - player.getCoordinate().getY() / 32;
        Cell targetCell = farm.findCellByCoordinate(dx + playerX, dy + playerY);
        double energyCost = 4;
        double playerEnergy = player.getEnergy();

        if (playerEnergy - energyCost < 0) {
            ctx.json(Response.BAD_REQUEST.setMessage("No enough energy"));
            return;
        }

        if (targetCell == null || !(targetCell.getObjectOnCell() instanceof AnimalBlock)) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid cell"));
            return;
        }
        Animal animal = ((AnimalBlock) targetCell.getObjectOnCell()).animal;
        if (animal == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid animal"));
            return;
        }
        if (!animal.getType().equals(AnimalType.SHEEP)) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid cell"));
            return;
        }
        Item product = animal.product;
        if (product == null) {
//            return noProductFoundHandle(animal, equippedItem, player, gameData);
        }
//        return handleCollectProducts(product, backpack, productSlot, animal, player, gameData);
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

    private static double calculateEnergyCostForHoeAxePickaxeWaterCan(int discount, Quality quality, GameData gameData) {
        double answer = 5 - quality.getQualityLevel() - discount;
        if (answer < 0) {
            answer = 0;
        }
        if (gameData.getWeatherToday() == Weather.SNOW) {
            answer *= 2;
        }
        if (gameData.getWeatherToday() == Weather.RAIN) {
            answer *= 1.5;
        }

        return answer;
    }

    private static float[] getXAndYIncrement(String direction) {
        if (direction.compareToIgnoreCase("down") == 0) {
            return new float[]{0, -16 * 16};
        } else if (direction.compareToIgnoreCase("up") == 0) {
            return new float[]{0, 16 * 16};
        } else if (direction.compareToIgnoreCase("right") == 0) {
            return new float[]{16 * 16, 0};
        } else if (direction.compareToIgnoreCase("left") == 0) {
            return new float[]{-16 * 16, 0};
        } else if (direction.compareToIgnoreCase("down_right") == 0) {
            return new float[]{1, -1};
        } else if (direction.compareToIgnoreCase("down_left") == 0) {
            return new float[]{-1, -1};
        } else if (direction.compareToIgnoreCase("up_right") == 0) {
            return new float[]{1, 1};
        } else if (direction.compareToIgnoreCase("up_left") == 0) {
            return new float[]{-1, 1};
        } else {
            return null;
        }
    }

    public void buildGreenhouse(Context ctx, GameServer gs) {
        try {
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Farm farm = player.getFarm();
            Backpack backpack = player.getInventory();
            Cell testCell = Farm.getCellByCoordinate(25, 4, farm.getCells());
            if (testCell.getObjectOnCell() instanceof Water) {
                ctx.json(Response.BAD_REQUEST.setMessage("You are already built."));
                return;
            }
            Slot slot = backpack.getSlotByItemName(MiscType.WOOD.name);

            if (slot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have any wood."));
                return;
            }

            if (slot.getCount() < 500) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have any wood."));
                return;
            }

            if (player.getMoney(game) < 1000) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have enough money."));
                return;
            }

            slot.setCount(slot.getCount() - 500);

            if (slot.getCount() == 0) {
                backpack.getSlots().remove(slot);
            }

            player.setMoney(player.getMoney(game) - 1000, game);

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

            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void toolUse(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String direction = (String) body.get("direction");
            String toolName = (String) body.get("toolName");
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            Item item = player.getInventory().getSlotByItemName(toolName).getItem();
            player.setEquippedItem(item);

            if (player.getEquippedItem() == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have any equipped item."));
                return;
            }
            if (!(player.getEquippedItem() instanceof Tool)) {
                ctx.json(Response.BAD_REQUEST.setMessage("You don't have any equipped tool."));
                return;
            }
            if (getXAndYIncrement(direction) == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Invalid direction."));
                return;
            }
            Tool equippedTool = (Tool) player.getEquippedItem();
            ToolTypes toolType = equippedTool.getType();
            if (toolType == ToolTypes.HOE) {
                handleHoeUse(ctx, game, player, direction, equippedTool.getQuality()
                    , player.getFarmingSkill().getLevel().energyCostDiscount);
            } else if (toolType == ToolTypes.PICKAXE) {
                handlePickaxeUse(ctx, game, player, direction
                    , player.getMiningSkill().getLevel()
                    , equippedTool.getQuality()
                    , player.getMiningSkill().getLevel().energyCostDiscount);
            } else if (toolType == ToolTypes.AXE) {
                handleAxeUse(ctx, game, player, direction, equippedTool.getQuality()
                    , player.getForagingSkill().getLevel().energyCostDiscount);
            } else if (toolType == ToolTypes.WATERING_CAN_DEFAULT) {
                handleWateringCanUse(ctx, game, player, direction, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
            } else if (toolType == ToolTypes.WATERING_CAN_COPPER) {
                handleWateringCanUse(ctx, game, player, direction, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
            } else if (toolType == ToolTypes.WATERING_CAN_IRON) {
                handleWateringCanUse(ctx, game, player, direction, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
            } else if (toolType == ToolTypes.WATERING_CAN_GOLD) {
                handleWateringCanUse(ctx, game, player, direction, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
            } else if (toolType == ToolTypes.WATERING_CAN_IRIDIUM) {
                handleWateringCanUse(ctx, game, player, direction, toolType
                    , player.getFarmingSkill().getLevel().energyCostDiscount, equippedTool.getQuality()
                    , equippedTool);
            } else if (toolType == ToolTypes.SCYTHE) {
                handleScytheUse(ctx, game, player, direction);
            } else if (toolType == ToolTypes.MILK_PAIL) {
                // TODO fix the function
                handleMilkPailUse(ctx, game, player, direction);
            } else if (toolType == ToolTypes.SHEAR) {
                // TODO fix the function
                handleShearUse(ctx, game, player, direction);
            } else {
                ctx.json(Response.BAD_REQUEST.setMessage("Unknown tool type: " + toolType));
                return;
            }
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void fishing(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            int xpGained = (Integer) body.get("xpGained");
            int count = (Integer) body.get("count");
            String fishType = (String) body.get("fishType");
            String quality = (String) body.get("quality");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);

            Quality fishQuality = Quality.getQualityByName(quality);
            FishType type = FishType.getFishType(fishType);
            Fish fish = new Fish(fishQuality, type);
            Backpack backpack = player.getInventory();
            if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
                ctx.json(Response.BAD_REQUEST.setMessage("You cannot have more than " + backpack.getSlots().size() + " slots"));
                return;
            }
            addFishes(fish, backpack, count);
            // TODO handle xp
            String playerJson = GameGSON.gson.toJson(player);
            ctx.json(Response.OK.setBody(playerJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            msg.put("player", playerJson);
            gs.broadcast(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
