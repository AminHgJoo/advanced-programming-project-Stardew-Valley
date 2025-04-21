package com.example.models.mapObjects;

import com.example.models.enums.types.StoneType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Stone extends MapObject {
    public Stone() {
        super(false, "stone", "gray");
    }

    public StoneType getStoneType() {
        return stoneType;
    }
}
