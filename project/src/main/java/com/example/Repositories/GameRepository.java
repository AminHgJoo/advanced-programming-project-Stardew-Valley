package com.example.Repositories;

import com.example.models.Game;
import com.example.models.Player;
import com.example.models.User;
import com.example.utilities.Connection;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.Update;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperator;
import dev.morphia.query.updates.UpdateOperators;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

// TODO optimizing the queiries

public class GameRepository {
    private static final Datastore db = Connection.getDatabase();

    public static Game findGameById(String id, boolean populateFlag) {
        try {
            Game game = db.find(Game.class)
                    .filter(Filters.eq("_id", new ObjectId(id)))
                    .first();
            return game;
        } catch (Exception e) {
//           System.out.println(e.getCause());
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void populateUserOfPlayers(Game game) {
        if (game == null) return;
        for (Player player : game.getPlayers()) {
            player.setUser(UserRepository.findUserById(player.getUser_id().toString()));
        }
        game.getCurrentPlayer().setUser(UserRepository.findUserById(game.getCurrentPlayer().getUser_id().toString()));
    }

    public static void saveGame(Game game) {
        if (game.getGameThread() != null) {
            game.getGameThread().setGame(game);
        }
//        db.save(game);

        new Thread(() -> {
            try {
                db.save(game);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void saveGame(Game game, ArrayList<User> users) {
        new Thread(() -> {
            try {
                db.save(game);
                for (User u : users) {
                    u.setCurrentGameId(game.get_id());
                    u.setCurrentGame(null);
                    u.getGames().add(game.get_id());
                    u.setNumberOfGames(u.getNumberOfGames() + 1);
                    UserRepository.saveUser(u);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static ArrayList<Game> findAllGames(boolean populateFlag) {
        ArrayList<Game> games = new ArrayList<>(db.find(Game.class).iterator().toList());
        return games;
    }

    public static Query<Game> updateGame(Game game) {
        return db.find(Game.class).filter(Filters.eq("_id", game.get_id().toString()));
    }

    public static void removeGame(Game game) {
        new Thread(() -> {
            for (Player player : game.getPlayers()) {
                player.getUser().setCurrentGame(null);
                UserRepository.saveUser(player.getUser());
            }
            db.delete(game);
        }).start();
    }
}
