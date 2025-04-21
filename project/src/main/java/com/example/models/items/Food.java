package com.example.models.items;

import com.example.models.enums.types.FoodTypes;
import dev.morphia.annotations.Embedded;

@Embedded
public class Food extends Item {
    public boolean isEdible;
    public FoodTypes foodType;

    public Food() {
    }

    public Food(boolean isEdible, FoodTypes foodType) {
        this.isEdible = isEdible;
        this.foodType = foodType;
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
