package com.example.models.enums.types;

import com.example.models.items.Item;

import java.util.ArrayList;

public enum AnimalType {
    Chicken(1, 4),
    DUCK(2, 8),
    RABBIT(4, 12),
    DINOSAUR(7, 8),
    COW(1, 4),
    GOAT(2, 8),
    SHEEP(3, 12),
    PIG(-1, 12),
    ;

    public final int productPerDay;
    public final int capacity;
    public final ArrayList<Item> products = new ArrayList<>();

    AnimalType(int productPerDay, int capacity) {
        this.productPerDay = productPerDay;
        this.capacity = capacity;
    }
}
