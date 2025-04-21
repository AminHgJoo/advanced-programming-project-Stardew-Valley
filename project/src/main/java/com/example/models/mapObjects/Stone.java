package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class Stone extends MapObject {
    public Stone() {
        super(false, "stone", "gray");
    }

}
