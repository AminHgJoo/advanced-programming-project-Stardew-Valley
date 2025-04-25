package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.MiscType;
import com.example.utilities.ItemLambda;
import dev.morphia.annotations.Embedded;

@Embedded
public class Misc extends Item {
    private MiscType miscType;

    @Override
    public void useItem() {

    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void dropItem() {

    }


    public Misc() {
        super();
        miscType = null;
    }

    public Misc(int value, MiscType miscType) {
        super(Quality.DEFAULT, Integer.MAX_VALUE, value, 0, miscType.name);
        this.miscType = miscType;
    }

    public MiscType getMiscType() {
        return miscType;
    }

    public void setMiscType(MiscType miscType) {
        this.miscType = miscType;
    }
}
