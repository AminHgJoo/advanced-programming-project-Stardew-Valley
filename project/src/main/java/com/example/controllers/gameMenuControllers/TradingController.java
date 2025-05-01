package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.types.MenuTypes;
import com.example.models.items.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TradingController extends Controller {
    public static Response handleStartTrade(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        App.setCurrMenuType(MenuTypes.TradeMenu);
        StringBuilder output = new StringBuilder();
        output.append("welcome to the Trading Menu");
        output.append("\n");
        output.append("new trading requests:\n");
        for (Trade trade : game.getPlayerUndecidedTradeRequestsReceived(player)) {
            if (!trade.shown) {
                output.append(trade.toString()).append("\n");
                trade.shown = true;
            }
        }
        GameRepository.saveGame(game);
        return new Response(true, output.toString());
    }

    public static Response handleTradeMoney(Request request) {
        return null;
    }

    public static Response handleTradeItem(Request request) {
        return null;
    }

    public static Response handleTradeList(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        ArrayList<Trade> trades = game.getPlayerUndecidedTradeRequestsReceived(player);
        if (trades.isEmpty()) {
            GameRepository.saveGame(game);
            return new Response(false, "no undecided trade requests found");
        }
        StringBuilder output = new StringBuilder();
        output.append("your trading requests:\n");
        for (Trade trade : trades) {
            output.append(trade.toString()).append("\n");
        }
        GameRepository.saveGame(game);
        return new Response(true, output.toString());
    }

    public static Response handleResponseAccept(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player secondPlayer = game.getCurrentPlayer();
        Backpack secondPlayerBackpack = secondPlayer.getInventory();
        int id = Integer.parseInt(request.body.get("id"));
        Trade trade = game.getTradeById(id);
        if (trade == null) {
            GameRepository.saveGame(game);
            return new Response(false, "no trade found");
        }
        Player firstPlayer = trade.firstPlayer;
        Backpack firstPlayerBackpack = firstPlayer.getInventory();
        Slot tradeItemSlotInFirstPlayerBackpack = firstPlayerBackpack.getSlotByItemName(trade.tradeItem.getName());
        if (tradeItemSlotInFirstPlayerBackpack == null || tradeItemSlotInFirstPlayerBackpack.getCount() < trade.itemAmount) {
            GameRepository.saveGame(game);
            return new Response(false, firstPlayer.getUser().getUsername() + " doesn't have enough " + trade.tradeItem.getName());
        }
        if (trade.tradeType.equals("offer")) {
            return acceptOfferHandle(trade, firstPlayer, game, tradeItemSlotInFirstPlayerBackpack, secondPlayerBackpack, firstPlayerBackpack, secondPlayer);
        }
        return acceptRequestHandle(trade, secondPlayerBackpack, game, firstPlayerBackpack, firstPlayer, secondPlayer, tradeItemSlotInFirstPlayerBackpack);
    }

    private static @NotNull Response acceptRequestHandle(Trade trade, Backpack secondPlayerBackpack, Game game, Backpack firstPlayerBackpack, Player firstPlayer, Player secondPlayer, Slot tradeItemSlotInFirstPlayerBackpack) {
        Item targetItem = trade.targetItem;
        int targetAmount = trade.targetAmount;
        Slot targetItemSlotInSecondPlayerBackpack = secondPlayerBackpack.getSlotByItemName(targetItem.getName());
        if (targetItemSlotInSecondPlayerBackpack == null || targetItemSlotInSecondPlayerBackpack.getCount() < targetAmount) {
            GameRepository.saveGame(game);
            return new Response(false, "you don't have enough amount of " + targetItem.getName());
        }
        Slot targetItemSlotInFirstPlayerBackpack = firstPlayerBackpack.getSlotByItemName(targetItem.getName());
        if (targetItemSlotInFirstPlayerBackpack == null && (firstPlayerBackpack.getSlots().size() == firstPlayerBackpack.getType().getMaxCapacity())) {
            GameRepository.saveGame(game);
            return new Response(false, firstPlayer.getUser().getUsername() + "'s backpack is full");
        }
        Slot tradeItemSlotInSecondPlayerBackpack = secondPlayerBackpack.getSlotByItemName(trade.tradeItem.getName());
        if(tradeItemSlotInSecondPlayerBackpack == null && secondPlayerBackpack.getSlots().size() == secondPlayerBackpack.getType().getMaxCapacity()) {
            GameRepository.saveGame(game);
            return new Response(false, secondPlayer.getUser().getUsername() + "'s backpack is full");
        }
        if(tradeItemSlotInSecondPlayerBackpack  == null){
            Slot newSlot = new Slot(trade.tradeItem, trade.itemAmount);
            secondPlayerBackpack.addSlot(newSlot);
        }
        else{
            tradeItemSlotInSecondPlayerBackpack.setCount(tradeItemSlotInSecondPlayerBackpack.getCount() + targetAmount);
        }
        tradeItemSlotInFirstPlayerBackpack.setCount(tradeItemSlotInFirstPlayerBackpack.getCount() - targetAmount);
        if(tradeItemSlotInFirstPlayerBackpack.getCount() <= 0){
            firstPlayerBackpack.removeSlot(tradeItemSlotInFirstPlayerBackpack);
        }
        if(targetItemSlotInFirstPlayerBackpack == null){
            Slot newSlot = new Slot(trade.targetItem, targetAmount);
            firstPlayerBackpack.addSlot(newSlot);
        }
        else{
            targetItemSlotInFirstPlayerBackpack.setCount(targetItemSlotInFirstPlayerBackpack.getCount() + targetAmount);
        }
        targetItemSlotInSecondPlayerBackpack.setCount(targetItemSlotInSecondPlayerBackpack.getCount() - targetAmount);
        if(targetItemSlotInSecondPlayerBackpack.getCount() <= 0){
            secondPlayerBackpack.removeSlot(targetItemSlotInSecondPlayerBackpack);
        }
        secondPlayer.addXpToFriendShip(10, firstPlayer);
        trade.tradeResult = 1;
        GameRepository.saveGame(game);
        return new Response(true, "trade successful");
    }

    private static @NotNull Response acceptOfferHandle(Trade trade, Player firstPlayer, Game game, Slot tradeItemSlotInFirstPlayerBackpack, Backpack secondPlayerBackpack, Backpack firstPlayerBackpack, Player secondPlayer) {
        int price = trade.tradePrice;
        if (firstPlayer.getMoney() < price) {
            GameRepository.saveGame(game);
            return new Response(false, "you have not enough money");
        }
        Slot itemSlotInSecondPlayerBackpack = secondPlayerBackpack.getSlotByItemName(trade.tradeItem.getName());
        if (itemSlotInSecondPlayerBackpack == null && secondPlayerBackpack.getSlots().size() == secondPlayerBackpack.getType().getMaxCapacity()) {
            GameRepository.saveGame(game);
            return new Response(false, "your backpack is full");
        }
        if (itemSlotInSecondPlayerBackpack == null) {
            Slot newSlot = new Slot(trade.tradeItem, trade.itemAmount);
            secondPlayerBackpack.addSlot(newSlot);
        }
        else{
            itemSlotInSecondPlayerBackpack.setCount(itemSlotInSecondPlayerBackpack.getCount() + trade.itemAmount);
        }
        tradeItemSlotInFirstPlayerBackpack.setCount(tradeItemSlotInFirstPlayerBackpack.getCount() - trade.itemAmount);
        if (tradeItemSlotInFirstPlayerBackpack.getCount() <= 0) {
            firstPlayerBackpack.removeSlot(tradeItemSlotInFirstPlayerBackpack);
        }
        firstPlayer.setMoney(firstPlayer.getMoney() + price);
        secondPlayer.setMoney(secondPlayer.getMoney() - price);
        secondPlayer.addXpToFriendShip(10, firstPlayer);
        trade.tradeResult = 1;
        GameRepository.saveGame(game);
        return new Response(true, "trade successful");
    }

    public static Response handleResponseReject(Request request) {
        return null;
    }

    public static Response handleResponseHistory(Request request) {
        return null;
    }

    public static Response handleTradeError(Request request) {
        return new Response(false, "you cannot choose both trading methods");
    }

    public static Response leaveTradingMenu(Request request) {
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "leaving trading menu...");
    }
}
