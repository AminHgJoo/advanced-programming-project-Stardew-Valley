package com.example.models.mapModels;

import dev.morphia.annotations.Embedded;

@Embedded
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(){

    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
