package com.example.models.mapObjects;

import com.example.models.enums.types.TreeType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Tree extends MapObject {
    private TreeType treeType;
    private boolean hasBeenWateredToday;

    public Tree() {}

    public Tree( TreeType treeType) {
        super(false, "tree", "green");
        this.treeType = treeType;
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
}
