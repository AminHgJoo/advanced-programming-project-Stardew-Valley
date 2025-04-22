package com.example.models.mapObjects;

import com.example.models.enums.types.ForagingMineralsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingMineral extends MapObject {
    private ForagingMineralsType type;

    public ForagingMineral(ForagingMineralsType type, String color, String name) {
        super(false, "foragingMineral", color);
        this.type = type;
    }

    public ForagingMineral(ForagingMineralsType type) {
        super(false, "foragingMineral", "black");
        this.type = type;
    }

    public ForagingMineralsType getType() {
        return type;
    }
}
