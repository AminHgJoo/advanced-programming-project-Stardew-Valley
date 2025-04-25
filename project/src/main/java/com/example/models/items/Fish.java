package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.FishType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Fish extends Item {
    private FishType fishType;

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

    public Fish() {
    }

    public Fish(Quality quality, int maxStackSize, int value, double energyCost, String name, FishType fishType) {
        super(quality, maxStackSize, value, energyCost, name);
        this.fishType = fishType;
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
