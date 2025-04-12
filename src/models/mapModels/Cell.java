package models.mapModels;

public class Cell {
    private Object objectOnCell;
    private Coordinate coordinate;

    public Cell(Object objectOnCell, Coordinate coordinate) {
        this.objectOnCell = objectOnCell;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Object getObjectOnCell() {
        return objectOnCell;
    }

    public void setObjectOnCell(Object objectOnCell) {
        this.objectOnCell = objectOnCell;
    }
}
