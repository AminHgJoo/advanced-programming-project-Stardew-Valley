package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingMineral extends MapObject {
    private ForagingMineralsType fMType;
    //TODO loading texture


    public ForagingMineral() {
    }

    public ForagingMineral(ForagingMineralsType type, String color, String name) {
        super(false, "foragingMineral", color, AssetManager.getImage(type.getTextureName()));
        this.fMType = type;
    }

    public ForagingMineral(ForagingMineralsType type) {
        super(false, "foragingMineral", "black", AssetManager.getImage(type.getTextureName()));
        this.fMType = type;
    }

    @Override
    public String toString() {
        return "M";
    }

    public ForagingMineralsType getFMType() {
        return fMType;
    }

    public void setFMType(ForagingMineralsType type) {
        this.fMType = type;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
