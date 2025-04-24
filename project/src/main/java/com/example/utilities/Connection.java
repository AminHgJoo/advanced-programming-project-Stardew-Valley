package com.example.utilities;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;

public class Connection {
    private static Datastore database;

    public static Datastore getDatabase() {
        if (database == null) {
            try {
                String DB = System.getProperty("DB_NAME");
                String DB_URI = System.getProperty("DB_URI");

                database = Morphia.createDatastore(MongoClients.create(DB_URI), DB);
                database.getMapper().mapPackage("com.example.models.items");
                database.getMapper().mapPackage("com.example.models.mapObjects");
                database.getMapper().mapPackage("com.example.models.mapModels");
                database.getMapper().mapPackage("com.example.models.skills");
                database.getMapper().mapPackage("com.example.models.buildings");
                database.getMapper().mapPackage("com.example.models.NPCModels");
//                database.getMapper().mapPackage("com.example.models.enums.types");
                database.getMapper().mapPackage("com.example.models");
                // Log mapped classes
//                database.getMapper().getMappedEntities().forEach(entity -> {
//                    testCodec(entity.getType(),database);
//                });

                database.ensureIndexes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return database;
    }
    public static void testCodec(Class<?> clazz , Datastore datastore) {
        try {
            Codec<?> codec = datastore.getCodecRegistry().get(clazz);
            System.out.println(clazz.getSimpleName() + ": " +
                    (codec != null ? "OK" : "NULL CODEC"));
        } catch (CodecConfigurationException e) {
            System.out.println(clazz.getSimpleName() + ": ERROR - " + e.getMessage());
        }
    }
}