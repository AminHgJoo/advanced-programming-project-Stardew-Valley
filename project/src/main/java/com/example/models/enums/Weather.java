package com.example.models.enums;

public enum Weather {
    SUNNY(new Season[]{Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER}),
    RAIN(new Season[]{Season.SPRING, Season.SUMMER, Season.AUTUMN}),
    STORM(new Season[]{Season.SPRING, Season.SUMMER, Season.AUTUMN}),
    SNOW(new Season[]{Season.WINTER});

    final public Season[] possibleSeasons;

    Weather(Season[] possibleSeasons) {
        this.possibleSeasons = possibleSeasons;
    }
}
