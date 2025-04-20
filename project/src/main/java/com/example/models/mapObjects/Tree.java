package com.example.models.mapObjects;

import com.example.models.enums.TreeStage;
import com.example.models.enums.types.TreeType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Tree extends MapObject {
    private TreeStage treeGrowthStage;
    private TreeType treeType;

    public Tree(){}
    public Tree(TreeStage treeGrowthStage, TreeType treeType) {
        super(false, "tree", "green");
        this.treeGrowthStage = treeGrowthStage;
        this.treeType = treeType;
    }

    public TreeStage getTreeGrowthStage() {
        return treeGrowthStage;
    }

    public TreeType getTreeType() {
        return treeType;
    }
}
