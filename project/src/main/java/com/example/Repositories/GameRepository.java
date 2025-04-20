package com.example.Repositories;

import com.example.models.Game;
import com.example.models.Player;
import com.example.models.User;
import com.example.utilities.Connection;
import dev.morphia.Datastore;
import dev.morphia.query.Update;
import dev.morphia.query.updates.UpdateOperator;
import dev.morphia.query.updates.UpdateOperators;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class GameRepository {
    private static final Datastore db = Connection.getDatabase();

    public static Game findGameById(String id, boolean populateFlag) {
        Game game = db.find(Game.class).filter("_id", new ObjectId(id)).first();
        if (populateFlag) {
            populateUserOfPlayers(game);
        }
        return game;
    }

    public static void populateUserOfPlayers(Game game) {
        if (game == null) return;
        for (Player player : game.getPlayers()) {
            player.setUser(UserRepository.findUserById(player.getUser_id().toString()));
        }
        game.getCurrentPlayer().setUser(UserRepository.findUserById(game.getCurrentPlayer().getUser_id().toString()));
    }

    public static void saveGame(Game game) {
        for (Player player : game.getPlayers()) {
            player.setUser(null);
        }
        game.getCurrentPlayer().setUser(null);
        db.save(game);
    }

    public static ArrayList<Game> findAllGames(boolean populateFlag) {
        ArrayList<Game> games = new ArrayList<>(db.find(Game.class).iterator().toList());
        if (populateFlag) {
            for (Game game : games) {
                populateUserOfPlayers(game);
            }
        }
        return games;
    }

    public static void removeGame(Game game) {
        for (Player player : game.getPlayers()) {
            player.getUser().setCurrentGame(null);
            UserRepository.saveUser(player.getUser());
        }
        db.delete(game);
    }
}
