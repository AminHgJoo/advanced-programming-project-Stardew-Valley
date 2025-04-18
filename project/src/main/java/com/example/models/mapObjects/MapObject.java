package com.example.models.mapObjects;

abstract public class MapObject {
    final public boolean isWalkable;
    //TODO: texture

    protected MapObject(boolean isWalkable) {
        this.isWalkable = isWalkable;
    }
}
