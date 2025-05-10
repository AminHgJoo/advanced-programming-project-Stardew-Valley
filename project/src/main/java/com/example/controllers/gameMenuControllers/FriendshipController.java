package com.example.controllers.gameMenuControllers;

import com.example.Repositories.GameRepository;
import com.example.controllers.Controller;
import com.example.models.*;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.items.Misc;
import com.example.models.mapModels.Coordinate;

import java.util.List;

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
        if ((player.isInVillage() && friend.isInVillage()) ||
                (player.getCurrentFarm(game) == friend.getCurrentFarm(game) &&
                        (Coordinate.calculateEuclideanDistance(player.getCoordinate(), friend.getCoordinate()) <= Math.sqrt(2)))) {
            Message msg = new Message(user.getUsername(), friend.getUser().getUsername(), message);
            game.getMessages().add(msg);
            friend.getNotifications().add(msg);
            player.addXpToFriendShip(20, friend);
            friend.addXpToFriendShip(20, player);

            GameRepository.saveGame(game);
            return new Response(true, "Message has been sent");
        }
        GameRepository.saveGame(game);
        return new Response(false, "long distance");
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
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");
        String itemName = request.body.get("item");
        int amount = Integer.parseInt(request.body.get("amount"));

        Player friend = game.findPlayerByUsername(username);
        if (friend == null) {
            return new Response(false, "Player not found");
        }
        Slot slot = player.getInventory().getSlotByItemName(itemName);
        if (slot == null) {
            return new Response(false, "Item not found");
        }
        if (slot.getCount() < amount) {
            return new Response(false, "You don't have enough item");
        }
        Gift gift = new Gift(user.getUsername(), username, slot.getItem(), amount);
        slot.setCount(slot.getCount() - amount);
        if (slot.getCount() <= 0) {
            player.getInventory().removeSlot(slot);
        }

        Slot fSlot = friend.getInventory().getSlotByItemName(itemName);
        if (fSlot == null) {
            fSlot = new Slot(slot.getItem(), amount);
            friend.getInventory().addSlot(fSlot);
        } else {
            fSlot.setCount(fSlot.getCount() + amount);
        }

        game.getGifts().add(gift);

        GameRepository.saveGame(game);
        return null;
    }

    public static Response handleGiftList(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        return new Response(true, player.getGiftsString(game));
    }

    public static Response handleGiftHistory(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");

        return new Response(true, player.getGiftHistoryString(game, username));
    }

    public static Response handleGiftRate(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        int index = Integer.parseInt(request.body.get("giftNumber"));
        int rate = Integer.parseInt(request.body.get("rate"));

        Gift g = game.findGiftByName(index - 1, user.getUsername());
        if (g == null) {
            return new Response(false, "Gift not found");
        }
        g.setRate(rate);
        int xp = (rate - 3) * 30 + 15;
        Player friend = game.findPlayerByUsername(g.getFrom());
        friend.addXpToFriendShip(xp, player);
        player.addXpToFriendShip(xp, friend);

        GameRepository.saveGame(game);
        return new Response(true, "Successfully rate the gift");
    }

    public static Response handleHug(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");

        Player friend = game.findPlayerByUsername(username);
        if (friend == null) {
            return new Response(false, "Player not found");
        }
        if ((player.isInVillage() && friend.isInVillage()) ||
                (player.getCurrentFarm(game) == friend.getCurrentFarm(game) &&
                        (Coordinate.calculateEuclideanDistance(player.getCoordinate(), friend.getCoordinate()) <= Math.sqrt(2)))) {
            player.addXpToFriendShip(60, friend);
            friend.addXpToFriendShip(60, player);

            GameRepository.saveGame(game);
            return new Response(true, "You hugged " + username);
        }
        GameRepository.saveGame(game);
        return new Response(false, "long distance");
    }


    public static Response handleFlower(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");
        String flowerName = request.body.get("flowerName");

        Player friend = game.findPlayerByUsername(username);
        if ((player.isInVillage() && friend.isInVillage()) ||
                (player.getCurrentFarm(game) == friend.getCurrentFarm(game) &&
                        (Coordinate.calculateEuclideanDistance(player.getCoordinate(), friend.getCoordinate()) <= Math.sqrt(2)))) {
            if (friend == null) {
                return new Response(false, "Player not found");
            }
            Slot flowerSlot = player.getInventory().getSlotByItemName(flowerName);
            if (flowerSlot == null) {
                return new Response(false, "Flower not found");
            }
            flowerSlot.setCount(flowerSlot.getCount() - 1);
            if (flowerSlot.getCount() == 0) {
                player.getInventory().removeSlot(flowerSlot);
            }
            Slot slot = friend.getInventory().getSlotByItemName(flowerName);
            if (slot == null) {
                friend.getInventory().addSlot(flowerSlot);
            } else {
                slot.setCount(slot.getCount() + 1);
            }
            Friendship friendship = player.findFriendshipByFriendName(username);
            if (friendship.getLevel() == 2) {
                friendship.setLevel(3);
                friend.findFriendshipByFriendName(player.getUser().getUsername()).setLevel(3);
            } else {
                player.addXpToFriendShip(100, friend);
                friend.addXpToFriendShip(100, player);
            }

            return new Response(true, "Flower has been sent to " + username);
        }
        GameRepository.saveGame(game);
        return new Response(false, "long distance");
    }

    public static Response handleAskMarriage(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");
        String ring = request.body.get("ring");

        Player friend = game.findPlayerByUsername(username);
        if (friend == null) {
            return new Response(false, "Player not found");
        }
        Slot slot = player.getInventory().getSlotByItemName(ring);
        if (slot == null) {
            return new Response(false, "Ring not found");
        }
        Misc ringItem = (Misc) slot.getItem();
        MarriageRequest marriageRequest = new MarriageRequest();
        marriageRequest.setFrom(user.getUsername());
        marriageRequest.setRing(ringItem);

        friend.getMarriageRequests().add(marriageRequest);
        GameRepository.saveGame(game);

        return new Response(true, "Marriage request was sent");
    }

    public static Response handleRespondMarriageAccept(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");

        Player friend = game.findPlayerByUsername(username);
        if (friend == null) {
            return new Response(false, "Player not found");
        }
        MarriageRequest marriageRequest = player.findRequestByUsername(username);
        if (marriageRequest == null) {
            return new Response(false, "Marriage request not found");
        }

        Slot slot = friend.getInventory().getSlotByItemName(marriageRequest.getRing().getName());
        slot.setCount(slot.getCount() - 1);
        if (slot.getCount() == 0) {
            friend.getInventory().removeSlot(slot);
        }
        Slot playerSlot = player.getInventory().getSlotByItemName(marriageRequest.getRing().getName());
        if (playerSlot == null) {
            playerSlot = new Slot(marriageRequest.getRing(), 1);
            player.getInventory().addSlot(playerSlot);
        } else {
            playerSlot.setCount(playerSlot.getCount() + 1);
        }
        friend.setFriendShipLevel(4, player);
        player.setFriendShipLevel(4, friend);

        friend.setPartnerName(user.getUsername());
        player.setPartnerName(username);

        player.removeMarriageRequest(marriageRequest);
        GameRepository.saveGame(game);
        return new Response(true, "Yar mobarak bada ishala mobarak bada");
    }

    public static Response handleRespondMarriageReject(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();

        String username = request.body.get("username");

        Player friend = game.findPlayerByUsername(username);
        if (friend == null) {
            return new Response(false, "Player not found");
        }
        MarriageRequest marriageRequest = player.findRequestByUsername(username);
        if (marriageRequest == null) {
            return new Response(false, "Marriage request not found");
        }

        player.removeMarriageRequest(marriageRequest);
        GameRepository.saveGame(game);
        return new Response(true, ":(( reject shodi");
    }
}
