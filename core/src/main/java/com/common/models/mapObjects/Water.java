package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import dev.morphia.annotations.Embedded;

@Embedded
public class Water extends MapObject {
    private Texture texture;
    //TODO loading texture
    public Water() {
        super(false, "water", "blue");
        this.texture = AssetManager.getTextures().get("water");
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
