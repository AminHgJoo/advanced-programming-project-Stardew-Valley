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
        this.texture = AssetManager.getImage(type.getTextureName());
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            this.texture = AssetManager.getImage(foragingCropsType.getTextureName());
        }
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
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
}
