package com.common.models.buildings;

import com.common.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Building {
    public ArrayList<Cell> buildingCells;

    public Building() {
    }

    public Building(ArrayList<Cell> buildingCells) {
        this.buildingCells = buildingCells;
    }
}
