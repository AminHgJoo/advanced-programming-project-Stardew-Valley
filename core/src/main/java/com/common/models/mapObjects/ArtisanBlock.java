package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.Slot;
import com.common.models.enums.types.mapObjectTypes.ArtisanBlockType;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;

@Embedded
public class ArtisanBlock extends MapObject {
    public LocalDateTime prepTime;
    public Slot productSlot;
    public boolean beingUsed;
    public boolean canBeCollected;
    private ArtisanBlockType artisanType;
    public LocalDateTime startTime;

    public ArtisanBlock() {
        super();
    }

    public ArtisanBlock(ArtisanBlockType artisanType) {
        super(false, "artisanBlock", "red", null);
        this.artisanType = artisanType;
        this.beingUsed = false;
        this.canBeCollected = false;
        this.texture = AssetManager.getImage(artisanType.getTextureName());
    }

    public ArtisanBlockType getArtisanType() {
        return artisanType;
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            this.texture = AssetManager.getImage(artisanType.getTextureName());
        }
        return texture;
    }

}
