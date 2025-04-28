package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.ForagingMineralsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingMineral extends Item {
    private ForagingMineralsType type;

    public void setType(ForagingMineralsType type) {
        this.type = type;
    }

    public ForagingMineralsType getType() {
        return type;
    }

    public ForagingMineral() {
        super();
        type = null;
    }

    public ForagingMineral(Quality quality, ForagingMineralsType type) {
        super(quality, Integer.MAX_VALUE, type.getSellPrice(), 0, type.name);
        this.type = type;
    }

    public ForagingMineral(Quality quality, ForagingMineralsType type, int sellPrice) {
        super(quality, Integer.MAX_VALUE, sellPrice, 0, type.name);
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
