package com.example.Repositories;

import com.example.models.Game;
import com.example.utilities.Connection;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class GameRepository {
    private static final Datastore db = Connection.getDatabase();
    public static Game findGameById(String id) {
        Game game = db.find(Game.class).filter("_id" , new ObjectId(id)).first();
        return game;
    }

    public static void saveGame(Game game) {
        db.save(game);
    }

    public static ArrayList<Game> findAllGames() {
        ArrayList<Game> games = new ArrayList<>(db.find(Game.class).iterator().toList());
        return games;
    }

    public static void removeGame(Game game) {
        db.delete(game);
    }
}
