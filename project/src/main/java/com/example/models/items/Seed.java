package com.example.models.items;

import com.example.models.enums.types.CropSeedsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Seed extends Item {
    private CropSeedsType cropSeedsType;
    private String name;

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
