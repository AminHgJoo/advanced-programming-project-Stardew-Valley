package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class ArtisanBlock extends MapObject {
    public ArtisanBlock() {
        super(false, "artisanBlock", "black");
    }
}
