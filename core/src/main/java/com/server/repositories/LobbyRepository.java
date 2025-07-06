package com.server.repositories;

import com.common.models.User;
import com.common.models.networking.Lobby;
import com.server.utilities.Connection;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;

import java.util.ArrayList;

public class LobbyRepository {
    private static final Datastore db = Connection.getDatabase();

    public static Lobby findById(String id) {
        Lobby lobby = db.find(Lobby.class).filter(Filters.eq("_id", id)).first();
        return lobby;
    }

    public static Lobby findByName(String name) {
        Lobby lobby = db.find(Lobby.class).filter(Filters.eq("name", name)).first();
        return lobby;
    }

    public static ArrayList<Lobby> findAll(boolean flag) {
        if (flag) {
            ArrayList<Lobby> lobbies = new ArrayList<>(db.find(Lobby.class).iterator().toList());
            return lobbies;
        } else {
            ArrayList<Lobby> lobbies = new ArrayList<>(db.find(Lobby.class).filter("isVisible", true)
                .iterator().toList());
            return lobbies;
        }
    }

    public static ArrayList<Lobby> findByOwnerUsername(String username) {
        ArrayList<Lobby> lobby = new ArrayList(db.find(Lobby.class).filter(Filters.eq("ownerUsername", username))
            .iterator().toList());
        return lobby;
    }

    public static void save(Lobby lobby) {
        db.save(lobby);
    }

    public static void delete(Lobby lobby) {
        db.delete(lobby);
    }
}
