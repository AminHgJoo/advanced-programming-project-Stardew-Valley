package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import dev.morphia.annotations.Embedded;

@Embedded
public class Water extends MapObject {
    //TODO loading texture
    public Water() {
        super(false, "water", "blue", AssetManager.getImage("water"));
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            texture = AssetManager.getImage("water");
        }
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
