package com.common.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class EmptyCell extends MapObject {
    public EmptyCell() {
        super(true, "empty", "yellow");
    }
}
