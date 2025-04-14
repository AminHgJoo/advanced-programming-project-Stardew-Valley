package com.example.models.mapObjects;

import models.enums.PlantType;
import models.enums.Season;

public class Plant {
    public final Season seasonOfGrowth;
    public final boolean canBeGiant;
    public final PlantType plantType;

    public Plant(Season seasonOfGrowth, boolean canBeGiant, PlantType plantType) {
        this.seasonOfGrowth = seasonOfGrowth;
        this.canBeGiant = canBeGiant;
        this.plantType = plantType;
    }
}
