package com.common.models.enums.types;

import com.common.models.items.Item;

import java.util.ArrayList;

public enum AnimalType {
    Chicken(1, 4, 800),
    DUCK(2, 8, 1200),
    RABBIT(4, 12, 8000),
    DINOSAUR(7, 8, 14000),
    COW(1, 4, 1500),
    GOAT(2, 8, 4000),
    SHEEP(3, 12, 8000),
    PIG(1, 12, 16000),
    ;

    public final int productPerDay;
    public final int capacity;
    public final int price;
    public final ArrayList<Item> products = new ArrayList<>();

    AnimalType(int productPerDay, int capacity, int price) {
        this.productPerDay = productPerDay;
        this.capacity = capacity;
        this.price = price;
    }
}
