package com.example.views;

import com.example.controllers.Controller;
import com.example.controllers.gameMenuControllers.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.GameMenuCommands;

public class GameMenu implements Menu {

    public void handleMenu(String input) {
        Response response = null;

        if (GameMenuCommands.GAME_NEW.matches(input)) {
            response = getNewGameResponse(input);
        } else if (GameMenuCommands.GAME_MAP.matches(input) && LoadingSavingTurnHandling.isWaitingForChoosingMap) {
            response = getGameMapResponse(input);
        } else if (GameMenuCommands.LOAD_GAME.matches(input)) {
            response = getLoadGameResponse(input);
        } else if (GameMenuCommands.EXIT_GAME.matches(input)) {
            response = getExitGameResponse(input);
        } else if (GameMenuCommands.NEXT_TURN.matches(input)) {
            response = getNextTurnResponse(input);
        } else if (GameMenuCommands.TIME.matches(input)) {
            response = getTimeResponse(input);
        } else if (GameMenuCommands.DATE.matches(input)) {
            response = getDateResponse(input);
        } else if (GameMenuCommands.DATETIME.matches(input)) {
            response = getDateTimeResponse(input);
        } else if (GameMenuCommands.DAY_OF_WEEK.matches(input)) {
            response = getDayOfWeekResponse(input);
        } else if (GameMenuCommands.CHEAT_ADVANCE_TIME.matches(input)) {
            response = getCheatAdvanceTimeResponse(input);
        } else if (GameMenuCommands.CHEAT_ADVANCE_DATE.matches(input)) {
            response = getCheatAdvanceDateResponse(input);
        } else if (GameMenuCommands.SEASON.matches(input)) {
            response = getSeasonResponse(input);
        } else if (GameMenuCommands.CHEAT_THOR.matches(input)) {
            response = getCheatThorResponse(input);
        } else if (GameMenuCommands.WEATHER.matches(input)) {
            response = getWeatherResponse(input);
        } else if (GameMenuCommands.WEATHER_FORECAST.matches(input)) {
            response = getWeatherForecastResponse(input);
        } else if (GameMenuCommands.CHEAT_WEATHER_SET.matches(input)) {
            response = getWeatherSetResponse(input);
        } else if (GameMenuCommands.GREEN_HOUSE_BUILD.matches(input)) {
            response = getGreenhouseBuildResponse(input);
        } else if (GameMenuCommands.WALK.matches(input)) {
            response = getWalkResponse(input);
        } else if (GameMenuCommands.PRINT_MAP.matches(input)) {
            response = getPrintMapResponse(input);
        } else if (GameMenuCommands.HELP_READING_MAP.matches(input)) {
            response = getMapHelpResponse(input);
        } else if (GameMenuCommands.ENERGY_SHOW.matches(input)) {
            response = getShowEnergyResponse(input);
        } else if (GameMenuCommands.ENERGY_SET_VALUE.matches(input)) {
            response = getSetEnergyValueResponse(input);
        } else if (GameMenuCommands.ENERGY_UNLIMITED.matches(input)) {
            response = getEnergyUnlimitedResponse(input);
        } else if (GameMenuCommands.INVENTORY_SHOW.matches(input)) {
            response = getShowInventoryResponse(input);
        } else if (GameMenuCommands.INVENTORY_TRASH.matches(input)) {
            response = getInventoryTrashResponse(input);
        } else if (GameMenuCommands.TOOLS_EQUIP.matches(input)) {
            response = getToolsEquipResponse(input);
        } else if (GameMenuCommands.TOOLS_SHOW_CURRENT.matches(input)) {
            response = getShowCurrentToolsResponse(input);
        } else if (GameMenuCommands.TOOLS_SHOW_AVAILABLE.matches(input)) {
            response = getShowAvailableToolsResponse(input);
        } else if (GameMenuCommands.TOOLS_UPGRADE.matches(input)) {
            response = getUpgradeToolsResponse(input);
        } else if (GameMenuCommands.TOOLS_USE_DIRECTION.matches(input)) {
            response = getUseToolsResponse(input);
        } else if (GameMenuCommands.CRAFT_INFO.matches(input)) {
            response = getCraftInfoResponse(input);
        } else if (GameMenuCommands.PLANT.matches(input)) {
            response = getPlantingResponse(input);
        } else if (GameMenuCommands.SHOW_PLANT.matches(input)) {
            response = getShowPlantResponse(input);
        } else if (GameMenuCommands.FERTILIZE.matches(input)) {
            response = getFertilizeResponse(input);
        } else if (GameMenuCommands.HOW_MUCH_WATER.matches(input)) {
            response = getHowMuchWaterResponse(input);
        } else if (GameMenuCommands.CRAFTING_SHOW_RECIPES.matches(input)) {
            response = getShowCraftingRecipesResponse(input);
        } else if (GameMenuCommands.CRAFTING_CRAFT.matches(input)) {
            response = getCraftingResponse(input);
        } else if (GameMenuCommands.PLACE_ITEM.matches(input)) {
            response = getPlaceItemResponse(input);
        } else if (GameMenuCommands.CHEAT_ADD_ITEM.matches(input)) {
            response = getAddItemCheatResponse(input);
        } else if (GameMenuCommands.COOKING_REFRIGERATOR_PICK.matches(input)) {
            response = getCookingPickRefrigeratorResponse(input);
        } else if (GameMenuCommands.COOKING_REFRIGERATOR_PUT.matches(input)) {
            response = getCookingRefrigeratorPutResponse(input);
        } else if (GameMenuCommands.COOKING_SHOW_RECIPES.matches(input)) {
            response = getShowCookingRecipesResponse(input);
        } else if (GameMenuCommands.EAT.matches(input)) {
            response = getEatResponse(input);
        } else if (GameMenuCommands.BUILD.matches(input)) {
            response = getBuildResponse(input);
        } else if (GameMenuCommands.BUY_ANIMAL.matches(input)) {
            response = getBuyAnimalResponse(input);
        } else if (GameMenuCommands.PET.matches(input)) {
            response = getPetResponse(input);
        } else if (GameMenuCommands.CHEAT_SET_FRIENDSHIP.matches(input)) {
            response = getChearSetFriendshipResponse(input);
        } else if (GameMenuCommands.ANIMALS.matches(input)) {
            response = getAnimalsResponse(input);
        } else if (GameMenuCommands.SHEPHERD.matches(input)) {
            response = getShepherResponse(input);
        } else if (GameMenuCommands.FEED_HAY.matches(input)) {
            response = getFeedHayResponse(input);
        } else if (GameMenuCommands.PRODUCES.matches(input)) {
            response = getProducesResponse(input);
        } else if (GameMenuCommands.COLLECT_PRODUCE.matches(input)) {
            response = getCollectProduceResponse(input);
        } else if (GameMenuCommands.SELL_ANIMAL.matches(input)) {
            response = getSellAnimalResponse(input);
        } else if (GameMenuCommands.FISHING.matches(input)) {
            response = getFishingResponse(input);
        } else if (GameMenuCommands.ARTISAN_USE.matches(input)) {
            response = getArtisanUseResponse(input);
        } else if (GameMenuCommands.ARTISAN_GET.matches(input)) {
            response = getArtisanGetResponse(input);
        } else if (GameMenuCommands.SHOW_ALL_PRODUCTS.matches(input)) {
            response = getShowAllProductsResponse(input);
        } else if (GameMenuCommands.SHOW_ALL_AVAILABLE_PRODUCTS.matches(input)) {
            response = getShowAvailableProductsResponse(input);
        } else if (GameMenuCommands.PURCHASE.matches(input)) {
            response = getPurchaseResponse(input);
        } else if (GameMenuCommands.CHEAT_ADD_DOLLARS.matches(input)) {
            response = getCheatAddDollarsResponse(input);
        } else if (GameMenuCommands.SELL_PRODUCT.matches(input)) {
            response = getSellProductResponse(input);
        } else if (GameMenuCommands.FRIENDSHIPS.matches(input)) {
            response = getFriendshipResponse(input);
        } else if (GameMenuCommands.TALK_HISTORY.matches(input)) {
            response = getTalkHistoryResponse(input);
        } else if (GameMenuCommands.TALK_HISTORY.matches(input)) {
            response = getTalkHistoryResponse(input);
        } else if (GameMenuCommands.GIFT.matches(input)) {
            response = getGiftResponse(input);
        } else if (GameMenuCommands.GIFT_LIST.matches(input)) {
            response = getGiftListResponse(input);
        } else if (GameMenuCommands.GIFT_HISTORY.matches(input)) {
            response = getGiftHistoryResponse(input);
        } else if (GameMenuCommands.GIFT_RATE.matches(input)) {
            response = getGiftRateResponse(input);
        } else if (GameMenuCommands.HUG.matches(input)) {
            response = getHugResponse(input);
        } else if (GameMenuCommands.FLOWER.matches(input)) {
            response = getFlowerResponse(input);
        } else if (GameMenuCommands.ASK_MARRIAGE.matches(input)) {
            response = getAskMarriageResponse(input);
        } else if (GameMenuCommands.RESPOND_MARRIAGE_ACCEPT.matches(input)) {
            response = getRespondMarriageAcceptResponse(input);
        } else if (GameMenuCommands.RESPOND_MARRIAGE_REJECT.matches(input)) {
            response = getRespondMarriageRejectResponse(input);
        } else if (GameMenuCommands.START_TRADE.matches(input)) {
            response = getStartTradeResponse(input);
        } else if (GameMenuCommands.TRADE.matches(input)) {
            response = getTradeResponse(input);
        } else if (GameMenuCommands.TRADE_LIST.matches(input)) {
            response = getTradeListResponse(input);
        } else if (GameMenuCommands.TRADE_RESPOND_ACCEPT.matches(input)) {
            response = getTradeRespondAcceptResponse(input);
        } else if (GameMenuCommands.TRADE_RESPOND_REJECT.matches(input)) {
            response = getTradeRespondRejectResponse(input);
        } else if (GameMenuCommands.TRADE_HISTORY.matches(input)) {
            response = getTradeHistoryResponse(input);
        } else if (GameMenuCommands.MEET_NPC.matches(input)) {
            response = getMeetNPCResponse(input);
        } else if (GameMenuCommands.GIFT_NPC.matches(input)) {
            response = getGiftNPCResponse(input);
        } else if (GameMenuCommands.FRIENDSHIP_NPC_LIST.matches(input)) {
            response = getFriendshipNPCListResponse(input);
        } else if (GameMenuCommands.QUESTS_LIST.matches(input)) {
            response = getQuestListResponse(input);
        } else if (GameMenuCommands.QUESTS_FINISH.matches(input)) {
            response = getQuestFinishResponse(input);
        } else if (GameMenuCommands.SHOW_MENU.matches(input)) {
            response = getShowMenuResponse(input);
        } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
            response = getExitMenuResponse(input);
        } else if (GameMenuCommands.ENTER_MENU.matches(input)) {
            response = getEnterMenuResponse(input);
        } else {
            response = getInvalidCommand();
        }

        printResponse(response);
    }

    private static Response getBuildResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("buildingName", GameMenuCommands.BUILD.getGroup(input, "buildingName"));
        request.body.put("x", GameMenuCommands.BUILD.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.BUILD.getGroup(input, "y"));
        response = World.handleBuildBuilding(request);
        return response;
    }

    private static Response getEatResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("foodName", GameMenuCommands.EAT.getGroup(input, "foodName"));
        response = InventoryFunctionalities.handleEating(request);
        return response;
    }

    private static Response getShowCookingRecipesResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = Cooking.handleShowCookingRecipes(request);
        return response;
    }

    private static Response getCookingRefrigeratorPutResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("item", GameMenuCommands.COOKING_REFRIGERATOR_PUT.getGroup(input, "item"));
        response = Cooking.handlePutIntoRefrigerator(request);
        return response;
    }

    private static Response getCookingPickRefrigeratorResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("item", GameMenuCommands.COOKING_REFRIGERATOR_PICK.getGroup(input, "item"));
        response = Cooking.handlePickFromRefrigerator(request);
        return response;
    }

    private static Response getAddItemCheatResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("itemName", GameMenuCommands.CHEAT_ADD_ITEM.getGroup(input, "itemName"));
        request.body.put("count", GameMenuCommands.CHEAT_ADD_ITEM.getGroup(input, "count"));
        response = InventoryFunctionalities.handleAddItemCheat(request);
        return response;
    }

    private static Response getPlaceItemResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("itemName", GameMenuCommands.PLACE_ITEM.getGroup(input, "itemName"));
        request.body.put("direction", GameMenuCommands.PLACE_ITEM.getGroup(input, "direction"));
        response = InventoryFunctionalities.handlePlaceItem(request);
        return response;
    }

    private static Response getCraftingResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("itemName", GameMenuCommands.CRAFTING_CRAFT.getGroup(input, "itemName"));
        response = InventoryFunctionalities.handleItemCrafting(request);
        return response;
    }

    private static Response getShowCraftingRecipesResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = InventoryFunctionalities.handleShowCraftingRecipes(request);
        return response;
    }

    private static Response getHowMuchWaterResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = Farming.handleHowMuchWater(request);
        return response;
    }

    private static Response getFertilizeResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("fertilizer", GameMenuCommands.FERTILIZE.getGroup(input, "fertilizer"));
        request.body.put("direction", GameMenuCommands.FERTILIZE.getGroup(input, "direction"));
        response = Farming.handleFertilization(request);
        return response;
    }

    private static Response getShowPlantResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("x", GameMenuCommands.SHOW_PLANT.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.SHOW_PLANT.getGroup(input, "y"));
        response = Farming.handleShowPlant(request);
        return response;
    }

    private static Response getPlantingResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("seed", GameMenuCommands.PLANT.getGroup(input, "seed"));
        request.body.put("direction", GameMenuCommands.PLANT.getGroup(input, "direction"));
        response = Farming.handleSeedPlanting(request);
        return response;
    }

    private static Response getCraftInfoResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("craftName", GameMenuCommands.CRAFT_INFO.getGroup(input, "craftName"));
        response = InventoryFunctionalities.handleCraftInfoQuery(request);
        return response;
    }

    private static Response getUseToolsResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("direction", GameMenuCommands.TOOLS_USE_DIRECTION.getGroup(input, "direction"));
        response = World.handleToolUse(request);
        return response;
    }

    private static Response getUpgradeToolsResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("toolName", GameMenuCommands.TOOLS_UPGRADE.getGroup(input, "toolName"));
        response = InventoryFunctionalities.handleToolUpgrade(request);
        return response;
    }

    private static Response getShowAvailableToolsResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = InventoryFunctionalities.handleShowTools(request);
        return response;
    }

    private static Response getShowCurrentToolsResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = InventoryFunctionalities.handleEquippedToolQuery(request);
        return response;
    }

    private static Response getToolsEquipResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("toolName", GameMenuCommands.TOOLS_EQUIP.getGroup(input, "toolName"));
        response = InventoryFunctionalities.handleToolEquip(request);
        return response;
    }

    private static Response getInventoryTrashResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("itemName", GameMenuCommands.INVENTORY_TRASH.getGroup(input, "itemName"));
        request.body.put("number", GameMenuCommands.INVENTORY_TRASH.getGroup(input, "number"));
        response = InventoryFunctionalities.handleInventoryTrashing(request);
        return response;
    }

    private static Response getShowInventoryResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = InventoryFunctionalities.handleShowInventory(request);
        return response;
    }

    private static Response getEnergyUnlimitedResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = MovementAndMap.handleUnlimitedEnergy(request);
        return response;
    }

    private static Response getSetEnergyValueResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("value", GameMenuCommands.ENERGY_SET_VALUE.getGroup(input, "value"));
        response = MovementAndMap.handleSetEnergy(request);
        return response;
    }

    private static Response getShowEnergyResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = MovementAndMap.handleShowEnergy(request);
        return response;
    }

    private static Response getMapHelpResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = MovementAndMap.handleMapHelp(request);
        return response;
    }

    private static Response getPrintMapResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("x", GameMenuCommands.PRINT_MAP.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.PRINT_MAP.getGroup(input, "y"));
        request.body.put("size", GameMenuCommands.PRINT_MAP.getGroup(input, "size"));
        response = MovementAndMap.handlePrintMap(request);
        return response;
    }

    private static Response getWalkResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("x", GameMenuCommands.WALK.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.WALK.getGroup(input, "y"));
        response = MovementAndMap.handleWalking(request);
        return response;
    }

    private static Response getGreenhouseBuildResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleGreenhouseBuilding(request);
        return response;
    }

    private static Response getWeatherSetResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("Type", GameMenuCommands.CHEAT_WEATHER_SET.getGroup(input, "Type"));
        response = World.handleSetWeatherCheat(request);
        return response;
    }

    private static Response getWeatherForecastResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleWeatherForecastQuery(request);
        return response;
    }

    private static Response getWeatherResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleWeatherQuery(request);
        return response;
    }

    private static Response getCheatThorResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("x", GameMenuCommands.CHEAT_THOR.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.CHEAT_THOR.getGroup(input, "y"));
        response = World.handleCheatThor(request);
        return response;
    }

    private static Response getSeasonResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleSeasonQuery(request);
        return response;
    }

    private static Response getCheatAdvanceDateResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("X", GameMenuCommands.CHEAT_ADVANCE_DATE.getGroup(input, "X"));
        response = World.handleCheatAdvanceDate(request);
        return response;
    }

    private static Response getCheatAdvanceTimeResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("X", GameMenuCommands.CHEAT_ADVANCE_TIME.getGroup(input, "X"));
        response = World.handleCheatAdvanceTime(request);
        return response;
    }

    private static Response getDayOfWeekResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleDayOfWeekQuery(request);
        return response;
    }

    private static Response getDateTimeResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleDatetimeQuery(request);
        return response;
    }

    private static Response getDateResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleDateQuery(request);
        return response;
    }

    private static Response getTimeResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = World.handleTimeQuery(request);
        return response;
    }

    private static Response getNextTurnResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = LoadingSavingTurnHandling.handleNextTurn(request);
        return response;
    }

    private static Response getExitGameResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = LoadingSavingTurnHandling.handleExitGame(request);
        return response;
    }

    private static Response getLoadGameResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = LoadingSavingTurnHandling.handleLoadGame(request);
        return response;
    }

    private static Response getGameMapResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("mapNumber", GameMenuCommands.GAME_MAP.getGroup(input, "mapNumber"));
        response = LoadingSavingTurnHandling.handleMapSelection(request);
        return response;
    }

    private static Response getNewGameResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("users", GameMenuCommands.GAME_NEW.getGroup(input, "users"));
        response = LoadingSavingTurnHandling.handleNewGame(request);
        return response;
    }

    private static Response getBuyAnimalResponse(String input) {
        Request request = new Request(input);
        request.body.put("animal", GameMenuCommands.BUY_ANIMAL.getGroup(input, "animal"));
        request.body.put("name", GameMenuCommands.BUY_ANIMAL.getGroup(input, "name"));
        Response response = LivestockController.handleBuyAnimal(request);
        return response;
    }

    private static Response getPetResponse(String input) {
        Request request = new Request(input);
        request.body.put("name", GameMenuCommands.PET.getGroup(input, "name"));
        Response response = LivestockController.handlePet(request);
        return response;
    }

    private static Response getChearSetFriendshipResponse(String input) {
        Request request = new Request(input);
        request.body.put("animalName", GameMenuCommands.CHEAT_SET_FRIENDSHIP.getGroup(input, "animalName"));
        request.body.put("amount", GameMenuCommands.CHEAT_SET_FRIENDSHIP.getGroup(input, "amount"));
        Response response = LivestockController.handleCheatSetFriendship(request);
        return response;
    }

    private static Response getAnimalsResponse(String input) {
        Request request = new Request(input);
        Response response = LivestockController.handleAnimals(request);
        return response;
    }

    private static Response getShepherResponse(String input) {
        Request request = new Request(input);
        request.body.put("animalName", GameMenuCommands.SHEPHERD.getGroup(input, "animalName"));
        request.body.put("x", GameMenuCommands.SHEPHERD.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.SHEPHERD.getGroup(input, "y"));
        Response response = LivestockController.handleShepherd(request);
        return response;
    }

    private static Response getFeedHayResponse(String input) {
        Request request = new Request(input);
        request.body.put("animalName", GameMenuCommands.FEED_HAY.getGroup(input, "animalName"));
        Response response = LivestockController.handleFeedHay(request);
        return response;
    }

    private static Response getProducesResponse(String input) {
        Request request = new Request(input);
        Response response = LivestockController.handleProduces(request);
        return response;
    }

    private static Response getCollectProduceResponse(String input) {
        Request request = new Request(input);
        request.body.put("name", GameMenuCommands.COLLECT_PRODUCE.getGroup(input, "name"));
        Response response = LivestockController.handleCollectProduce(request);
        return response;
    }

    private static Response getSellAnimalResponse(String input) {
        Request request = new Request(input);
        request.body.put("name", GameMenuCommands.SELL_ANIMAL.getGroup(input, "name"));
        Response response = LivestockController.handleSellAnimal(request);
        return response;
    }

    private static Response getFishingResponse(String input) {
        Request request = new Request(input);
        request.body.put("fishingPole", GameMenuCommands.FISHING.getGroup(input, "fishingPole"));
        Response response = LivestockController.handleFishing(request);
        return response;
    }

    private static Response getArtisanUseResponse(String input) {
        Request request = new Request(input);
        request.body.put("artisanName", GameMenuCommands.ARTISAN_USE.getGroup(input, "artisanName"));
        request.body.put("item1Name", GameMenuCommands.ARTISAN_USE.getGroup(input, "item1Name"));
        Response response = ArtisanController.handleArtisanUse(request);
        return response;
    }

    private static Response getArtisanGetResponse(String input) {
        Request request = new Request(input);
        request.body.put("artisanName", GameMenuCommands.ARTISAN_GET.getGroup(input, "artisanName"));
        Response response = ArtisanController.handleArtisanUse(request);
        return response;
    }

    private static Response getShowAllProductsResponse(String input) {
        Request request = new Request(input);
        Response response = DealingController.handleShowAllProducts(request);
        return response;
    }

    private static Response getShowAvailableProductsResponse(String input) {
        Request request = new Request(input);
        Response response = DealingController.handleShowAvailableProducts(request);
        return response;
    }

    private static Response getPurchaseResponse(String input) {
        Request request = new Request(input);
        request.body.put("productName", GameMenuCommands.PURCHASE.getGroup(input, "productName"));
        request.body.put("count", GameMenuCommands.PURCHASE.getGroup(input, "count"));
        Response response = DealingController.handlePurchase(request);
        return response;
    }

    private static Response getCheatAddDollarsResponse(String input) {
        Request request = new Request(input);
        request.body.put("count", GameMenuCommands.CHEAT_ADD_DOLLARS.getGroup(input, "count"));
        Response response = DealingController.handleCheatAddDollars(request);
        return response;
    }

    private static Response getSellProductResponse(String input) {
        Request request = new Request(input);
        request.body.put("productName", GameMenuCommands.SELL_PRODUCT.getGroup(input, "productName"));
        request.body.put("count", GameMenuCommands.SELL_PRODUCT.getGroup(input, "count"));
        Response response = DealingController.handleSellProduct(request);
        return response;
    }

    private static Response getFriendshipResponse(String input) {
        Request request = new Request(input);
        Response response = FriendshipController.handleFriendship(request);
        return response;
    }

    private static Response getTalkResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.TALK.getGroup(input, "username"));
        request.body.put("message", GameMenuCommands.TALK.getGroup(input, "message"));
        Response response = FriendshipController.handleTalk(request);
        return response;
    }

    private static Response getTalkHistoryResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.TALK_HISTORY.getGroup(input, "username"));
        Response response = FriendshipController.handleTalkHistory(request);
        return response;
    }

    private static Response getGiftResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.GIFT.getGroup(input, "username"));
        request.body.put("item", GameMenuCommands.GIFT.getGroup(input, "item"));
        request.body.put("amount", GameMenuCommands.GIFT.getGroup(input, "amount"));
        Response response = FriendshipController.handleGift(request);
        return response;
    }

    private static Response getGiftListResponse(String input) {
        Request request = new Request(input);
        Response response = FriendshipController.handleGiftList(request);
        return response;
    }

    private static Response getGiftHistoryResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.GIFT_HISTORY.getGroup(input, "username"));
        Response response = FriendshipController.handleGiftHistory(request);
        return response;
    }

    private static Response getGiftRateResponse(String input) {
        Request request = new Request(input);
        request.body.put("giftNumber", GameMenuCommands.GIFT_RATE.getGroup(input, "giftNumber"));
        request.body.put("rate", GameMenuCommands.GIFT_RATE.getGroup(input, "rate"));
        Response response = FriendshipController.handleGiftRate(request);
        return response;
    }

    private static Response getHugResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.HUG.getGroup(input, "username"));
        Response response = FriendshipController.handleHug(request);
        return response;
    }

    private static Response getFlowerResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.FLOWER.getGroup(input, "username"));
        Response response = FriendshipController.handleFlower(request);
        return response;
    }

    private static Response getAskMarriageResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.ASK_MARRIAGE.getGroup(input, "username"));
        request.body.put("ring", GameMenuCommands.ASK_MARRIAGE.getGroup(input, "ring"));
        Response response = FriendshipController.handleAskMarriage(request);
        return response;
    }

    private static Response getRespondMarriageAcceptResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.RESPOND_MARRIAGE_ACCEPT.getGroup(input, "username"));
        Response response = FriendshipController.handleRespondMarriageAccept(request);
        return response;
    }

    private static Response getRespondMarriageRejectResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.RESPOND_MARRIAGE_REJECT.getGroup(input, "username"));
        Response response = FriendshipController.handleRespondMarriageReject(request);
        return response;
    }

    private static Response getStartTradeResponse(String input) {
        Request request = new Request(input);
        Response response = TradingController.handleStartTrade(request);
        return response;
    }

    private static Response getTradeResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.START_TRADE.getGroup(input, "username"));
        request.body.put("item", GameMenuCommands.START_TRADE.getGroup(input, "item"));
        request.body.put("type", GameMenuCommands.START_TRADE.getGroup(input, "type"));
        request.body.put("amount", GameMenuCommands.START_TRADE.getGroup(input, "amount"));
        request.body.put("price", GameMenuCommands.START_TRADE.getGroup(input, "price"));
        request.body.put("targetItem", GameMenuCommands.START_TRADE.getGroup(input, "targetItem"));
        request.body.put("targetAmount", GameMenuCommands.START_TRADE.getGroup(input, "targetAmount"));
        Response response = TradingController.handleStartTrade(request);
        return response;
    }

    private static Response getTradeListResponse(String input) {
        Request request = new Request(input);
        Response response = TradingController.handleTradeList(request);
        return response;
    }

    private static Response getTradeRespondAcceptResponse(String input) {
        Request request = new Request(input);
        request.body.put("id", GameMenuCommands.TRADE_RESPOND_ACCEPT.getGroup(input, "id"));
        Response response = TradingController.handleResponseAccept(request);
        return response;
    }

    private static Response getTradeRespondRejectResponse(String input) {
        Request request = new Request(input);
        request.body.put("id", GameMenuCommands.TRADE_RESPOND_REJECT.getGroup(input, "id"));
        Response response = TradingController.handleResponseReject(request);
        return response;
    }

    private static Response getTradeHistoryResponse(String input) {
        Request request = new Request(input);
        Response response = TradingController.handleResponseHistory(request);
        return response;
    }

    private static Response getMeetNPCResponse(String input) {
        Request request = new Request(input);
        request.body.put("npcName", GameMenuCommands.MEET_NPC.getGroup(input, "npcName"));
        Response response = NPCController.handleMeetNPC(request);
        return response;
    }

    private static Response getGiftNPCResponse(String input) {
        Request request = new Request(input);
        request.body.put("npcName", GameMenuCommands.GIFT_NPC.getGroup(input, "npcName"));
        request.body.put("item", GameMenuCommands.GIFT_NPC.getGroup(input, "item"));
        Response response = NPCController.handleGiftNPC(request);
        return response;
    }

    private static Response getFriendshipNPCListResponse(String input) {
        Request request = new Request(input);
        Response response = NPCController.handleFriendshipNPCList(request);
        return response;
    }

    private static Response getQuestListResponse(String input) {
        Request request = new Request(input);
        Response response = NPCController.handleQuestList(request);
        return response;
    }

    private static Response getQuestFinishResponse(String input) {
        Request request = new Request(input);
        Response response = NPCController.handleQuestFinish(request);
        return response;
    }

    private static Response getEnterMenuResponse(String input) {
        Request request = new Request(input);
        request.body.put("menuName", GameMenuCommands.ENTER_MENU.getGroup(input, "menuName"));
        Response response = Controller.handleEnterMenu(request);
        return response;
    }

    private static Response getExitMenuResponse(String input) {
        Request request = new Request(input);
        Response response = Controller.handleExitMenu(request);
        return response;
    }

    private static Response getShowMenuResponse(String input) {
        Request request = new Request(input);
        Response response = Controller.handleShowMenu(request);
        return response;
    }

}
