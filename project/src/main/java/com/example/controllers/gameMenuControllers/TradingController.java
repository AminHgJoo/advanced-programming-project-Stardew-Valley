package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.User;
import com.example.models.enums.types.MenuTypes;

public class TradingController extends Controller {
    public static Response handleStartTrade(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        App.setCurrMenuType(MenuTypes.TradeMenu);
        StringBuilder output = new StringBuilder();
        output.append("welcome to the Trading Menu");

        return new Response(true, "redirecting to trading menu");
    }

    public static Response handleTradeMoney(Request request) {
        return null;
    }

    public static Response handleTradeItem(Request request) {return null;}

    public static Response handleTradeList(Request request) {
        return null;
    }

    public static Response handleResponseAccept(Request request) {
        return null;
    }

    public static Response handleResponseReject(Request request) {
        return null;
    }

    public static Response handleResponseHistory(Request request) {
        return null;
    }

    public static Response handleTradeError(Request request) {
        return null;
    }

    public static Response leaveTradingMenu(Request request) {
        App.setCurrMenuType(MenuTypes.GameMenu);
        return new Response(true, "leaving trading menu...");
    }
}
