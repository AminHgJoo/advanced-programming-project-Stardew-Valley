package com.example.models.enums.types;

public enum ToolTypes implements ItemType {
    HOE(0),
    PICKAXE(0),
    AXE(0),
    WATERING_CAN_DEFAULT(40),
    WATERING_CAN_COPPER(55),
    WATERING_CAN_IRON(70),
    WATERING_CAN_GOLD(85),
    WATERING_CAN_IRIDIUM(100),
    FISHING_ROD(0),
    SCYTHE(0),
    MILK_PAIL(0),
    SHEAR(0);

    public final int waterCapacity;

    ToolTypes(int waterCapacity) {
        this.waterCapacity = waterCapacity;
    }
}
