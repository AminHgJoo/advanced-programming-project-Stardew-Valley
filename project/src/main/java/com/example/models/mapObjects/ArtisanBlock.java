package com.example.models.mapObjects;

import com.example.models.enums.types.ArtisanBlockType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ArtisanBlock extends MapObject {
    private ArtisanBlockType artisanType;

    public ArtisanBlock(ArtisanBlockType artisanType) {
        super(false, "artisanBlock", "black");
        this.artisanType = artisanType;
    }

    public ArtisanBlockType getArtisanType() {
        return artisanType;
    }
}
