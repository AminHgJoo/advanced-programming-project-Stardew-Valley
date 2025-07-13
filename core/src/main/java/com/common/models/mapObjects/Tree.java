package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.App;
import com.common.models.enums.types.mapObjectTypes.TreeType;
import com.server.utilities.DateUtility;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;

@Embedded
public class Tree extends MapObject {
    private TreeType treeType;
    private boolean hasBeenWateredToday;
    private int stageNumber = 0;
    private LocalDateTime[] growthDeadLines = new LocalDateTime[5];
    private LocalDateTime harvestDeadLine = null;
    //TODO loading texture ( note that trees have stages )

    public Tree() {
    }

    public Tree(TreeType treeType, LocalDateTime source) {
        super(false, "tree", "green", null);
        this.treeType = treeType;
        growthDeadLines[0] = DateUtility.getLocalDateTime(source, treeType.stageOneTime);
        growthDeadLines[1] = DateUtility.getLocalDateTime(growthDeadLines[0], treeType.stageOneTime);
        growthDeadLines[2] = DateUtility.getLocalDateTime(growthDeadLines[1], treeType.stageTwoTime);
        growthDeadLines[3] = DateUtility.getLocalDateTime(growthDeadLines[2], treeType.stageThreeTime);
        growthDeadLines[4] = DateUtility.getLocalDateTime(growthDeadLines[3], treeType.stageFourTime);
        if (growthDeadLines[0] == null) {
            growthDeadLines[0] = App.getLoggedInUser().getCurrentGame().getDate();
        }
        this.texture = AssetManager.getImage(treeType.textureNames[stageNumber]);

    }

    public LocalDateTime[] getGrowthDeadLines() {
        return growthDeadLines;
    }

    public void pushBackDeadlines(int numOfDays) {
        for (int i = stageNumber; i < growthDeadLines.length; i++) {
            if (growthDeadLines[i] != null) {
                growthDeadLines[i] = DateUtility.getLocalDateTime(growthDeadLines[i], numOfDays);
            }
        }
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

    public LocalDateTime getHarvestDeadLine() {
        return harvestDeadLine;
    }

    public void setHarvestDeadLine(LocalDateTime harvestDeadLine) {
        this.harvestDeadLine = harvestDeadLine;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
