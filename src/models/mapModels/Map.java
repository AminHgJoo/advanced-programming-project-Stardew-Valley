package models.mapModels;

import java.util.ArrayList;

public class Map {
    private final ArrayList<Farm> farms;
    private final Village village;

    public Map(ArrayList<Farm> farms, Village village) {
        this.farms = farms;
        this.village = village;
    }

    public ArrayList<Farm> getFarms() {
        return farms;
    }

    public Village getVillage() {
        return village;
    }
}
