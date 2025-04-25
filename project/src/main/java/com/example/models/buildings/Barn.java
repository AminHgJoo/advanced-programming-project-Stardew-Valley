package com.example.models.buildings;

import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Barn extends Building {
    public Barn() {
    }

    public Barn(ArrayList<Cell> buildingCells) {
        super(buildingCells);
    }
}
