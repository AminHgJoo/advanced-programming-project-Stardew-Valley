package com.example.utilities;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class Connection {
    private static Datastore database;

    public static Datastore getDatabase() {
        if (database == null) {
            try {
                database = Morphia.createDatastore(MongoClients.create("mongodb://localhost:27017"), "AP_PROJECT");
                database.getMapper().mapPackage("com.example.models");
                database.ensureIndexes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return database;
    }
}