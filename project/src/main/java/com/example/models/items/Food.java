package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.items.buffs.FoodBuff;
import dev.morphia.annotations.Embedded;

@Embedded
public class Food extends Item {
    public FoodTypes foodTypes;
    public FoodBuff foodBuff;

    public Food() {
        super();
    }

    /// To be used in enums only!
    public Food(FoodTypes foodTypes) {
    }

    public Food(Quality quality, FoodTypes foodTypes) {
        super(quality, Integer.MAX_VALUE, foodTypes.value, -foodTypes.energy, foodTypes.name);
        this.foodTypes = foodTypes;
        this.foodBuff = foodTypes.foodBuff;
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
