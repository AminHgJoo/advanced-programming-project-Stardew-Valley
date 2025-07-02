package com.common.utilities;

import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapObjects.MapObject;

public class Tile {
    // for walking
    public int distance = 0;
    public double energy = 0;
    public int turns = 0;
    public Tile prev = null;
    private MapObject objectOnCell;
    private Coordinate coordinate;
    private boolean isTilled;

    public Tile(Cell cell) {
        this.coordinate = cell.getCoordinate();
        this.isTilled = cell.isTilled();
        this.objectOnCell = cell.getObjectOnCell();
    }

    public Tile(MapObject objectOnCell, Coordinate coordinate) {
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
    public Tile clone() {
        return new Tile(objectOnCell, coordinate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile cell = (Tile) o;
        return coordinate.getX() == cell.coordinate.getX() && coordinate.getY() == cell.coordinate.getY();
    }
}
