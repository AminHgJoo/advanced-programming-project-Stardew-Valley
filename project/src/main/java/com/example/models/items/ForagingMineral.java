package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.ForagingMineralsType;

public class ForagingMineral extends Item {
    private ForagingMineralsType type;

    public ForagingMineralsType getType() {
        return type;
    }

    public ForagingMineral(Quality quality, int value, ForagingMineralsType type) {
        super(quality, Integer.MAX_VALUE, value, 0, type.name);
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
