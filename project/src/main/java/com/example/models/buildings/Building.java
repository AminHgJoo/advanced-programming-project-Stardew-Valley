package com.example.models.buildings;

import com.example.models.mapModels.Cell;

import java.util.ArrayList;

public abstract class Building {
    public final ArrayList<Cell> buildingCells;

    protected Building(ArrayList<Cell> buildingCells) {
        this.buildingCells = buildingCells;
    }
}
