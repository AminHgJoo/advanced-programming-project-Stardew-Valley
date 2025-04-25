package com.example.models.enums.types;

public enum MiscType implements ItemType {
    WOOD("Wood", 0),
    FIBER("Fiber", 0),
    FERTILIZER("Fertilizer", 0);

    final public String name;
    final public int value;

    MiscType(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
