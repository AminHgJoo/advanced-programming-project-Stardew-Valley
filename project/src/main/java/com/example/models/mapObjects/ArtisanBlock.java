package com.example.models.mapObjects;

import com.example.models.Slot;
import com.example.models.enums.types.mapObjectTypes.ArtisanBlockType;
import dev.morphia.annotations.Embedded;

import java.sql.Time;
import java.time.LocalDateTime;

@Embedded
public class ArtisanBlock extends MapObject {
    private ArtisanBlockType artisanType;
    public LocalDateTime PrepTime;
    public Slot productSlot;
    public boolean beingUsed;
    public boolean canBeCollected;


    public ArtisanBlock(ArtisanBlockType artisanType) {
        super(false, "artisanBlock", "black");
        this.artisanType = artisanType;
        this.beingUsed = false;
        canBeCollected = false;
    }

    public ArtisanBlockType getArtisanType() {
        return artisanType;
    }
}
