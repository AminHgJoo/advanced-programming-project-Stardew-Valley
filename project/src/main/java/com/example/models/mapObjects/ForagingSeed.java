package com.example.models.mapObjects;

import com.example.models.enums.types.ForagingSeedsType;
import dev.morphia.annotations.Embedded;

@Embedded
public class ForagingSeed extends MapObject {
    public ForagingSeedsType type;
    public ForagingSeed(){
        super();
    }
    public ForagingSeed(ForagingSeedsType type) {
        super(true, "foragingSeed", "black");
        this.type = type;
    }
}
