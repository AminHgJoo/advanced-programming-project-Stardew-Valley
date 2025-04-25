package com.example.models.buildings;

import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class PlayerHome extends Building {
    public PlayerHome() {
    }

    public PlayerHome(ArrayList<Cell> buildingCells) {
        super(buildingCells);
    }
}
