package com.server.GameServers;

import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadGameServer extends Thread {
    private final GameData game;
    private final HashMap<String, Boolean> activePlayers = new HashMap<>();
    private ArrayList<PlayerConnection> playerConnections;
    private ArrayList<String> usernames = new ArrayList<>();

    public GameData getGame() {
        return game;
    }

    public LoadGameServer(GameData game, String id, ArrayList<PlayerConnection> playerConnection) {
        this.game = game;
        for (Player p : game.getPlayers()) {
            if (p.getUser_id().equals(id)) {
                activePlayers.put(p.getUser_id(), true);
                usernames.add(p.getUser().getUsername());
            } else
                activePlayers.put(p.getUser_id(), false);
        }
        this.playerConnections = playerConnection;
    }

    public boolean checkActivePlayer() {
        for (Player p : game.getPlayers()) {
            if (!activePlayers.get(p.getUser_id())) {
                return false;
            }
        }
        return true;
    }

    public void addUser(User user) {
        activePlayers.put(user.get_id(), true);
        usernames.add(user.get_id());
        playerConnections.add(AppWebSocket.getConnectedPlayers().get(user.getUsername()));
        game.findPlayerByUserId(user.get_id()).setOnline(true);

        HashMap<String, String> msg = new HashMap<>();
        msg.put("player_user_id", user.get_id());
        msg.put("player_username", user.getUsername());
        msg.put("type", "PLAYER_ONLINE");
        AppWebSocket.broadcast(game, msg);
    }

    @Override
    public void run() {
        while (!checkActivePlayer()) {

        }
        AppWebSocket.startOldGame(game, usernames);
    }
}
