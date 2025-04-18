package com.example.models.mapModels;

import java.util.ArrayList;

public class Map {
    private final ArrayList<Farm> farms;
    private final Village village;

    private Map(ArrayList<Farm> farms, Village village) {
        this.farms = farms;
        this.village = village;
    }

    public static Map makeMap() {
        ArrayList<Farm> farms = new ArrayList<>();
        farms.add(Farm.makeFarm());
        farms.add(Farm.makeFarm());
        farms.add(Farm.makeFarm());
        farms.add(Farm.makeFarm());
        Village village = new Village();
        return new Map(farms, village);
    }

    public ArrayList<Farm> getFarms() {
        return farms;
    }

    public Village getVillage() {
        return village;
    }
}
