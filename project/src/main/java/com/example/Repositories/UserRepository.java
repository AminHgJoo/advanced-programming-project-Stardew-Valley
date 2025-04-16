package com.example.Repositories;

import com.example.models.User;
import com.example.utilities.Connection;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class UserRepository {
    private static final Datastore db = Connection.getDatabase();
    public static User findUserById(String id) {
        User user = db.find(User.class).filter("_id",new ObjectId(id)).first();
        return user;
    }

    public static User findUserByUsername(String username) {
        User user = db.find(User.class).filter("username",username).first();
        return user;
    }

    public static void saveUser(User user) {
        db.save(user);
    }

    public static ArrayList<User> findAllUsers() {
       ArrayList<User> users = new ArrayList<>(db.find(User.class).iterator().toList());
       return users;
    }

    public static void removeUser(User user) {
        db.delete(user);
    }

    public static User getStayLoggedInUser() {
        return null;
    }

    public static void saveStayLoggedInUser(User user) {
    }
}
