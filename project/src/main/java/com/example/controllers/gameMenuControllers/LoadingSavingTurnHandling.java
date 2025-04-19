package com.example.controllers.gameMenuControllers;

import com.example.Repositories.UserRepository;
import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.Game;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.Player;
import com.example.models.User;

import java.util.ArrayList;

public class LoadingSavingTurnHandling extends Controller {
    public static boolean isWaitingForChoosingMap = false;

    public static Response handleNewGame(Request request) {
        String[] usernames = request.body.get("users").split("\\s+");

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(App.getLoggedInUser()));
        for (int i = 1; i < usernames.length; i++) {
            String username = usernames[i];
            User user = UserRepository.findUserByUsername(username);
            if (user == null) {
                return new Response(false, "User not found");
            }
            Player player = new Player(user);
            players.add(player);
        }
        Game game = new Game(players, players.get(0));
        for (Player player : players) {
            player.getUser().setCurrentGame(game);
        }
        isWaitingForChoosingMap = true;
        return new Response(true, "The game has been made successfully. Awaiting each user's map choice...");
    }

    public static Response handleMapSelection(Request request) {
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Player player = game.getCurrentPlayer();
        int mapNumber = Integer.parseInt(request.body.get("mapNumber"));
        player.setFarm(game.getMap().getFarms().get(mapNumber - 1));
        boolean check = game.nextPlayerTurn();
        if (check) {
            isWaitingForChoosingMap = false;
        }
        return new Response(true, player.getUser().getUsername() + "'s chosen the farm");
    }

    public static Response handleLoadGame(Request request) {
        return null;

    }

    public static Response handleExitGame(Request request) {
        return null;

    }

    public static Response handleNextTurn(Request request) {
        return null;

    }
}
