package com.example.models.mapObjects;

import com.example.models.enums.types.ForagingCropsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingCrop extends MapObject {
    public ForagingCropsType type;
    public ForagingCrop(){}
    public ForagingCrop(ForagingCropsType type) {
        super(true, "foragingCrop", "black");
        this.type = type;
    }
}
