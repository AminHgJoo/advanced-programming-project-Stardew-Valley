package com.common.models.items;

import com.client.utils.AssetManager;
import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingMineralItem extends Item {
    private ForagingMineralsType type;

    public ForagingMineralItem() {
        super();
    }

    public ForagingMineralItem(Quality quality, ForagingMineralsType type) {
        super(quality, Integer.MAX_VALUE, type.getSellPrice(), 0, type.name);
        this.type = type;
        this.texture = AssetManager.getImage(type.getTextureName());
    }

    public ForagingMineralItem(Quality quality, ForagingMineralsType type, int sellPrice) {
        super(quality, Integer.MAX_VALUE, sellPrice, 0, type.name);
        this.type = type;
        this.texture = AssetManager.getImage(type.getTextureName());
    }

    public ForagingMineralsType getType() {
        return type;
    }

    public void setType(ForagingMineralsType type) {
        this.type = type;
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

}
