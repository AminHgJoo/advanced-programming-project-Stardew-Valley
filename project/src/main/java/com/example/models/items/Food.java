package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.ConsumableType;
import com.example.models.enums.types.FishType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Food extends Item {
    public boolean isEdible;
    public ConsumableType consumableType;

    public Food() {
        super();
        isEdible = false;
        consumableType = null;
    }

    public Food(Quality quality, int maxStackSize, int value, double energyCost, String name, ConsumableType consumableType, boolean isEdible) {
        this.isEdible = isEdible;
        this.quality = quality;
        this.maxStackSize = maxStackSize;
        this.value = value;
        this.energyCost = energyCost;
        this.name = name;
        this.consumableType = consumableType;
    }

    @Override
    public int getValue() {
        if (consumableType instanceof FishType) {
            if (this.quality == Quality.SILVER)
                return (int) ((double) this.value * 1.25);
            else if (this.quality == Quality.GOLD)
                return (int) ((double) this.value * 1.5);
            else if (this.quality == Quality.IRIDIUM)
                return (int) ((double) this.value * 2);
        }
        return value;
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
