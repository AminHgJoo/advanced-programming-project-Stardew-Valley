package com.example.models.mapObjects;

abstract public class MapObject {
    final public String type;
    final public boolean isWalkable;
    final public String color;
    //TODO: texture

    protected MapObject(boolean isWalkable, String type, String color) {
        this.isWalkable = isWalkable;
        this.type = type;
        this.color = color;
    }

    @Override
    public String toString() {
        return type.substring(0, 1).toUpperCase();
    }
}
