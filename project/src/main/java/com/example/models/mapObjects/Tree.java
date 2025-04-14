package com.example.models.mapObjects;

import models.enums.TreeStage;
import models.enums.TreeType;

public class Tree {
    private final TreeStage treeGrowthStage;
    private final TreeType treeType;

    public Tree(TreeStage treeGrowthStage, TreeType treeType) {
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
