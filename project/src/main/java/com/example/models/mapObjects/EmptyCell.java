package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class EmptyCell extends MapObject {
    public EmptyCell() {
        super(true, "empty", "yellow");
    }
}
