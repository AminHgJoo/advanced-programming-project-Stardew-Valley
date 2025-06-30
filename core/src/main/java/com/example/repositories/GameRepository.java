package com.example.repositories;

import com.example.models.GameData;
import com.example.models.Player;
import com.example.models.User;
import com.example.utilities.Connection;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class GameRepository {
    private static final Datastore db = Connection.getDatabase();

    public static GameData findGameById(String id, boolean populateFlag) {
        try {
            GameData gameData = db.find(GameData.class)
                    .filter(Filters.eq("_id", new ObjectId(id)))
                    .first();
            if (gameData != null && gameData.getCurrentPlayer() != null)
                gameData.setCurrentPlayer(gameData.findPlayerByUsername(gameData.getCurrentPlayer().getUser().getUsername()));
            if(gameData != null){
                for (Player player : gameData.getPlayers()) {
                    player.setFarm(gameData.getFarmByNumber(player.getFarm().getFarmNumber()));
                }
            }

            return gameData;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void populateUserOfPlayers(GameData gameData) {
        if (gameData == null) return;
        for (Player player : gameData.getPlayers()) {
            player.setUser(UserRepository.findUserById(player.getUser_id().toString()));
        }
        gameData.getCurrentPlayer().setUser(UserRepository.findUserById(gameData.getCurrentPlayer().getUser_id().toString()));
    }

    public static void saveGame(GameData gameData) {
        if (gameData.getGameThread() != null) {
            gameData.getGameThread().setGame(gameData);
        }
        db.save(gameData);

//        new Thread(() -> {
//            try {
//                db.save(game);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    public static void saveGame(GameData gameData, ArrayList<User> users) {
        db.save(gameData);
        for (User u : users) {
            u.setCurrentGameId(gameData.get_id());
            u.setCurrentGame(null);
            u.getGames().add(gameData.get_id());
            u.setNumberOfGames(u.getNumberOfGames() + 1);
            UserRepository.saveUser(u);
        }
//        new Thread(() -> {
//            try {
//                db.save(game);
//                for (User u : users) {
//                    u.setCurrentGameId(game.get_id());
//                    u.setCurrentGame(null);
//                    u.getGames().add(game.get_id());
//                    u.setNumberOfGames(u.getNumberOfGames() + 1);
//                    UserRepository.saveUser(u);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    public static ArrayList<GameData> findAllGames(boolean populateFlag) {
        ArrayList<GameData> gameData = new ArrayList<>(db.find(GameData.class).iterator().toList());
        return gameData;
    }

    public static Query<GameData> updateGame(GameData gameData) {
        return db.find(GameData.class).filter(Filters.eq("_id", gameData.get_id().toString()));
    }

    public static void removeGame(GameData gameData) {
        db.delete(gameData);
        for (Player player : gameData.getPlayers()) {
            player.getUser().setCurrentGame(null);
            player.getUser().setCurrentGameId(null);
            player.getUser().getGames().remove(gameData.get_id());
            UserRepository.saveUser(player.getUser());
        }
    }
}
