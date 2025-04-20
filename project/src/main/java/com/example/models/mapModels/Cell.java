package com.example.models.mapModels;

import com.example.models.mapObjects.MapObject;
import dev.morphia.annotations.Embedded;

@Embedded
public class Cell {
    //TODO : modify
    private MapObject objectOnCell;
    private Coordinate coordinate;
    private boolean isTilled;

    // for walking
    public int distance = 0;
    public double energy = 0;
    public int turns = 0;
    public Cell prev = null;

    public Cell(MapObject objectOnCell, Coordinate coordinate) {
        this.objectOnCell = objectOnCell;
        this.coordinate = coordinate;
        this.isTilled = false;
    }

    public int diffXPrev() {
        return this.coordinate.getX() - this.prev.coordinate.getX();
    }

    public int diffYPrev() {
        return this.coordinate.getY() - this.prev.coordinate.getY();
    }

    public void setEnergy() {
        energy = distance + 10 * turns;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public MapObject getObjectOnCell() {
        return objectOnCell;
    }

    public void setObjectOnCell(MapObject objectOnCell) {
        this.objectOnCell = objectOnCell;
    }

    public boolean isTilled() {
        return isTilled;
    }

    public void setTilled(boolean tilled) {
        isTilled = tilled;
    }

    @Override
    public Cell clone() {
        return new Cell(objectOnCell, coordinate);
    }
}
