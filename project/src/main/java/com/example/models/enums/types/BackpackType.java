package com.example.models.enums.types;

public enum BackpackType {
    DEFAULT(12, "Default"),
    GIANT(24, "Giant"),
    DELUXE(Double.POSITIVE_INFINITY, "Deluxe"),
    ;

    final private double maxCapacity;
    final private String name;

    BackpackType(double maxCapacity, String name) {
        this.maxCapacity = maxCapacity;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }
}
