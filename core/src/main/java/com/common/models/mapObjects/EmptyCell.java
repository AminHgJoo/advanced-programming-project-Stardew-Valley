package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import dev.morphia.annotations.Embedded;

@Embedded
public class EmptyCell extends MapObject {
    //TODO loading texture
    private Texture texture;
    public EmptyCell() {
        super(true, "empty", "yellow", AssetManager.getTextures().get("grass"));
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
