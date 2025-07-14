package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

@Embedded
public class MapObject {
    public String type;
    public boolean isWalkable;
    public String color;
    @Transient
    public Texture texture;
    //TODO: texture

    public MapObject() {

    }

    public MapObject(boolean isWalkable, String type, String color, Texture texture) {
        this.isWalkable = isWalkable;
        this.type = type;
        this.color = color;
        this.texture = texture;
    }


    @Override
    public String toString() {
        if (this instanceof AnimalBlock)
            return "L";
        return type.substring(0, 1).toUpperCase();
    }
}
