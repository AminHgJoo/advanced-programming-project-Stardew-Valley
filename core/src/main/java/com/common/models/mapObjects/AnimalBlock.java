package com.common.models.mapObjects;

import com.common.models.Animal;
import dev.morphia.annotations.Embedded;

@Embedded
public class AnimalBlock extends MapObject {
    public Animal animal;

    public AnimalBlock() {super();}

    public AnimalBlock(Animal animal) {
        super(false, "animal", "purple");
        this.animal = animal;
    }
}
