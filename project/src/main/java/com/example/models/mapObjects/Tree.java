package com.example.models.mapObjects;

import com.example.models.enums.types.TreeType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Tree extends MapObject {
    private TreeType treeType;

    public Tree() {}

    public Tree( TreeType treeType) {
        super(false, "tree", "green");
        this.treeType = treeType;
    }

    public TreeType getTreeType() {
        return treeType;
    }
}
