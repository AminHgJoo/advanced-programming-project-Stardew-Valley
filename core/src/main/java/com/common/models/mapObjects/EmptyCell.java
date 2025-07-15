package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

@Embedded
public class EmptyCell extends MapObject {

    public EmptyCell() {
        super(true, "empty", "yellow", AssetManager.getImage("grass"));
    }

    @Override
    public Texture getTexture(){
        if(texture == null){
            this.texture = AssetManager.getImage("grass");
        }
        return texture;
    }

}
