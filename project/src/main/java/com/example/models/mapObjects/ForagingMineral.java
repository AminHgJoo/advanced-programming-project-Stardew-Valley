package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingMineral extends MapObject {
    public ForagingMineral() {
        super(false, "foragingMineral", "black");
    }
}
