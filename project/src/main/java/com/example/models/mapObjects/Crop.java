package com.example.models.mapObjects;

import com.example.models.enums.Season;
import com.example.models.enums.types.CropType;
import dev.morphia.annotations.Embedded;

// TODO CROP STAGE
@Embedded
public class Crop extends MapObject {
    public final Season seasonOfGrowth;
    public final boolean canBeGiant;
    public final CropType cropType;
    private int daysToNextStage;
    private int stageNumber;
    private boolean hasBeenWateredToday;

    public Crop(Season seasonOfGrowth, boolean canBeGiant, CropType plantType) {
        super(true, "plant", "green");
        this.seasonOfGrowth = seasonOfGrowth;
        this.canBeGiant = canBeGiant;
        this.cropType = plantType;
        stageNumber = 0;
        daysToNextStage = cropType.StageZeroDaysToNextStage;
        this.hasBeenWateredToday = false;
    }

    public boolean isHasBeenWateredToday() {
        return hasBeenWateredToday;
    }

    public void setHasBeenWateredToday(boolean hasBeenWateredToday) {
        this.hasBeenWateredToday = hasBeenWateredToday;
    }

    public int getDaysToNextStage() {
        return daysToNextStage;
    }

    public void setDaysToNextStage(int daysToNextStage) {
        this.daysToNextStage = daysToNextStage;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
    }
}
