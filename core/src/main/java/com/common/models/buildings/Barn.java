package com.common.models.buildings;

import com.common.models.Animal;
import com.common.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Barn extends Building {
    public String barnType;
    public ArrayList<Animal> animals = new ArrayList<>();
    public int capacity;

    public Barn() {
    }

    public Barn(ArrayList<Cell> buildingCells, String barnType, int capacity) {
        super(buildingCells);
        this.barnType = barnType;
        this.capacity = capacity;
    }
}
