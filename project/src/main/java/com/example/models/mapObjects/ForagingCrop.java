package com.example.models.mapObjects;

import com.example.models.enums.types.ForagingCropsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingCrop extends MapObject {
    public ForagingCropsType type;
    private boolean canBeHarvested;

    public ForagingCrop(){}

    public ForagingCrop(ForagingCropsType type, boolean canBeHarvested) {
        super(true, "foragingCrop", "black");
        this.type = type;
        this.canBeHarvested = canBeHarvested;
    }
}
