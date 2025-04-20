package com.example.models.buildings;

import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Greenhouse extends Building {
    public Greenhouse(){}
    public Greenhouse(ArrayList<Cell> buildingCells) {
        super(buildingCells);
    }
}
