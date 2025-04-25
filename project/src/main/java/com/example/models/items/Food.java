package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.FoodTypes;
import dev.morphia.annotations.Embedded;

@Embedded
public class Food extends Item {
    public boolean isEdible;
    public FoodTypes foodTypes;
    public FoodBuff foodBuff;

    public Food() {
        super();
        isEdible = false;
    }

    public Food(Quality quality, int value, double energyCost, String name, boolean isEdible, FoodTypes foodTypes, FoodBuff foodBuff) {
        super(quality, Integer.MAX_VALUE, value, energyCost, name);
        this.isEdible = isEdible;
        this.foodTypes = foodTypes;
        this.foodBuff = foodBuff;
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
