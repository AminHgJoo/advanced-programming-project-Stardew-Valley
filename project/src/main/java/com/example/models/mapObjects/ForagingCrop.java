package com.example.models.mapObjects;

import com.example.models.enums.types.ForagingCropsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingCrop extends MapObject {
    private ForagingCropsType fCropType;
    private boolean canBeHarvested;

    public ForagingCrop(){
        super();
    }

    public ForagingCrop(ForagingCropsType type, boolean canBeHarvested) {
        super(true, "foragingCrop", "black");
        this.fCropType = type;
        this.canBeHarvested = canBeHarvested;
    }

    public boolean isCanBeHarvested() {
        return canBeHarvested;
    }

    public void setCanBeHarvested(boolean canBeHarvested) {
        this.canBeHarvested = canBeHarvested;
    }

    public ForagingCropsType getfCropType() {
        return fCropType;
    }

    public void setfCropType(ForagingCropsType fCropType) {
        this.fCropType = fCropType;
    }
}
