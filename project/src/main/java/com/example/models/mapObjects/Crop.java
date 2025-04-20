package com.example.models.mapObjects;

import com.example.models.enums.Season;
import com.example.models.enums.types.CropType;
// TODO CROP STAGE
public class Crop extends MapObject {
    public final Season seasonOfGrowth;
    public final boolean canBeGiant;
    public final CropType cropType;
    private int daysToNextStage;
    private int stageNumber;

    public Crop(Season seasonOfGrowth, boolean canBeGiant, CropType plantType) {
        super(true, "plant", "green");
        this.seasonOfGrowth = seasonOfGrowth;
        this.canBeGiant = canBeGiant;
        this.cropType = plantType;
        stageNumber = 0;
        daysToNextStage = cropType.StageZeroDaysToNextStage;
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
