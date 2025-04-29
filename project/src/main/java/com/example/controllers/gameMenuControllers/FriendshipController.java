package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;

import java.util.ArrayList;
import java.util.List;

// TODO handle friendship xp in thread

public class FriendshipController extends Controller {
    public static Response handleFriendship(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        return new Response(true, player.friendShipToString());
    }

    public static Response handleTalk(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");
        String message = request.body.get("message");

        Player friend = game.findPlayerByUsername(username);
        if (friend == null) {
            return new Response(false, "Player not found");
        }

        Message msg = new Message(user.getUsername(), friend.getUser().getUsername(), message);
        game.getMessages().add(msg);
        friend.getNotifications().add(msg);
        player.addXpToFriendShip(20 ,friend);
        friend.addXpToFriendShip(20 ,player);

        GameRepository.saveGame(game);
        return new Response(true, "Message has been sent");
    }

    public static Response handleTalkHistory(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        // TODO players should be close together
        String username = request.body.get("username");
        List<Message> messages = game.getMessages().stream().filter((m) -> {
            return (m.getSender().compareToIgnoreCase(username) == 0) &&
                    (m.getRecipient().compareToIgnoreCase(player.getUser().getUsername())) == 0;
        }).toList();

        StringBuilder builder = new StringBuilder();
        for (Message m : messages) {
            builder.append("From : ").append(m.getSender()).append("\n");
            builder.append("Message : ").append(m.getMessage()).append("\n");
            builder.append("------------------------------------------------------\n");
        }
        return new Response(true, builder.toString());
    }

    public static Response handleGift(Request request) {

        return null;
    }

    public static Response handleGiftList(Request request) {
        return null;
    }

    public static Response handleGiftHistory(Request request) {
        return null;
    }

    public static Response handleGiftRate(Request request) {
        return null;
    }

    public static Response handleHug(Request request) {
        return null;
    }

    public static Response handleFlower(Request request) {
        return null;
    }

    public static Response handleAskMarriage(Request request) {
        return null;
    }

    public static Response handleRespondMarriageAccept(Request request) {
        return null;
    }

    public static Response handleRespondMarriageReject(Request request) {
        return null;
    }
}
