package com.example.models.mapObjects;

import com.example.models.enums.types.CropType;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;

// TODO CROP STAGE
@Embedded
public class Crop extends MapObject {
    public  CropType cropType;
    private int daysToNextStage;
    private int stageNumber;
    private boolean hasBeenWateredToday = false;
    private boolean hasBeenFertilized = false;
    public Crop(){
        super();
    }

    public Crop(CropType plantType) {
        super(true, "plant", "green");
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name : ").append(cropType.name).append("\n");
        builder.append("tim to next stage : ").append(daysToNextStage).append("\n");
        builder.append("stage number : ").append(stageNumber).append("\n");
        builder.append("has been watered today : ").append(hasBeenWateredToday).append("\n");
        builder.append("has been fertilized : ").append(hasBeenFertilized).append("\n");
        return builder.toString();
    }

    public boolean isHasBeenFertilized() {
        return hasBeenFertilized;
    }

    public void setHasBeenFertilized(boolean hasBeenFertilized) {
        this.hasBeenFertilized = hasBeenFertilized;
    }
}
