package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.App;
import com.common.models.enums.types.itemTypes.CropSeedsType;
import com.server.utilities.DateUtility;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;

@Embedded
public class Crop extends MapObject {
    public CropSeedsType cropSeedsType;
    private int stageNumber;
    private boolean hasBeenWateredToday = false;
    private boolean hasBeenDeluxeFertilized = false;
    private LocalDateTime[] growthDeadLines = new LocalDateTime[5];
    private LocalDateTime harvestDeadLine = null;
    private LocalDateTime lastWateringDate = null;
    private boolean isGiant = false;

    public Crop() {
        super();
    }

    public Crop(CropSeedsType plantType, LocalDateTime source) {
        super(true, "plant", "green", null);
        this.cropSeedsType = plantType;
        this.lastWateringDate = source;
        stageNumber = 0;
        growthDeadLines[0] = DateUtility.getLocalDateTime(source, cropSeedsType.stageZeroDaysToNextStage);
        growthDeadLines[1] = DateUtility.getLocalDateTime(growthDeadLines[0], cropSeedsType.stageOneDaysToNextStage);
        growthDeadLines[2] = DateUtility.getLocalDateTime(growthDeadLines[1], cropSeedsType.stageTwoDaysToNextStage);
        growthDeadLines[3] = DateUtility.getLocalDateTime(growthDeadLines[2], cropSeedsType.stageThreeDaysToNextStage);
        growthDeadLines[4] = DateUtility.getLocalDateTime(growthDeadLines[3], cropSeedsType.stageFourDaysToNextStage);
        this.hasBeenWateredToday = false;
        harvestDeadLine = null;
        this.texture = AssetManager.getImage(cropSeedsType.getTextureName());
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            this.texture = AssetManager.getImage(cropSeedsType.getStagedTexture(stageNumber));
        }
        if (isGiant) {
            this.texture = AssetManager.getImage("giant" + cropSeedsType.getTextureName());
        }
        return texture;
    }

    public void pushBackDeadlines(int numOfDays) {
        for (int i = stageNumber; i < growthDeadLines.length; i++) {
            if (growthDeadLines[i] != null) {
                if (numOfDays > 0)
                    growthDeadLines[i] = DateUtility.getLocalDateTime(growthDeadLines[i], numOfDays);
                else {
                    growthDeadLines[i] = growthDeadLines[i].minusDays(-numOfDays);
                    while (growthDeadLines[i].getDayOfMonth() >= 29) {
                        growthDeadLines[i] = growthDeadLines[i].minusDays(1);
                    }
                }
            }
        }
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

    public String cropDesc() {
        StringBuilder builder = new StringBuilder();
        builder.append("name : ").append(cropSeedsType.name).append("\n");
        builder.append("time to next stage : ").append(getDaysToNextStage()).append("\n");
        builder.append("stage number : ").append(stageNumber).append("\n");
        builder.append("has been watered today : ").append(hasBeenWateredToday).append("\n");
        builder.append("has been fertilized : ").append(hasBeenDeluxeFertilized).append("\n");
        return builder.toString();
    }


    public boolean isHasBeenDeluxeFertilized() {
        return hasBeenDeluxeFertilized;
    }

    public void setHasBeenDeluxeFertilized(boolean hasBeenDeluxeFertilized) {
        this.hasBeenDeluxeFertilized = hasBeenDeluxeFertilized;
    }

    public LocalDateTime getHarvestDeadLine() {
        return harvestDeadLine;
    }

    public void setHarvestDeadLine(LocalDateTime harvestDeadLine) {
        this.harvestDeadLine = harvestDeadLine;
    }

    public boolean isGiant() {
        return isGiant;
    }

    public void setGiant(boolean giant) {
        isGiant = giant;
    }

    public LocalDateTime getLastWateringDate() {
        return lastWateringDate;
    }

    public void setLastWateringDate(LocalDateTime lastWateringDate) {
        this.lastWateringDate = lastWateringDate;
    }
}
