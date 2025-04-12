package models.mapObjects;

import models.enums.StoneType;

public class Stone {
    private final StoneType stoneType;

    public Stone(StoneType stoneType) {
        this.stoneType = stoneType;
    }

    public StoneType getStoneType() {
        return stoneType;
    }
}
