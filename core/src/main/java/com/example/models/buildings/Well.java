package com.example.models.buildings;


import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Well extends Building {
    public String wellType;
    public ArrayList<Cell> buildingCells;

    public Well() {
    }
}
