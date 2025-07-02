package com.common.models.buildings;

import com.common.models.Animal;
import com.common.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Coop extends Building {
    public String coopType;
    public ArrayList<Animal> animals = new ArrayList<>();
    public int capacity;

    public Coop() {
    }

    public Coop(ArrayList<Cell> buildingCells, String coopType, int capacity) {
        super(buildingCells);
        this.coopType = "Coop";
        this.capacity = capacity;
    }
}
