package com.common.models.mapObjects;

import dev.morphia.annotations.Embedded;

@Embedded
public class BuildingBlock extends MapObject {
    public String buildingType;

    public BuildingBlock() {
        super();
    }

    public BuildingBlock(boolean isWalkable, String buildingType) {
        //TODO display
        super(isWalkable, "buildingBlock", "red", null);
        this.buildingType = buildingType;
    }
}
