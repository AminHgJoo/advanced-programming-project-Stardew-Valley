package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

@Embedded
public class EmptyCell extends MapObject {
    //TODO loading texture
    @Transient
    private Texture texture;
    public EmptyCell() {
        super(true, "empty", "yellow", AssetManager.getImage("grass"));
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
