package com.common.models.items;

import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.CropSeedsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Seed extends Item {
    private CropSeedsType cropSeedsType;

    public Seed() {
    }

    public CropSeedsType getCropType() {
        return cropSeedsType;
    }

    public void setCropType(CropSeedsType cropSeedsType) {
        this.cropSeedsType = cropSeedsType;
    }

    public Seed(CropSeedsType cropSeedsType) {
        this.cropSeedsType = cropSeedsType;
        this.name = cropSeedsType.source;
        this.quality = Quality.DEFAULT;
        this.maxStackSize = Integer.MAX_VALUE;
        this.value = cropSeedsType.baseSellPrice;
        this.energyCost = 0;
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
    public String getName() {
        return cropSeedsType.source;
    }

    public void setName(String name) {
        this.name = name;
    }
}
