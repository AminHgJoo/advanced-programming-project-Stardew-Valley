package com.server.GameServers;

import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.NPCModels.NPC;
import com.common.models.Player;
import com.common.models.User;
import com.google.gson.Gson;
import com.server.controllers.InGameControllers.GameServerController;
import com.server.repositories.GameRepository;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class GameServer extends Thread {
    private final GameServerController controller;
    private final Gson gson = GameGSON.gson;
    private AtomicReference<ArrayList<PlayerConnection>> playerConnections = new AtomicReference<>();
    private GameData game;
    private boolean isRunning = true;
    private int count = 1;

    public GameServer(ArrayList<PlayerConnection> players, GameData game) {
        this.playerConnections.set(players);
        this.game = game;
        HashMap<String, String> res = new HashMap<>();
        res.put("type", "GAME_START");
        res.put("message", "game has been started successfully");
        res.put("game", this.gson.toJson(game));
        for (PlayerConnection playerConnection : players) {
            playerConnection.send(new Gson().toJson(res));
        }
        controller = new GameServerController(this);
    }

    public void broadcast(HashMap<String, String> message) {
        try {
            for (PlayerConnection pc : playerConnections.get()) {
                pc.send(gson.toJson(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        AppWebSocket.broadcast(game , message);
    }

    public void narrowCast(String username, HashMap<String, String> message) {
//        AppWebSocket.narrowcast(username, message);
        try {

            for (PlayerConnection pc : playerConnections.get()) {
                if (pc.getUsername().equals(username)) {
                    pc.send(gson.toJson(message));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userLeave(User user) {
        // TODO do things about player leave in game
        ArrayList<String> arr = new ArrayList<>();
        Player p = null;
        for (Player player : game.getPlayers()) {
            if (player.getUser_id().equals(user.get_id())) {
                p = player;
            }
            arr.add(player.getUser().getUsername());
        }
        p.setOnline(false);
        removePlayerConnection(p);
    }

    public void multicast(HashMap<String, String> msg, ArrayList<String> usernames) {
        for (String u : usernames) {
            narrowCast(u, msg);
        }
    }

    public void multicast(HashMap<String, String> msg, String... usernames) {
        for (String u : usernames) {
            narrowCast(u, msg);
        }
    }

    @Override
    public void run() {
        String gameJson1 = this.gson.toJson(game);
        HashMap<String, String> msg1 = new HashMap<>();
        msg1.put("type", "GAME_UPDATED");
        msg1.put("game", gameJson1);
        broadcast(msg1);
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            if (count % 7 == 0) {
                boolean check = game.advanceTime();
                if (check) {
                    HashMap<String, String> msg = new HashMap<>();
                    msg.put("type", "DAY_END");
                    broadcast(msg);
                }
                String gameJson = this.gson.toJson(game);

                HashMap<String, String> message = new HashMap<>();
                message.put("type", "GAME_UPDATED");
                message.put("game", gameJson);
                broadcast(message);
            }
            if (count % 15 == 0) {
                GameRepository.saveGame(game);
                String gameJson = this.gson.toJson(game);
                HashMap<String, String> message = new HashMap<>();
                message.put("type", "GAME_UPDATED");
                message.put("game", gameJson);
                broadcast(message);
            }
            boolean check = false;
            for (Player p : game.getPlayers()) {
                if (p.isInVillage()) {
                    check = true;
                    break;
                }
            }
            if (check) {
                for (NPC npc : game.getMap().getVillage().getNpcs()) {
                    boolean check2 = npc.update(1);
                    if (check2) {
                        String gameJson = this.gson.toJson(game);
                        HashMap<String, String> message = new HashMap<>();
                        message.put("type", "GAME_UPDATED");
                        message.put("game", gameJson);
                        broadcast(message);
                    }
                }
            }

            for (Player p : game.getPlayers()) {
                if (p.getEnergy() == 0) {
                    HashMap<String, String> msg = new HashMap<>();
                    msg.put("type", "FAINT");
                    narrowCast(p.getUser().getUsername(), msg);
                }
            }

            game.checkForRecipeUnlocking();
            game.handleBuffExpiration();
            game.checkForSkillUpgrades();
        }
    }

    public void handleRequests(Context ctx) {
        if (ctx.method() == HandlerType.POST) {
            controller.routingTheRequests(ctx, this);
        } else if (ctx.method() == HandlerType.GET) {

        }
    }

    public void endGame() {
        isRunning = false;
    }

    public GameData getGame() {
        return game;
    }

    public Player removePlayerConnection(PlayerConnection pc) {
        if (pc != null) {
            playerConnections.get().remove(pc);
            Player p = game.findPlayerByUsername(pc.getUsername());
            p.setOnline(false);
            sentOfflineMessage(p);
            return p;
        }
        return null;
    }

    public void sentOfflineMessage(Player p) {
        HashMap<String, String> msg = new HashMap<>();
        msg.put("type", "PLAYER_OFFLINE");
        msg.put("player_user_id", p.getUser_id());
        msg.put("player_username", p.getUser().getUsername());
        broadcast(msg);
    }

    public ArrayList<PlayerConnection> getPlayerConnections() {
        return playerConnections.get();
    }

    public boolean containsUsername(String username) {
        for (PlayerConnection pc : playerConnections.get()) {
            if (pc.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void removePlayerConnection(Player player) {
        PlayerConnection pc = null;
        for (PlayerConnection playerConnection : playerConnections.get()) {
            if (playerConnection.getUsername().equals(player.getUser().getUsername())) {
                pc = playerConnection;
            }
        }

        if (pc != null) {
            playerConnections.get().remove(pc);
        }
        Player p = player;
        sentOfflineMessage(p);
    }
}
