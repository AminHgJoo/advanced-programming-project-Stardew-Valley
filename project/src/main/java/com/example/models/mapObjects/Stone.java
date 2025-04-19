package com.example.models.mapObjects;

import com.example.models.enums.types.StoneType;

public class Stone extends MapObject {
    private final StoneType stoneType;

    public Stone(StoneType stoneType) {
        super(false, "stone", "gray");
        this.stoneType = stoneType;
    }

    public StoneType getStoneType() {
        return stoneType;
    }
}
