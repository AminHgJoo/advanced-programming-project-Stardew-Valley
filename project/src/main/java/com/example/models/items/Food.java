package com.example.models.items;

import com.example.models.enums.types.FoodTypes;

public class Food extends Item {
    public final boolean isEdible;
    public final FoodTypes foodType;

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
