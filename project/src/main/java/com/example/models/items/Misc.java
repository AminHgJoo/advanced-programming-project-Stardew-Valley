package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.MiscType;
import dev.morphia.annotations.Embedded;

@Embedded
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

    public Misc(){
        super();
        type = null;
    }

    public Misc(int value, String name, MiscType type) {
        super(Quality.DEFAULT, Integer.MAX_VALUE, value, 0, name);
        this.type = type;
    }

    public MiscType getType() {
        return type;
    }

    public void setType(MiscType type) {
        this.type = type;
    }
}
