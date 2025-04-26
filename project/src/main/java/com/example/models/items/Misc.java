package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.MiscType;
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

    public Misc(MiscType miscType, Quality quality) {
        super(quality, Integer.MAX_VALUE, miscType.value, 0, miscType.name);
        this.miscType = miscType;
    }

    public MiscType getMiscType() {
        return miscType;
    }

    public void setMiscType(MiscType miscType) {
        this.miscType = miscType;
    }
}
