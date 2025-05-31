package com.example.models.buildings;

import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Mine extends Building {
    public Mine() {
    }

    public Mine(ArrayList<Cell> buildingCells) {
        super(buildingCells);
    }
}
