package com.example.models.items;

import com.example.models.enums.Quality;
import com.example.models.enums.types.CropSeedsType;
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

    public Seed(CropSeedsType cropSeedsType, String name) {
        this.cropSeedsType = cropSeedsType;
        this.name = name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
