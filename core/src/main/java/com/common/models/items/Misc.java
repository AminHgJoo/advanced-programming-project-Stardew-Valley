package com.common.models.items;

import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.MiscType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Misc extends Item {
    private MiscType miscType;

    public Misc() {
        super();
    }

    public Misc(MiscType miscType, Quality quality) {
        super(quality, Integer.MAX_VALUE, miscType.value, 0, miscType.name);
        this.miscType = miscType;
    }

    public Misc(Quality quality, MiscType miscType, int price) {
        super(quality, Integer.MAX_VALUE, price, 0, miscType.name);
        this.miscType = miscType;
    }


    public Misc(MiscType miscType) {
        this(miscType, Quality.DEFAULT);
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

    public MiscType getMiscType() {
        return miscType;
    }

    public void setMiscType(MiscType miscType) {
        this.miscType = miscType;
    }

    @Override
    public int getValue() {
        if (this.quality == Quality.SILVER)
            return (int) ((double) this.value * 1.25);
        else if (this.quality == Quality.GOLD)
            return (int) ((double) this.value * 1.5);
        else if (this.quality == Quality.IRIDIUM)
            return (int) ((double) this.value * 2);

        return value;
    }
}
