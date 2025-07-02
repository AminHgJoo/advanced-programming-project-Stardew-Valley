package com.common.models.buildings;

import com.common.models.mapModels.Cell;
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
