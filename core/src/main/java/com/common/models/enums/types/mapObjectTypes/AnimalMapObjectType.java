package com.common.models.enums.types.mapObjectTypes;

import com.common.models.enums.types.AnimalType;

public enum AnimalMapObjectType {
    CHICKEN(AnimalType.Chicken, "White_Chicken"),
    DUCK(AnimalType.DUCK, "Duck"),
    RABBIT(AnimalType.RABBIT, "Rabbit"),
    DINOSAUR(AnimalType.DINOSAUR, "Dinosaur"),
    COW(AnimalType.COW, "White_Cow"),
    GOAT(AnimalType.GOAT, "Goat"),
    SHEEP(AnimalType.SHEEP, "Sheep"),
    PIG(AnimalType.PIG, "Pig");

    private final AnimalType animalType;
    private final String textureName;

    AnimalMapObjectType(AnimalType animalType, String textureName) {
        this.animalType = animalType;
        this.textureName = textureName;
    }

    public static AnimalMapObjectType getAnimalMapObjectType(AnimalType animalType) {
        for (AnimalMapObjectType animalMapObjectType : AnimalMapObjectType.values()) {
            if (animalMapObjectType.animalType == animalType) {
                return animalMapObjectType;
            }
        }
        return null;
    }

    public String getTextureName() {
        return textureName;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }
}
