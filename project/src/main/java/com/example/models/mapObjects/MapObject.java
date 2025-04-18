package com.example.models.mapObjects;

abstract public class MapObject {
    final public String type;
    final public boolean isWalkable;
    //TODO: texture

    protected MapObject(boolean isWalkable, String type) {
        this.isWalkable = isWalkable;
        this.type = type;
    }
}
