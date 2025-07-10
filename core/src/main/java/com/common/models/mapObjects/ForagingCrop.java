package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.enums.types.mapObjectTypes.ForagingCropsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingCrop extends MapObject {
    private ForagingCropsType foragingCropsType;
    private boolean canBeHarvested;
    //TODO texture loading

    public ForagingCrop() {
        super();
    }

    public ForagingCrop(ForagingCropsType type, boolean canBeHarvested) {
        super(true, "foragingCrop", "green", null);
        this.foragingCropsType = type;
        this.canBeHarvested = canBeHarvested;
        this.texture = AssetManager.getTextures().get(type.getTextureName());
    }

    public boolean isCanBeHarvested() {
        return canBeHarvested;
    }

    public void setCanBeHarvested(boolean canBeHarvested) {
        this.canBeHarvested = canBeHarvested;
    }

    public ForagingCropsType getForagingCropsType() {
        return foragingCropsType;
    }

    public void setForagingCropsType(ForagingCropsType foragingCropsType) {
        this.foragingCropsType = foragingCropsType;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
