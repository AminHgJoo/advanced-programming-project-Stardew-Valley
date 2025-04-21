package com.example.models.items;

import com.example.models.enums.Quality;
import dev.morphia.annotations.Embedded;

@Embedded
abstract public class Item {
    protected Quality quality;
    protected int maxStackSize;
    protected int value;
    protected double energyCost;
    protected String name;

    public Item(Quality quality, int maxStackSize, int value, double energyCost, String name) {
        this.quality = quality;
        this.maxStackSize = maxStackSize;
        this.value = value;
        this.energyCost = energyCost;
        this.name = name;
    }

    abstract public void useItem();

    abstract public void deleteItem();

    abstract public void dropItem();

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
