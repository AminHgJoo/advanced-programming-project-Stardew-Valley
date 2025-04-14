package com.example.models.mapObjects;

import com.example.models.enums.TreeStage;
import com.example.models.enums.types.TreeType;

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
