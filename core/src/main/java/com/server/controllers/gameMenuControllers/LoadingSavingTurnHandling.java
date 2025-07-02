package com.server.controllers.gameMenuControllers;

import com.common.repositories.GameRepository;
import com.common.repositories.UserRepository;
import com.server.controllers.Controller;
import com.common.models.App;
import com.common.models.GameData;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.Player;
import com.common.models.User;
import com.common.models.enums.types.MenuTypes;
import com.common.models.mapModels.Farm;
import com.common.views.AppView;
import com.common.views.gameViews.GameThread;

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
            if (user.equals(App.getLoggedInUser())) {
                return new Response(false, "You can't add yourself.");
            }
            if (user.getCurrentGame() != null) {
                return new Response(false, "Player " + username + " is already in a game");
            }
            Player player = new Player(user);
            players.add(player);
        }
        GameData gameData = new GameData(players, players.get(0));
        for (Player player : players) {
            player.getUser().setCurrentGame(gameData);
        }
        isWaitingForChoosingMap = true;
        return new Response(true, "The game has been made successfully. Awaiting each user's map choice...\n" +
                "Use 'game map <map_number>' to pick map 1 or 2.");
    }

    public static Response handleMapSelection(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        Player player = gameData.getCurrentPlayer();
        int mapNumber = Integer.parseInt(request.body.get("mapNumber"));
        if (mapNumber != 1 && mapNumber != 2) {
            return new Response(false, "Invalid map number");
        }
        Farm farm = Farm.makeFarm(mapNumber);
        gameData.getMap().addFarm(farm);
        player.setFarm(farm);
        boolean check = gameData.cycleToNextPlayer();
        if (check) {
            isWaitingForChoosingMap = false;
        }
        String responseString = player.getUser().getUsername() + " has chosen their farm.";
        if (check) {
            ArrayList<User> users = new ArrayList<>();
            for (Player player1 : gameData.getPlayers()) {
                users.add(player1.getUser());
                player1.setUser(null);
            }
            responseString += "\nAll farm selection successful! Game successfully created!";
            GameRepository.saveGame(gameData, users);
        }
        return new Response(true, responseString);
    }

    public static Response handleLoadGame(Request request) {
        if (App.getLoggedInUser().getCurrentGame() == null) {
            return new Response(false, "No saved game found.");
        }
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();
        ArrayList<Player> players = gameData.getPlayers();
        Player firstPlayer = players.get(0);
        Player loader = null;
        for (Player player : players) {
            User u = UserRepository.findUserById(player.getUser_id().toString());
            player.setUser(u);
            if (player.getUser().equals(user)) {
                gameData.setCurrentPlayer(player);
                loader = player;
                break;
            }
        }
        gameData.setGameOngoing(true);
        int loaderIndex = players.indexOf(loader);
        players.set(0, loader);
        players.set(loaderIndex, firstPlayer);

        gameData.setGameThread(new GameThread(gameData));
        gameData.getGameThread().keepRunning = true;
        gameData.getGameThread().start();
        GameRepository.saveGame(gameData);

        return new Response(true, "The game has been loaded successfully. Welcome "
                + user.getUsername());
    }

    public static Response handleExitGame(Request request) {
        GameData gameData = App.getLoggedInUser().getCurrentGame();

        if (gameData.getCurrentPlayer().getUser().equals(App.getLoggedInUser())) {

            gameData.setGameOngoing(false);
            gameData.getGameThread().keepRunning = false;
            gameData.hasTurnCycleFinished = false;
            for (Player p : gameData.getPlayers()) {
                User u = p.getUser();
                int m = u.getMoneyHighScore();
                u.setMoneyHighScore(Math.max(m, p.getMoney(gameData)));
                UserRepository.saveUser(u);
            }
                App.setCurrMenuType(MenuTypes.GameMenu);
            GameRepository.saveGame(gameData);

            return new Response(true, "Exiting and saving game. Redirecting to game menu...");
        } else {
            return new Response(false, "Only the logged in user may exit the game.");
        }
    }

    public static Response handleForceDeleteGame(Request request) {

        GameData gameData = App.getLoggedInUser().getCurrentGame();
        User loggedInUser = App.getLoggedInUser();

        if (!gameData.getCurrentPlayer().getUser().equals(App.getLoggedInUser())) {
            return new Response(false, "Only the logged in user can force delete the game.");
        }

        System.out.println("Attempting to delete the game. Initializing voting sequence...");
        gameData.cycleToNextPlayer();

        boolean success = false;
        while (!success) {
            System.out.println("Awaiting confirmation (Y/n) from player "
                    + gameData.getCurrentPlayer().getUser().getUsername());
            String answer = AppView.scanner.nextLine();
            if (answer.compareToIgnoreCase("y") == 0) {
                System.out.println("Confirmation received.");
                success = gameData.cycleToNextPlayer();
            } else {
                gameData.setCurrentPlayer(gameData.findPlayerByUser(loggedInUser));
                return new Response(true, "Confirmation not received. Aborting...");
            }
        }
        gameData.getGameThread().keepRunning = false;
        gameData.setGameOngoing(false);
        loggedInUser.setCurrentGame(null);
        loggedInUser.getGames().remove(gameData.get_id());
        GameRepository.removeGame(gameData);
        return new Response(true, "The game has been deleted successfully. Going to game menu...");
    }

    public static Response handleNextTurn(Request request) {
        User user = App.getLoggedInUser();
        GameData gameData = user.getCurrentGame();

        int numberOfPlayers = gameData.getPlayers().size();
        int playerIndex;

        gameData.getCurrentPlayer().setUsedEnergyInTurn(0);

        String responseString = "";
        do {
            playerIndex = gameData.getPlayers().indexOf(gameData.getCurrentPlayer());
            if (playerIndex == numberOfPlayers - 1) {
                gameData.setCurrentPlayer(gameData.getPlayers().get(0));
                gameData.hasTurnCycleFinished = true;
            } else {
                gameData.setCurrentPlayer(gameData.getPlayers().get(playerIndex + 1));
            }
            if (gameData.getCurrentPlayer().isPlayerFainted()) {
                responseString +=
                        ("Player " + gameData.getCurrentPlayer().getUser().getUsername() + " was fainted and skipped.\n");
            }
        } while (gameData.getCurrentPlayer().isPlayerFainted());


        responseString += ("It is " + gameData.getCurrentPlayer().getUser().getUsername() + "'s turn now!");
        String notifs = gameData.getCurrentPlayer().getNotificationsString(gameData);
        gameData.getCurrentPlayer().reInitNotifications();
        GameRepository.saveGame(gameData);
        return new Response(true, responseString + "\n" + notifs);
    }
}
