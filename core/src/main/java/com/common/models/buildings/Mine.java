package com.common.models.buildings;

import com.common.models.mapModels.Cell;
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
