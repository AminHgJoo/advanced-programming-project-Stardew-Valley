package com.common.models.items;

import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.ToolTypes;
import dev.morphia.annotations.Embedded;

@Embedded
public class Tool extends Item {
    private ToolTypes type;
    private int waterReserve;

    public Tool() {
        super();
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

    @Override
    public String toString() {
        if (type == ToolTypes.WATERING_CAN_DEFAULT ||
                type == ToolTypes.WATERING_CAN_COPPER ||
                type == ToolTypes.WATERING_CAN_IRON ||
                type == ToolTypes.WATERING_CAN_GOLD ||
                type == ToolTypes.WATERING_CAN_IRIDIUM) {
            return name;
        }
        if (type == ToolTypes.FISHING_ROD) {
            String qualityStr;
            switch (quality) {
                case COPPER -> qualityStr = "Training";
                case SILVER -> qualityStr = "Bamboo";
                case GOLD -> qualityStr = "Fiberglass";
                case IRIDIUM -> qualityStr = "Iridium";
                default -> qualityStr = null;
            }
            return qualityStr + " " + name;
        }
        return this.quality.toString() + " " + this.name;
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

    @Override
    public String getName() {
        return this.toString();
    }
}
