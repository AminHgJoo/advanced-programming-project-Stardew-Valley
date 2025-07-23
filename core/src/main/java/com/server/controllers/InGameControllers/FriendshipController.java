package com.server.controllers.InGameControllers;

import com.common.GameGSON;
import com.common.models.*;
import com.common.models.items.Misc;
import com.common.models.mapModels.Coordinate;
import com.server.GameServers.GameServer;
import com.server.repositories.GameRepository;
import com.server.utilities.Response;
import io.javalin.http.Context;
import scala.Int;

import java.util.HashMap;

public class FriendshipController extends Controller {
    public void gift(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            String username = (String) body.get("username");
            String itemName = (String) body.get("item");
            int amount = (Integer) body.get("amount");
            GameData game = gs.getGame();

            Player player = game.findPlayerByUserId(id);
            Player friend = game.findPlayerByUsername(username);
            if (friend == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Player not found"));
                return;
            }
            Slot slot = player.getInventory().getSlotByItemName(itemName);
            if (slot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Slot not found"));
                return;
            }
            if (slot.getCount() < amount) {
                ctx.json(Response.BAD_REQUEST.setMessage("Not enough item"));
                return;
            }
            Gift gift = new Gift(player.getUser().getUsername(), username, slot.getItem(), amount);
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
            String gameJson = GameGSON.gson.toJson(game);
            ctx.json(Response.OK.setBody(gameJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "GAME_UPDATED");
            msg.put("game", gameJson);
            gs.broadcast(msg);

            HashMap<String, String> msgToPlayer = new HashMap<>();
            msgToPlayer.put("type", "RECEIVED_GIFT");
            msgToPlayer.put("gift", gift.toString());
            gs.narrowCast(friend.getUser().getUsername(), msgToPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void giftRate(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            int index = (Integer) body.get("giftNumber");
            int rate = (Integer) body.get("rate");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);

            Gift g = game.findGiftByName(index - 1, player.getUser().getUsername());
            if (g == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Gift not found"));
                return;
            }
            g.setRate(rate);
            int xp = (rate - 3) * 30 + 15;
            Player friend = game.findPlayerByUsername(g.getFrom());
            friend.addXpToFriendShip(xp, player);
            player.addXpToFriendShip(xp, friend);

            String gameJson = GameGSON.gson.toJson(game);
            ctx.json(Response.OK.setBody(gameJson));

            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "GAME_UPDATED");
            msg.put("game", gameJson);
            gs.broadcast(msg);

            HashMap<String, String> msgToPlayer = new HashMap<>();
            msgToPlayer.put("type", "GIFT_RATED");
            msgToPlayer.put("gift", g.toString());
            msgToPlayer.put("rate", rate + "");
            gs.narrowCast(friend.getUser().getUsername(), msgToPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void hug(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            String username = (String) body.get("username");

            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);

            Player friend = game.findPlayerByUsername(username);
            if (friend == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Player not found"));
                return;
            }
            if ((player.isInVillage() && friend.isInVillage()) ||
                (player.getCurrentFarm(game) == friend.getCurrentFarm(game) &&
                    (Coordinate.calculateEuclideanDistance(player.getCoordinate(), friend.getCoordinate()) <= Math.sqrt(2)))) {
                player.addXpToFriendShip(60, friend);
                friend.addXpToFriendShip(60, player);
                String gameJson = GameGSON.gson.toJson(game);
                ctx.json(Response.OK.setBody(gameJson));
                HashMap<String, String> msg = new HashMap<>();
                msg.put("type", "GAME_UPDATED");
                msg.put("game", gameJson);
                gs.broadcast(msg);

                HashMap<String, String> msgToPlayer = new HashMap<>();
                msgToPlayer.put("type", "HUGGED");
                msgToPlayer.put("player_user_id", id);
                msgToPlayer.put("player_username", player.getUser().getUsername());
                gs.narrowCast(friend.getUser().getUsername(), msgToPlayer);
                return;
            }
            ctx.json(Response.BAD_REQUEST.setMessage("long distance"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void flower(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String username = (String) body.get("username");
            String flowerName = (String) body.get("flowerName");

            Player friend = game.findPlayerByUsername(username);
            if ((player.isInVillage() && friend.isInVillage()) ||
                (player.getCurrentFarm(game) == friend.getCurrentFarm(game) &&
                    (Coordinate.calculateEuclideanDistance(player.getCoordinate(), friend.getCoordinate()) <= Math.sqrt(2)))) {
                if (friend == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Player not found"));
                    return;
                }
                Slot flowerSlot = player.getInventory().getSlotByItemName(flowerName);
                if (flowerSlot == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Slot not found"));
                    return;
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
                String gameJson = GameGSON.gson.toJson(game);
                ctx.json(Response.OK.setBody(gameJson));
                HashMap<String, String> msg = new HashMap<>();
                msg.put("type", "GAME_UPDATED");
                msg.put("game", gameJson);
                gs.broadcast(msg);

                HashMap<String, String> msgToPlayer = new HashMap<>();
                msgToPlayer.put("type", "RECEIVED_FLOWER");
                msgToPlayer.put("player_user_id", id);
                msgToPlayer.put("player_username", player.getUser().getUsername());
                msgToPlayer.put("flower", flowerName);
                gs.narrowCast(friend.getUser().getUsername(), msgToPlayer);
                return;
            }

            ctx.json(Response.BAD_REQUEST.setMessage("long distance"));

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void askMarriage(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String username = (String) body.get("username");
            String ring = (String) body.get("ring");

            Player friend = game.findPlayerByUsername(username);
            if (friend == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Player not found"));
                return;
            }
            Slot slot = player.getInventory().getSlotByItemName(ring);
            if (slot == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Slot not found"));
                return;
            }
            Misc ringItem = (Misc) slot.getItem();
            MarriageRequest marriageRequest = new MarriageRequest();
            marriageRequest.setFrom(player.getUser().getUsername());
            marriageRequest.setRing(ringItem);

            friend.getMarriageRequests().add(marriageRequest);

            String gameJson = GameGSON.gson.toJson(game);
            ctx.json(Response.OK.setBody(gameJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "GAME_UPDATED");
            msg.put("game", gameJson);
            gs.broadcast(msg);

            HashMap<String, String> msgToPlayer = new HashMap<>();
            msgToPlayer.put("type", "ASKED_FOR_MARRIAGE");
            msgToPlayer.put("player_user_id", id);
            msgToPlayer.put("player_username", player.getUser().getUsername());
            msgToPlayer.put("ring", ring);
            gs.narrowCast(friend.getUser().getUsername(), msgToPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void acceptMarriage(Context ctx, GameServer gs) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String username = (String) body.get("username");

            Player friend = game.findPlayerByUsername(username);
            if (friend == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Player not found"));
                return;
            }
            MarriageRequest marriageRequest = player.findRequestByUsername(username);
            if (marriageRequest == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Request not found"));
                return;
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

            friend.setPartnerName(player.getUser().getUsername());
            player.setPartnerName(username);
            player.removeMarriageRequest(marriageRequest);

            String gameJson = GameGSON.gson.toJson(game);
            ctx.json(Response.OK.setBody(gameJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "GAME_UPDATED");
            msg.put("game", gameJson);
            gs.broadcast(msg);
            HashMap<String, String> msgToPlayer = new HashMap<>();
            msgToPlayer.put("type", "ACCEPT_MARRIAGE");
            msgToPlayer.put("player_user_id", id);
            msgToPlayer.put("player_username", player.getUser().getUsername());
            gs.narrowCast(friend.getUser().getUsername(), msgToPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void rejectMarriage(Context ctx, GameServer gs) {
        try{
            HashMap<String , Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            GameData game = gs.getGame();
            Player player = game.findPlayerByUserId(id);
            String username = (String)body.get("username");

            Player friend = game.findPlayerByUsername(username);
            if (friend == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Player not found"));
                return;
            }
            MarriageRequest marriageRequest = player.findRequestByUsername(username);
            if (marriageRequest == null) {
                ctx.json(Response.BAD_REQUEST.setMessage("Request not found"));
                return;
            }

            player.removeMarriageRequest(marriageRequest);
            String gameJson = GameGSON.gson.toJson(game);
            ctx.json(Response.OK.setBody(gameJson));
            HashMap<String, String> msg = new HashMap<>();
            msg.put("type", "GAME_UPDATED");
            msg.put("game" , gameJson);
            gs.broadcast(msg);
            HashMap<String, String> msgToPlayer = new HashMap<>();
            msgToPlayer.put("type", "REJECT_MARRIAGE");
            msgToPlayer.put("player_user_id", id);
            msgToPlayer.put("player_username", player.getUser().getUsername());
            gs.narrowCast(friend.getUser().getUsername(), msgToPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
