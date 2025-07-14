package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

@Embedded
public class Water extends MapObject {
    @Transient
    private Texture texture;
    //TODO loading texture
    public Water() {
        super(false, "water", "blue", AssetManager.getImage("water"));
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
