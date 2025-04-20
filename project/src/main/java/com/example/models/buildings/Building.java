package com.example.models.buildings;

import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public abstract class Building {
    public final ArrayList<Cell> buildingCells;

    protected Building(ArrayList<Cell> buildingCells) {
        this.buildingCells = buildingCells;
    }
}
