package com.example.models.buildings;

import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Coop extends Building {
    public Coop() {
    }

    public Coop(ArrayList<Cell> buildingCells) {
        super(buildingCells);
    }
}
