package com.common.models.mapObjects;

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

    public ArtisanBlock() {
        super();
    }

    public ArtisanBlock(ArtisanBlockType artisanType) {
        super(false, "artisanBlock", "red");
        this.artisanType = artisanType;
        this.beingUsed = false;
        this.canBeCollected = false;
    }

    public ArtisanBlockType getArtisanType() {
        return artisanType;
    }
}
