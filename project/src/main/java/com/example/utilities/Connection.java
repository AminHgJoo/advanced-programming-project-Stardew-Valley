package com.example.utilities;

import com.example.models.mapObjects.*;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class Connection {
    private static Datastore database;

    public static Datastore getDatabase() {
        if (database == null) {
            try {
                String DB = System.getProperty("DB_NAME");
                String DB_URI = System.getProperty("DB_URI");

                database = Morphia.createDatastore(MongoClients.create(DB_URI), DB);
                database.getMapper().mapPackage("com.example.models");
                database.getMapper().mapPackage("com.example.models.mapObjects");
                database.getMapper().mapPackage("com.example.models.buildings");
                database.getMapper().mapPackage("com.example.models.items");
                database.ensureIndexes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return database;
    }
}