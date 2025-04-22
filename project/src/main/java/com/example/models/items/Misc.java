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

    public Misc(int value, String name, MiscType type) {
        super(Quality.DEFAULT, Integer.MAX_VALUE, value, 0, name);
        this.type = type;
    }
}
