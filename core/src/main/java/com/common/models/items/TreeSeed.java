package com.common.models.items;

import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.TreeSeedsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class TreeSeed extends Item {
    private TreeSeedsType treeSeedsType;

    public TreeSeed() {
    }

    @Override
    public void useItem() {

    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void dropItem() {

    }

    public TreeSeed(TreeSeedsType treeSeedsType) {
        super(Quality.DEFAULT, Integer.MAX_VALUE, treeSeedsType.value, 0, treeSeedsType.name);
        this.treeSeedsType = treeSeedsType;
    }

    public TreeSeedsType getTreeSeedsType() {
        return treeSeedsType;
    }

    public void setTreeSeedsType(TreeSeedsType treeSeedsType) {
        this.treeSeedsType = treeSeedsType;
    }
}
