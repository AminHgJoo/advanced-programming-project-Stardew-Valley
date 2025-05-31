package com.example.models.buildings;

import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public abstract class Building {
    public ArrayList<Cell> buildingCells;

    public Building() {
    }

    public Building(ArrayList<Cell> buildingCells) {
        this.buildingCells = buildingCells;
    }
}
