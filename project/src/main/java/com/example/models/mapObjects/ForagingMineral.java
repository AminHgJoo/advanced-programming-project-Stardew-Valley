package com.example.models.mapObjects;

import com.example.models.enums.types.ForagingMineralsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingMineral extends MapObject {
    ForagingMineralsType type;
    public ForagingMineral(){
    }
    public ForagingMineral(ForagingMineralsType type) {
        super(false, "foragingMineral", "black");
        this.type = type;
    }
}
