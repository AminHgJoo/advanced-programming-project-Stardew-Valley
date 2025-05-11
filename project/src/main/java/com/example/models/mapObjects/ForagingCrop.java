package com.example.models.mapObjects;

import com.example.models.enums.types.mapObjectTypes.ForagingCropsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingCrop extends MapObject {
    private ForagingCropsType foragingCropsType;
    private boolean canBeHarvested;

    public ForagingCrop() {
        super();
    }

    public ForagingCrop(ForagingCropsType type, boolean canBeHarvested) {
        super(true, "foragingCrop", "green");
        this.foragingCropsType = type;
        this.canBeHarvested = canBeHarvested;
    }

    public boolean isCanBeHarvested() {
        return canBeHarvested;
    }

    public void setCanBeHarvested(boolean canBeHarvested) {
        this.canBeHarvested = canBeHarvested;
    }

    public ForagingCropsType getForagingCropsType() {
        return foragingCropsType;
    }

    public void setForagingCropsType(ForagingCropsType foragingCropsType) {
        this.foragingCropsType = foragingCropsType;
    }
}
