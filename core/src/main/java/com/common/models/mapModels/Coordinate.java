package com.common.models.mapModels;

import dev.morphia.annotations.Embedded;

@Embedded
public class Coordinate {
    private float x;
    private float y;

    public Coordinate() {

    }

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /// Eureka Indeed!
    public static double calculateEuclideanDistance(Cell cell1, Cell cell2) {
        Coordinate a = cell1.getCoordinate(), b = cell2.getCoordinate();

        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }

    public static double calculateEuclideanDistance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
