package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingCrop extends MapObject {
    public ForagingCrop() {
        super(true, "foragingCrop", "black");
    }
}
