package com.server.views.gameViews;

import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.commands.GameMenuCommands;
import com.server.views.Menu;
import com.server.controllers_old.gameMenuControllers.TradingController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TradeMenuView implements Menu {
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

    private static Response getTradeRespondAcceptResponse(String input) {
        Request request = new Request(input);
        request.body.put("id", GameMenuCommands.TRADE_RESPOND_ACCEPT.getGroup(input, "id"));
        Response response = TradingController.handleResponseAccept(request);
        return response;
    }

    private static Response getTradeListResponse(String input) {
        Request request = new Request(input);
        Response response = TradingController.handleTradeList(request);
        return response;
    }

    private static @Nullable Response tradeError(String input) {
        Request request = new Request(input);
        Response response = TradingController.handleTradeError(request);
        return response;
    }

    private static @NotNull Response leaveMenu(String input) {
        Response response;
        Request request = new Request(input);
        response = TradingController.leaveTradingMenu(request);
        return response;
    }

    private static Response getTradeMoneyResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.TRADE_MONEY.getGroup(input, "username"));
        request.body.put("item", GameMenuCommands.TRADE_MONEY.getGroup(input, "item"));
        request.body.put("type", GameMenuCommands.TRADE_MONEY.getGroup(input, "type"));
        request.body.put("amount", GameMenuCommands.TRADE_MONEY.getGroup(input, "amount"));
        request.body.put("price", GameMenuCommands.TRADE_MONEY.getGroup(input, "price"));
        Response response = TradingController.handleTradeMoney(request);
        return response;
    }

    private static Response getTradeItemResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", GameMenuCommands.TRADE_ITEM.getGroup(input, "username"));
        request.body.put("item", GameMenuCommands.TRADE_ITEM.getGroup(input, "item"));
        request.body.put("type", GameMenuCommands.TRADE_ITEM.getGroup(input, "type"));
        request.body.put("amount", GameMenuCommands.TRADE_ITEM.getGroup(input, "amount"));
        request.body.put("targetItem", GameMenuCommands.TRADE_ITEM.getGroup(input, "targetItem"));
        request.body.put("targetAmount", GameMenuCommands.TRADE_ITEM.getGroup(input, "targetAmount"));
        Response response = TradingController.handleTradeItem(request);
        return response;
    }

    public void handleMenu(String input) {
        Response response = null;
        if (GameMenuCommands.EXIT_MENU.matches(input)) {
            response = leaveMenu(input);
        } else if (GameMenuCommands.TRADE_MONEY.matches(input)) {
            response = getTradeMoneyResponse(input);
        } else if (GameMenuCommands.TRADE_ITEM.matches(input)) {
            response = getTradeItemResponse(input);
        } else if (GameMenuCommands.TRADE_ERROR.matches(input)) {
            response = tradeError(input);
        } else if (GameMenuCommands.TRADE_LIST.matches(input)) {
            response = getTradeListResponse(input);
        } else if (GameMenuCommands.TRADE_RESPOND_ACCEPT.matches(input)) {
            response = getTradeRespondAcceptResponse(input);
        } else if (GameMenuCommands.TRADE_RESPOND_REJECT.matches(input)) {
            response = getTradeRespondRejectResponse(input);
        } else if (GameMenuCommands.TRADE_HISTORY.matches(input)) {
            response = getTradeHistoryResponse(input);
        } else {
            response = getInvalidCommand();
        }
        printResponse(response);
    }

}
