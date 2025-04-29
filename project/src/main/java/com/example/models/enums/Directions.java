package com.example.models.enums;

import com.example.models.mapModels.Coordinate;

public enum Directions {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(1, -1),
    DOWN_LEFT(-1, 1),
    DOWN_RIGHT(1, 1),
    ;

    private int[] vector = new int[2];

    Directions(int x, int y) {
        vector[0] = x;
        vector[1] = y;
    }

    public int[] getVector() {
        return vector;
    }

    public Coordinate getCoordinate(Coordinate coordinate) {
        return new Coordinate(coordinate.getX() + vector[0], coordinate.getY() + vector[1]);
    }

    public static Directions getDir(String name){
        for(Directions dir : Directions.values()){
            if(dir.name().compareToIgnoreCase(name) == 0){
                return dir;
            }
        }
        return null;
    }
}
