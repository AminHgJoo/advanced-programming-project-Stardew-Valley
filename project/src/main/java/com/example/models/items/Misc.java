package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.MiscType;

public class Misc extends Item {
    private MiscType type;

    @Override
    public void useItem() {

    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void dropItem() {

    }

    public Misc(Quality quality, int maxStackSize, int value, double energyCost, String name, MiscType type) {
        super(quality, maxStackSize, value, energyCost, name);
        this.type = type;
    }
}
