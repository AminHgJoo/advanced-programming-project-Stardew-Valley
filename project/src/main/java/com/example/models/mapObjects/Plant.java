package com.example.models.mapObjects;

import com.example.models.enums.Season;
import com.example.models.enums.types.PlantType;

public class Plant extends MapObject {
    public final Season seasonOfGrowth;
    public final boolean canBeGiant;
    public final PlantType plantType;

    public Plant(Season seasonOfGrowth, boolean canBeGiant, PlantType plantType) {
        super(true, "plant");
        this.seasonOfGrowth = seasonOfGrowth;
        this.canBeGiant = canBeGiant;
        this.plantType = plantType;
    }
}
