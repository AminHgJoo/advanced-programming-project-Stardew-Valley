package com.example.models.mapObjects;

import com.example.models.enums.types.ForagingMineralsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingMineral extends MapObject {
    private ForagingMineralsType fMType;

    public ForagingMineral(){

    }

    public ForagingMineral(ForagingMineralsType type, String color, String name) {
        super(false, "foragingMineral", color);
        this.fMType = type;
    }

    public ForagingMineral(ForagingMineralsType type) {
        super(false, "foragingMineral", "black");
        this.fMType = type;
    }

    public void setFMType(ForagingMineralsType type) {
        this.fMType = type;
    }

    public ForagingMineralsType getFMType() {
        return fMType;
    }
}
