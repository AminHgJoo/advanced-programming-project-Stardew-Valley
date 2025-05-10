package com.example.models.mapObjects;

import com.example.models.App;
import com.example.models.enums.types.itemTypes.CropSeedsType;
import com.example.utilities.DateUtility;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;

// TODO CROP STAGE
@Embedded
public class Crop extends MapObject {
    public CropSeedsType cropSeedsType;
    private int stageNumber;
    private boolean hasBeenWateredToday = false;
    private boolean hasBeenFertilized = false;
    private LocalDateTime[] growthDeadLines = new LocalDateTime[5];

    public Crop() {
        super();
    }

    public void pushBackDeadlines(int numOfDays) {
        for (int i = stageNumber; i < growthDeadLines.length; i++) {
            if (growthDeadLines[i] != null) {
                growthDeadLines[i] = DateUtility.getLocalDate(growthDeadLines[i], numOfDays);
            }
        }
    }

    public Crop(CropSeedsType plantType , LocalDateTime source) {
        super(true, "plant", "green");
        this.cropSeedsType = plantType;
        stageNumber = 0;
        growthDeadLines[0] = DateUtility.getLocalDate(source, cropSeedsType.stageZeroDaysToNextStage);
        growthDeadLines[1] = DateUtility.getLocalDate(growthDeadLines[0], cropSeedsType.stageOneDaysToNextStage);
        growthDeadLines[2] = DateUtility.getLocalDate(growthDeadLines[1], cropSeedsType.stageTwoDaysToNextStage);
        growthDeadLines[3] = DateUtility.getLocalDate(growthDeadLines[2], cropSeedsType.stageThreeDaysToNextStage);
        growthDeadLines[4] = DateUtility.getLocalDate(growthDeadLines[3], cropSeedsType.stageFourDaysToNextStage);
        this.hasBeenWateredToday = false;
    }

    public LocalDateTime[] getGrowthDeadLines() {
        return growthDeadLines;
    }

    public boolean isHasBeenWateredToday() {
        return hasBeenWateredToday;
    }

    public void setHasBeenWateredToday(boolean hasBeenWateredToday) {
        this.hasBeenWateredToday = hasBeenWateredToday;
    }

    public int getDaysToNextStage() {
        LocalDateTime deadLine = growthDeadLines[stageNumber];

        if (deadLine == null) {
            return -1;
        }

        LocalDateTime timeNow = App.getLoggedInUser().getCurrentGame().getDate();

        int yearDifference = deadLine.getYear() - timeNow.getYear();
        int monthDifference = deadLine.getMonthValue() - timeNow.getMonthValue();
        int dayDifference = deadLine.getDayOfMonth() - timeNow.getDayOfMonth();

        return yearDifference * 336 + monthDifference * 28 + dayDifference;
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
        builder.append("name : ").append(cropSeedsType.name).append("\n");
        builder.append("time to next stage : ").append(getDaysToNextStage()).append("\n");
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
