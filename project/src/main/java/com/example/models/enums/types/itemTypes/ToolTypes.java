package com.example.models.enums.types.itemTypes;

import com.example.models.Slot;
import com.example.models.enums.Quality;
import com.example.models.items.Tool;

public enum ToolTypes implements ItemType {
    HOE(0, "Hoe"),
    PICKAXE(0, "Pickaxe"),
    AXE(0, "Axe"),
    WATERING_CAN_DEFAULT(40, "Watering Can Default"),
    WATERING_CAN_COPPER(55, "Watering Can Copper"),
    WATERING_CAN_IRON(70, "Watering Can Iron"),
    WATERING_CAN_GOLD(85, "Watering Can Gold"),
    WATERING_CAN_IRIDIUM(100, "Watering Can Iridium"),
    FISHING_ROD(0, "Fishing Rod"),
    SCYTHE(0, "Scythe"),
    MILK_PAIL(0, "Milk Pail"),
    SHEAR(0, "Shear"),
    ;

    public final int waterCapacity;
    public final String name;

    ToolTypes(int waterCapacity, String name) {
        this.waterCapacity = waterCapacity;
        this.name = name;
    }


    @Override
    public Slot createAmountOfItem(int amount) {
        return new Slot(new Tool(Quality.DEFAULT, 0, 0, this.name, this, this.waterCapacity), 1);
    }
}
