package com.example.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class BuildingBlock extends MapObject {
    public BuildingBlock(){}
    public BuildingBlock(boolean isWalkable) {
        super(isWalkable, "buildingBlock", "red");
    }
}
