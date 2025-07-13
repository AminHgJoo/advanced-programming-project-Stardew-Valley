package com.common.models.enums.types.itemTypes;

import com.client.utils.AssetManager;
import com.common.models.Slot;
import com.common.models.enums.Quality;
import com.common.models.items.Tool;

public enum ToolTypes implements ItemType {
    //TODO fishing rod image
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

    public String getName() {
        return name;
    }

    @Override
    public Slot createAmountOfItem(int amount, Quality quality) {
        return new Slot(new Tool(quality, 0, 0, this.name, this, this.waterCapacity), 1);
    }
    @Override
    public String getTextureName() {
        String name = getName();
        if(name.equals("Watering Can Default") || name.equals("Watering Can Copper"))
            return "copperwateringcan";
        if(name.equals("Watering Can Iron"))
            return "steelwateringcan";
        if(name.equals("Watering Can Gold"))
            return "goldwateringcan";
        if(name.equals("Watering Can Iridium"))
            return "iridiumwateringcan";
        if(name.equals("shear"))
            return "shears";
        return AssetManager.generateKeyFromFileName(name);
    }
}
