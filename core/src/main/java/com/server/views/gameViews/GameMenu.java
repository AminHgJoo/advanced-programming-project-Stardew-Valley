package com.server.views.gameViews;

import com.common.models.App;
import com.common.models.GameData;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.commands.GameMenuCommands;
import com.server.controllers_old.Controller;
import com.server.controllers_old.gameMenuControllers.*;
import com.server.views.Menu;
import org.jetbrains.annotations.NotNull;

public class GameMenu implements Menu {

    private static Response getWalkAddCoordsResponse(String input) {
        return null;
    }

    private static Response getShowCoordsResponse(String input) {
        Request request = new Request(input);
        return MovementAndMap.showCoords(request);
    }

    private static @NotNull Response showMoney(String input) {
        Response response;
        Request request = new Request(input);
        response = World.showMoney(request);
        return response;
    }

    private static @NotNull Response cheatEmptyRectangle(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("x", GameMenuCommands.CHEAT_EMPTY_RECTANGLE.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.CHEAT_EMPTY_RECTANGLE.getGroup(input, "y"));
        response = MovementAndMap.cheatEmptyRectangle(request);
        return response;
    }

    private static @NotNull Response cheatAddSkillXp(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("skill", GameMenuCommands.CHEAT_ADD_SKILL_XP.getGroup(input, "skill"));
        request.body.put("amount", GameMenuCommands.CHEAT_ADD_SKILL_XP.getGroup(input, "amount"));
        response = InventoryFunctionalities.cheatAddSkillXP(request);
        return response;
    }

    private static @NotNull Response goToPartnerFarm(String input) {
        Response response;
        Request request = new Request(input);
        response = MovementAndMap.goToPartnerFarm(request);
        return response;
    }

    private static @NotNull Response walkHome(String input) {
        Response response;
        Request request = new Request(input);
        response = MovementAndMap.walkHome(request);
        return response;
    }

    private static @NotNull Response goToVillage(String input) {
        Response response;
        Request request = new Request(input);
        response = MovementAndMap.goToVillage(request);
        return response;
    }

    private static Response getEnterPlayerHomeResponse(String input) {
        Request request = new Request(input);
        Response response = Cooking.handleEnterPlayerHome(request);
        return response;
    }

    private static Response goToStore(String input) {
        Request request = new Request(input);
        request.body.put("storeName", GameMenuCommands.GO_TO_STORE.getGroup(input, "storeName"));
        Response response = DealingController.handleGoToStore(request);
        return response;
    }

    private static Response getShowFullFarmResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = MovementAndMap.showFullFarm(request);
        return response;
    }

    private static Response getShowFarmResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("x", GameMenuCommands.PRINT_MAP.getGroup(input, "x"));
        request.body.put("y", GameMenuCommands.PRINT_MAP.getGroup(input, "y"));
        request.body.put("size", GameMenuCommands.PRINT_MAP.getGroup(input, "size"));
        response = MovementAndMap.showFarm(request);
        return response;
    }

    private static Response getForceDeleteGameResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = LoadingSavingTurnHandling.handleForceDeleteGame(request);
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

    private static Response getPlantingTree(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("seed", GameMenuCommands.PLANT.getGroup(input, "seed"));
        request.body.put("direction", GameMenuCommands.PLANT.getGroup(input, "direction"));
        response = Farming.handleTreePlanting(request);
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

    private static Response getWalkResponse(String input) {
        return null;
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
        return null;
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

    private static Response getPetResponse(String input) {
        Request request = new Request(input);
        request.body.put("name", GameMenuCommands.PET.getGroup(input, "name"));
        Response response = LivestockController.handlePet(request);
        return response;
    }

    private static Response getCheatSetFriendshipResponse(String input) {
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

    private static Response getShepherdResponse(String input) {
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
        Response response = ArtisanController.handleArtisanGet(request);
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
        request.body.put("flowerName", GameMenuCommands.FLOWER.getGroup(input, "flowerName"));
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

    private static Response getMeetNPCResponse(String input) {
        Request request = new Request(input);
        request.body.put("npcName", GameMenuCommands.MEET_NPC.getGroup(input, "npcName"));
        Response response = NPCController.handleMeetNPC(request);
        return response;
    }

    private static Response getTalkNpcResponse(String input) {
        Request request = new Request(input);
        request.body.put("npcName", GameMenuCommands.TALK_NPC.getGroup(input, "npcName"));
        request.body.put("message", GameMenuCommands.TALK_NPC.getGroup(input, "message"));
        Response response = NPCController.handleTalkNPC(request);
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
        request.body.put("npcName", GameMenuCommands.QUESTS_LIST.getGroup(input, "npcName"));
        Response response = NPCController.handleQuestList(request);
        return response;
    }

    private static Response getQuestFinishResponse(String input) {
        Request request = new Request(input);
        request.body.put("index", GameMenuCommands.QUESTS_FINISH.getGroup(input, "index"));
        request.body.put("npcName", GameMenuCommands.QUESTS_FINISH.getGroup(input, "npcName"));
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

    public void handleMenu(String input) {
        Response response = null;
        if (App.getLoggedInUser().getCurrentGame() == null
            || !App.getLoggedInUser().getCurrentGame().isGameOngoing()) {

            if (LoadingSavingTurnHandling.isWaitingForChoosingMap) {
                if (GameMenuCommands.GAME_MAP.matches(input)) {
                    response = getGameMapResponse(input);
                } else {
                    response = getInvalidCommand();
                }
            } else if (GameMenuCommands.GAME_NEW.matches(input)) {
                response = getNewGameResponse(input);
            } else if (GameMenuCommands.LOAD_GAME.matches(input)) {
                response = getLoadGameResponse(input);
            } else if (GameMenuCommands.SHOW_MENU.matches(input)) {
                response = getShowMenuResponse(input);
            } else if (GameMenuCommands.EXIT_MENU.matches(input)) {
                response = getExitMenuResponse(input);
            } else if (GameMenuCommands.ENTER_MENU.matches(input)) {
                response = getEnterMenuResponse(input);
            } else {
                response = getInvalidCommand();
            }
        } else {
            GameData gameData = App.getLoggedInUser().getCurrentGame();
            if (GameMenuCommands.SHOW_FARM.matches(input)) {
                response = getShowFullFarmResponse(input);
            } else if (GameMenuCommands.SHOW_COORDS.matches(input)) {
                response = getShowCoordsResponse(input);
            } else if (GameMenuCommands.EXIT_GAME.matches(input)) {
                response = getExitGameResponse(input);
            } else if (GameMenuCommands.NEXT_TURN.matches(input)) {
                response = getNextTurnResponse(input);
            } else if (GameMenuCommands.FORCE_DELETE_GAME.matches(input)) {
                response = getForceDeleteGameResponse(input);
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
                response = getShowFarmResponse(input);
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
            } else if (GameMenuCommands.TOOLS_USE_DIRECTION.matches(input)) {
                response = getUseToolsResponse(input);
            } else if (GameMenuCommands.CRAFT_INFO.matches(input)) {
                response = getCraftInfoResponse(input);
            } else if (GameMenuCommands.PLANT.matches(input)) {
                response = getPlantingResponse(input);
            } else if (GameMenuCommands.PLANT_TREE.matches(input)) {
                response = getPlantingTree(input);
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
            } else if (GameMenuCommands.ENTER_HOME.matches(input)) {
                response = getEnterPlayerHomeResponse(input);
            } else if (GameMenuCommands.COOKING_SHOW_RECIPES.matches(input)) {
                response = getShowCookingRecipesResponse(input);
            } else if (GameMenuCommands.EAT.matches(input)) {
                response = getEatResponse(input);
            } else if (GameMenuCommands.PET.matches(input)) {
                response = getPetResponse(input);
            } else if (GameMenuCommands.CHEAT_SET_FRIENDSHIP.matches(input)) {
                response = getCheatSetFriendshipResponse(input);
            } else if (GameMenuCommands.ANIMALS.matches(input)) {
                response = getAnimalsResponse(input);
            } else if (GameMenuCommands.SHEPHERD.matches(input)) {
                response = getShepherdResponse(input);
            } else if (GameMenuCommands.FEED_HAY.matches(input)) {
                response = getFeedHayResponse(input);
            } else if (GameMenuCommands.PRODUCES.matches(input)) {
                response = getProducesResponse(input);
            } else if (GameMenuCommands.COLLECT_PRODUCE.matches(input)) {
                response = getCollectProduceResponse(input);
            } else if (GameMenuCommands.SELL_ANIMAL.matches(input)) {
                response = getSellAnimalResponse(input);
            } else if (GameMenuCommands.ARTISAN_USE.matches(input)) {
                response = getArtisanUseResponse(input);
            } else if (GameMenuCommands.ARTISAN_GET.matches(input)) {
                response = getArtisanGetResponse(input);
            } else if (GameMenuCommands.CHEAT_ADD_DOLLARS.matches(input)) {
                response = getCheatAddDollarsResponse(input);
            } else if (GameMenuCommands.SELL_PRODUCT.matches(input)) {
                response = getSellProductResponse(input);
            } else if (GameMenuCommands.FRIENDSHIPS.matches(input)) {
                response = getFriendshipResponse(input);
            } else if (GameMenuCommands.TALK.matches(input)) {
                response = getTalkResponse(input);
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
            } else if (GameMenuCommands.MEET_NPC.matches(input)) {
                response = getMeetNPCResponse(input);
            } else if (GameMenuCommands.TALK_NPC.matches(input)) {
                response = getTalkNpcResponse(input);
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
            } else if (GameMenuCommands.GO_TO_STORE.matches(input)) {
                response = goToStore(input);
            } else if (GameMenuCommands.GO_TO_VILLAGE.matches(input)) {
                response = goToVillage(input);
            } else if (GameMenuCommands.WALK_HOME.matches(input)) {
                response = walkHome(input);
            } else if (GameMenuCommands.GO_TO_PARTNER_FARM.matches(input)) {
                response = goToPartnerFarm(input);
            } else if (GameMenuCommands.CHEAT_ADD_SKILL_XP.matches(input)) {
                response = cheatAddSkillXp(input);
            } else if (GameMenuCommands.CHEAT_EMPTY_RECTANGLE.matches(input)) {
                response = cheatEmptyRectangle(input);
            } else if (GameMenuCommands.SHOW_MONEY.matches(input)) {
                response = showMoney(input);
            } else if (GameMenuCommands.WALK_ADD_COORDS.matches(input)) {
                response = getWalkAddCoordsResponse(input);
            } else {
                response = getInvalidCommand();
            }
        }

        printResponse(response);
    }

}
