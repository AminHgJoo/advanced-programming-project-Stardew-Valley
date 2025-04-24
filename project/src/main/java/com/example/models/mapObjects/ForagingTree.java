package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingTree extends MapObject {
    public ForagingTree() {
        super(false, "foragingTree", "black");
    }
}
