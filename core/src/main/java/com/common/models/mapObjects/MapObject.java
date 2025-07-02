package com.common.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
abstract public class MapObject {
    public String type;
    public boolean isWalkable;
    public String color;
    //TODO: texture

    public MapObject() {

    }

    public MapObject(boolean isWalkable, String type, String color) {
        this.isWalkable = isWalkable;
        this.type = type;
        this.color = color;
    }

    @Override
    public String toString() {
        if (this instanceof AnimalBlock)
            return "L";
        return type.substring(0, 1).toUpperCase();
    }
}
