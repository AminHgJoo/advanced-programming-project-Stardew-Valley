package com.common.models.items;

import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.FishType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Fish extends Item {
    private FishType fishType;

    public Fish() {
    }

    public Fish(Quality quality, FishType fishType) {
        super(quality, Integer.MAX_VALUE, fishType.price, 0, fishType.name);
        this.fishType = fishType;
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

    public FishType getFishType() {
        return fishType;
    }

    @Override
    public int getValue() {
        if (this.quality == Quality.SILVER)
            return (int) ((double) this.value * 1.25);
        else if (this.quality == Quality.GOLD)
            return (int) ((double) this.value * 1.5);
        else if (this.quality == Quality.IRIDIUM)
            return (int) ((double) this.value * 2);

        return value;
    }
}
