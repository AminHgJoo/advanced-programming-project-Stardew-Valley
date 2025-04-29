package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.ToolTypes;
import dev.morphia.annotations.Embedded;

@Embedded
public class Tool extends Item {
    private ToolTypes type;
    private int waterReserve;

    public Tool() {
        super();
        type = null;
    }

    public Tool(Quality quality, int value, double energyCost, String name, ToolTypes type, int waterReserve) {
        super(quality, 1, value, energyCost, name);
        this.type = type;
        this.waterReserve = waterReserve;
    }

    public Tool(Quality quality, ToolTypes type, int price) {
        super(quality, 1, 0, 5, type.name);
        this.type = type;
        this.waterReserve = type.waterCapacity;
    }

    /// Only in blacksmith shop.
    public void upgradeTool() {

    }

    @Override
    public void useItem() {

    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void dropItem() {

    }

    public ToolTypes getType() {
        return type;
    }

    public int getWaterReserve() {
        return waterReserve;
    }

    public void setWaterReserve(int waterReserve) {
        this.waterReserve = waterReserve;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }
}
