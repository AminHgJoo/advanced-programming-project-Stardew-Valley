package models.mapModels;

import models.buildings.Building;

import java.util.ArrayList;

public class Farm {
    final private ArrayList<Cell> cells;
    final private ArrayList<Building> buildings;

    public Farm(ArrayList<Cell> cells, ArrayList<Building> buildings) {
        this.cells = cells;
        this.buildings = buildings;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }
}
