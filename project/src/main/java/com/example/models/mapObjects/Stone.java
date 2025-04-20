package com.example.models.mapObjects;

import com.example.models.enums.types.StoneType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Stone extends MapObject {
    private StoneType stoneType;

    public Stone() {}
    public Stone(StoneType stoneType) {
        super(false, "stone", "gray");
        this.stoneType = stoneType;
    }

    public StoneType getStoneType() {
        return stoneType;
    }
}
