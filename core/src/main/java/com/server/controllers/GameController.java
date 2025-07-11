package com.server.controllers;

import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.User;
import com.common.models.networking.Lobby;
import com.server.GameServers.AppWebSocket;
import com.server.repositories.GameRepository;
import com.server.repositories.LobbyRepository;
import com.server.repositories.UserRepository;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.ArrayList;

public class GameController {
    public void startGame(Context ctx) {
        try {
            String id = ctx.attribute("id");
            String lobbyId = ctx.pathParam("lobbyId");

            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found"));
                return;
            }
            Lobby lobby = LobbyRepository.findById(lobbyId);
            if (lobby == null) {
                ctx.json(Response.NOT_FOUND.setMessage("Lobby not found"));
                return;
            }
            if (!lobby.getOwnerUsername().equals(user.getUsername())) {
                ctx.json(Response.FORBIDDEN.setMessage("You are not the owner of this lobby"));
                return;
            }

            if (lobby.getUsers().size() <= 2) {
                ctx.json(Response.BAD_REQUEST.setMessage("Not enough players"));
                return;
            }

            ArrayList<Player> players = new ArrayList<>();
            for (String username : lobby.getUsers()) {
                User u = UserRepository.findUserByUsername(username);
                if (u == null) {
                    ctx.json(Response.NOT_FOUND.setMessage("User " + username + " not found"));
                    return;
                }
                Player p = new Player(u);
                players.add(p);
            }
            GameData game = new GameData(players);

            GameRepository.saveGame(game);
            AppWebSocket.startGame(game, lobby);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
