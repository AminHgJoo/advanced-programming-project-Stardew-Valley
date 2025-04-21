package com.example.models.enums;

public enum Season {
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall"),
    WINTER("Winter");

    private final String name;

    Season(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
