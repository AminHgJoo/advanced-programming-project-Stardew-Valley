package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingSeed extends MapObject {
    public ForagingSeed() {
        super(true, "foragingSeed", "black");
    }
}
