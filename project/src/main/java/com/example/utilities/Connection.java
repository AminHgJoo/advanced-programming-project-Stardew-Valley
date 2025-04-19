package com.example.utilities;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class Connection {
    private static Datastore database;

    public static Datastore getDatabase() {
        if (database == null) {
            try {
                String DB = "AP_PROJECT";
                if (System.getenv("APP_MODE") != null && System.getenv("APP_MODE").equals("TEST")) {
                    DB += "_TEST";
                }
                database = Morphia.createDatastore(MongoClients.create("mongodb://localhost:27017"), DB);
                database.getMapper().mapPackage("com.example.models");
                database.ensureIndexes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return database;
    }
}