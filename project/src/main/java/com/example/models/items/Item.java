package com.example.models.items;

import models.enums.Quality;

abstract public class Item {
    protected Quality quality;
    protected int maxStackSize;
    protected int value;
    protected double energyCost;
    protected String name;

    abstract public void useItem();

    abstract public void deleteItem();

    abstract public void dropItem();
}
