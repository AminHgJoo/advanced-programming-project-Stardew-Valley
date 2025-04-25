package com.example.models.items;

import com.example.models.Slot;
import com.example.models.enums.types.CropType;
import com.example.utilities.ItemLambda;
import dev.morphia.annotations.Embedded;

@Embedded
public class Seed extends Item{
    private CropType cropType;
    private String name;

    public Seed(){}

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    public Seed(CropType cropType, String name) {
        this.cropType = cropType;
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
