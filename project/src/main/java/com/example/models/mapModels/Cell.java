package com.example.models.mapModels;

import com.example.models.mapObjects.MapObject;

public class Cell {
    //TODO : modify
    private MapObject objectOnCell;
    private Coordinate coordinate;
    private boolean isTilled;

    public Cell(MapObject objectOnCell, Coordinate coordinate) {
        this.objectOnCell = objectOnCell;
        this.coordinate = coordinate;
        this.isTilled = false;
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
}
