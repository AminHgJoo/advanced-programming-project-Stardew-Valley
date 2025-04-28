package com.example.models.mapObjects;

import com.example.models.enums.types.mapObjectTypes.TreeType;
import com.example.utilities.DateUtility;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;

@Embedded
public class Tree extends MapObject {
    private TreeType treeType;
    private boolean hasBeenWateredToday;
    private int stageNumber = 0;
    private LocalDateTime[] growthDeadLines = new LocalDateTime[5];


    public LocalDateTime[] getGrowthDeadLines() {
        return growthDeadLines;
    }
    public Tree() {
    }
    public Tree(TreeType treeType , LocalDateTime source) {
        super(false, "tree", "green");
        this.treeType = treeType;
        growthDeadLines[0] = DateUtility.getLocalDate(source, treeType.stageOneTime);
        growthDeadLines[1] = DateUtility.getLocalDate(growthDeadLines[0], treeType.stageOneTime);
        growthDeadLines[2] = DateUtility.getLocalDate(growthDeadLines[1], treeType.stageTwoTime);
        growthDeadLines[3] = DateUtility.getLocalDate(growthDeadLines[2], treeType.stageThreeTime);
        growthDeadLines[4] = DateUtility.getLocalDate(growthDeadLines[3], treeType.stageFourTime);
    }

    public TreeType getTreeType() {
        return treeType;
    }

    public boolean isHasBeenWateredToday() {
        return hasBeenWateredToday;
    }

    public void setHasBeenWateredToday(boolean hasBeenWateredToday) {
        this.hasBeenWateredToday = hasBeenWateredToday;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
    }
}
