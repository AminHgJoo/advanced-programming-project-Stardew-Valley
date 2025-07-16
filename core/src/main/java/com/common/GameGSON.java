package com.common;

import com.common.models.mapObjects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.IOException;
import java.time.LocalDateTime;

public class GameGSON {
    public static final RuntimeTypeAdapterFactory<MapObject> mapObjectAdaptor = RuntimeTypeAdapterFactory
        .of(MapObject.class, "_type")
        .registerSubtype(Water.class, "water")
        .registerSubtype(Tree.class, "tree")
        .registerSubtype(AnimalBlock.class, "animal")
        .registerSubtype(ArtisanBlock.class, "artisanBlock")
        .registerSubtype(BuildingBlock.class, "buildingBlock")
        .registerSubtype(Crop.class, "plant")
        .registerSubtype(DroppedItem.class, "droppedItem")
        .registerSubtype(EmptyCell.class, "empty")
        .registerSubtype(ForagingCrop.class, "foragingCrop")
        .registerSubtype(ForagingMineral.class, "foragingMineral");

    public static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
            @Override
            public void write(JsonWriter out, LocalDateTime value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else
                    out.value(value.toString());
            }

            @Override
            public LocalDateTime read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return LocalDateTime.parse(in.nextString());
            }
        })
        .serializeSpecialFloatingPointValues()
        .registerTypeAdapterFactory(mapObjectAdaptor)
        .create();
}
