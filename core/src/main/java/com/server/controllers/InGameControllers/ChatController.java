package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.*;
import com.common.models.enums.Quality;
import com.common.models.enums.commands.GameMenuCommands;
import com.common.models.enums.recipes.CookingRecipes;
import com.common.models.enums.recipes.CraftingRecipes;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.worldEnums.Weather;
import com.common.utils.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;
import org.intellij.lang.annotations.Language;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatController extends ServerController {
    public ChatController(GameServer gs) {
        super(gs);
    }

    public void fetchMessages(Context ctx, GameServer gs) {
        try {
            GameData gameData = gs.getGame();
            ArrayList<ChatMessage> messages = gameData.chatMessages;
            String arrayJson = new Gson().toJson(messages);
            ctx.json(Response.OK.setMessage("").setBody(arrayJson));
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    public void addMessage(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String message = (String) body.get("message");
            String sender = (String) body.get("sender");
            String recipient = (String) body.get("recipient");
            Boolean isPrivate = (Boolean) body.get("isPrivate");

            ChatMessage msg = new ChatMessage(message, sender, recipient, isPrivate);
            gs.getGame().chatMessages.add(msg);
            ctx.json(Response.OK.setMessage("Message Added Successfully").setBody(""));

            HashMap<String, String> multicastMsg = new HashMap<>();
            multicastMsg.put("type", "MESSAGE_ADDED");
            multicastMsg.put("message", new GsonBuilder().setPrettyPrinting().create().toJson(msg));
            ArrayList<String> multicastRecipients = new ArrayList<>();

            for (Player player : gs.getGame().getPlayers()) {
                String username = player.getUser().getUsername();

                if (!username.equals(sender)) {
                    multicastRecipients.add(username);
                }
            }

            gs.multicast(multicastMsg, multicastRecipients);

        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    public void parseCheat(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String command = (String) body.get("command");
            if (GameMenuCommands.CHEAT_ADD_DOLLARS.matches(command)) {
                addDollars(ctx, command, gs);
            } else if (GameMenuCommands.CHEAT_ADVANCE_TIME.matches(command)) {
                advanceTime(ctx, command, gs);
            } else if (GameMenuCommands.CHEAT_ADVANCE_DATE.matches(command)) {
                advanceDate(ctx, command, gs);
            } else if (GameMenuCommands.ENERGY_SET_VALUE.matches(command)) {
                setEnergy(ctx, gs, command);
            } else if (GameMenuCommands.CHEAT_THOR.matches(command)) {
                thor(ctx, command, gs);
            } else if (GameMenuCommands.CHEAT_WEATHER_SET.matches(command)) {
                setWeather(ctx, command, gs);
            } else if (GameMenuCommands.CHEAT_ADD_ITEM.matches(command)) {
                addItem(ctx, command, gs);
            } else if (GameMenuCommands.CHEAT_SET_FRIENDSHIP.matches(command)) {
                setFriendship(ctx, command, gs);
            } else if (GameMenuCommands.CHEAT_ADD_SKILL_XP.matches(command)) {
                skillXp(ctx, command, gs);
            } else if (GameMenuCommands.CHEAT_UNLOCK_RECIPE.matches(command)) {
                unlockRecipe(ctx, command, gs);
            } else {
                ctx.json(Response.NOT_FOUND.setMessage("Cheat Not Found"));
            }
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    public void unlockRecipe(Context ctx, String command, GameServer gs) {
        String recipeName = GameMenuCommands.CHEAT_UNLOCK_RECIPE.getGroup(command, "name");
        String id = ctx.attribute("id");

        GameData gameData = gs.getGame();
        Player player = getPlayer(gameData, id);

        if (player == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("Player Not Found"));
            return;
        }

        CookingRecipes targetRecipe = null;

        for (CookingRecipes recipe : CookingRecipes.values()) {
            if (recipe.name.compareToIgnoreCase(recipeName) == 0) {
                targetRecipe = recipe;
            }
        }

        if (targetRecipe != null) {
            if (!player.getUnlockedCookingRecipes().contains(targetRecipe)) {
                player.getUnlockedCookingRecipes().add(targetRecipe);
            }
            String json = GameGSON.gson.toJson(player);
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            gs.broadcast(msg);
            ctx.json(Response.OK.setMessage("player").setBody(json));
            return;
        }

        CraftingRecipes targetRecipeCrafting = null;

        for (CraftingRecipes recipe : CraftingRecipes.values()) {
            if (recipe.name.compareToIgnoreCase(recipeName) == 0) {
                targetRecipeCrafting = recipe;
            }
        }

        if (targetRecipeCrafting != null) {
            if (!player.getUnlockedCraftingRecipes().contains(targetRecipeCrafting)) {
                player.getUnlockedCraftingRecipes().add(targetRecipeCrafting);
            }
            String json = GameGSON.gson.toJson(player);
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "PLAYER_UPDATED");
            msg.put("player_user_id", id);
            gs.broadcast(msg);
            ctx.json(Response.OK.setMessage("player").setBody(json));
            return;
        }

        ctx.json(Response.BAD_REQUEST.setMessage("Recipe Not Found"));
    }

    public void setEnergy(Context ctx, GameServer gs, String command) {
        int energy = Integer.parseInt(GameMenuCommands.ENERGY_SET_VALUE.getGroup(command, "value"));
        String id = ctx.attribute("id");
        GameData gameData = gs.getGame();
        Player player = gameData.findPlayerByUserId(id);
        player.setEnergy(player.getEnergy() + energy);
        String json = GameGSON.gson.toJson(player);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "PLAYER_UPDATED");
        msg.put("player_user_id", id);
        gs.broadcast(msg);
        ctx.json(Response.OK.setMessage("player").setBody(json));
    }

    // Cheat Controllers
    public void addDollars(Context ctx, String command, GameServer gs) {
        int count = Integer.parseInt(GameMenuCommands.CHEAT_ADD_DOLLARS.getGroup(command, "count"));
        String id = ctx.attribute("id");
        GameData gameData = gs.getGame();
        Player player = gameData.findPlayerByUserId(id);
        player.setMoney(player.getMoney(gameData) + count, gameData);
        String playerJson = GameGSON.gson.toJson(player);
        ctx.json(Response.OK.setMessage("player").setBody(playerJson));

        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "PLAYER_UPDATED");
        msg.put("player", playerJson);
        gs.broadcast(msg);
    }

    public void advanceTime(Context ctx, String command, GameServer gs) {
        int amountOfHours = Integer.parseInt(GameMenuCommands.CHEAT_ADVANCE_TIME.getGroup(command, "X"));
        GameData currentGameData = gs.getGame();
        String id = ctx.attribute("id");
        Player player = currentGameData.findPlayerByUserId(id);
        LocalDateTime currentDateTime = currentGameData.getDate();
        LocalDateTime nextDateTime;
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
        currentGameData.setDate(nextDateTime);
        currentGameData.checkSeasonChange();
        if (check) {
            currentGameData.newDayBackgroundChecks();
        }
        currentGameData.handleArtisanUse();
        String gameJson = GameGSON.gson.toJson(currentGameData);
        ctx.json(Response.OK.setMessage("game").setBody(gameJson));
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "GAME_UPDATED");
        msg.put("game", gameJson);
        gs.broadcast(msg);
    }

    public void advanceDate(Context ctx, String command, GameServer gs) {
        int amountOfDays = Integer.parseInt(GameMenuCommands.CHEAT_ADVANCE_DATE.getGroup(command, "X"));
        GameData currentGameData = gs.getGame();
        String id = ctx.attribute("id");
        Player player = currentGameData.findPlayerByUserId(id);
        LocalDateTime currentDateTime = currentGameData.getDate();
        LocalDateTime nextDateTime;
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
        currentGameData.setDate(nextDateTime);
        if (check) {
            currentGameData.newDayBackgroundChecks();
        }
        currentGameData.checkSeasonChange();
        String gameJson = GameGSON.gson.toJson(currentGameData);
        ctx.json(Response.OK.setMessage("game").setBody(gameJson));
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "GAME_UPDATED");
        msg.put("game", gameJson);
        gs.broadcast(msg);
    }

    public void thor(Context ctx, String command, GameServer gs) {
        ctx.json(Response.OK.setMessage("Cheat done successfully"));
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "THOR");
        gs.broadcast(msg);
    }

    public void setWeather(Context ctx, String command, GameServer gs) {
        String type = GameMenuCommands.CHEAT_WEATHER_SET.getGroup(command, "Type");
        Weather weather = Weather.getWeatherByName(type);
        GameData gameData = gs.getGame();
        if (weather == null) {
            ctx.json(Response.BAD_REQUEST.setMessage("null weather"));
            return;
        } else {
            gameData.setWeatherToday(weather);
        }
        String gameJson = GameGSON.gson.toJson(gameData);
        ctx.json(Response.OK.setMessage("game").setBody(gameJson));
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "GAME_UPDATED");
        msg.put("game", gameJson);
        gs.broadcast(msg);
    }

    public void addItem(Context ctx, String command, GameServer gs) {
        int count = Integer.parseInt(GameMenuCommands.CHEAT_ADD_ITEM.getGroup(command, "count"));
        String itemName = GameMenuCommands.CHEAT_ADD_ITEM.getGroup(command, "itemName");

        HashMap<String, ItemType> allItemsList = App.getAllItemTypes();

        @Language("Regexp")
        String toolsRegex = "(?<quality>Default|Copper|Silver|Gold|Iridium|Training|Bamboo|Fiberglass) " +
            "(?<toolName>Axe|Hoe|Pickaxe|Fishing Rod|Scythe)";
        Pattern pattern = Pattern.compile(toolsRegex);
        Matcher matcher = pattern.matcher(itemName);

        boolean matchFound = matcher.matches();

        GameData gameData = gs.getGame();
        String id = ctx.attribute("id");
        Player player = gameData.findPlayerByUserId(id);
        Backpack backpack = player.getInventory();

        if (!allItemsList.containsKey(itemName) && !matchFound) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid item name").setBody("Invalid item name"));
            return;
        }

        if (backpack.getType().getMaxCapacity() == backpack.getSlots().size()) {
            ctx.json(Response.BAD_REQUEST.setMessage("You cannot add an item to the backpack"));
            return;
        }

        Slot slot = backpack.getSlotByItemName(itemName);

        Quality quality = Quality.DEFAULT;

        if (matchFound) {
            quality = Quality.getQualityByName(matcher.group("quality"));
        }

        if (slot == null) {
            Slot toBeAddedSlot = allItemsList.get(matchFound ? matcher.group("toolName") : itemName).createAmountOfItem(count, quality);
            backpack.getSlots().add(toBeAddedSlot);
        } else {
            slot.setCount(Math.min(slot.getCount() + count, slot.getItem().getMaxStackSize()));
        }

        String playerJson = GameGSON.gson.toJson(player);
        ctx.json(Response.OK.setMessage("player").setBody(playerJson));
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "PLAYER_UPDATED");
        msg.put("player_user_id", id);
        msg.put("player", playerJson);
        gs.broadcast(msg);
    }

    public void setFriendship(Context ctx, String command, GameServer gs) {

    }

    public void skillXp(Context ctx, String command, GameServer gs) {
        String skill = GameMenuCommands.CHEAT_ADD_SKILL_XP.getGroup(command, "skill");
        String amount = GameMenuCommands.CHEAT_ADD_SKILL_XP.getGroup(command, "amount");
        GameData gameData = gs.getGame();
        String id = ctx.attribute("id");
        Player player = gameData.findPlayerByUserId(id);
        if (skill.compareToIgnoreCase("farming") == 0) {
            player.getUnbuffedFarmingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + Integer.parseInt(amount));
        } else if (skill.compareToIgnoreCase("foraging") == 0) {
            player.getUnbuffedForagingSkill().setXp(player.getUnbuffedFarmingSkill().getXp() + Integer.parseInt(amount));
        } else if (skill.compareToIgnoreCase("fishing") == 0) {
            player.getUnbuffedFishingSkill().setXp(player.getUnbuffedFishingSkill().getXp() + Integer.parseInt(amount));
        } else if (skill.compareToIgnoreCase("mining") == 0) {
            player.getUnbuffedMiningSkill().setXp(player.getUnbuffedMiningSkill().getXp() + Integer.parseInt(amount));
        } else {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid skill").setBody("Invalid skill"));
            return;
        }
        String playerJson = GameGSON.gson.toJson(player);
        ctx.json(Response.OK.setMessage("player").setBody(playerJson));
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "PLAYER_UPDATED");
        msg.put("player_user_id", id);
        msg.put("player", playerJson);
        gs.broadcast(msg);
    }
}
