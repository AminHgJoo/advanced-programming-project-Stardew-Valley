package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.ToolTypes;

public class Tool extends Item {
    private ToolTypes type;

    public Tool(Quality quality, int value, double energyCost, String name, ToolTypes type) {
        super(quality, 1, value, energyCost, name);
        this.type = type;
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
}
