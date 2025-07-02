package com.common.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class Water extends MapObject {
    public Water() {
        super(false, "water", "blue");
    }
}
