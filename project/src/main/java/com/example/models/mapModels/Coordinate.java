package com.example.models.mapModels;

import dev.morphia.annotations.Embedded;

@Embedded
public class Coordinate {
    private int x;
    private int y;

    public Coordinate() {

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

    /// Eureka Indeed!
    public static double calculateEuclideanDistance(Cell cell1, Cell cell2) {
        Coordinate a = cell1.getCoordinate(), b = cell2.getCoordinate();

        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }
}
