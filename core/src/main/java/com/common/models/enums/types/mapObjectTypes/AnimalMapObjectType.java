package com.common.models.enums.types.mapObjectTypes;

import com.common.models.enums.types.AnimalType;

public enum AnimalMapObjectType {
    CHICKEN(AnimalType.Chicken, "whiteChicken"),
    DUCK(AnimalType.DUCK, "duck"),
    RABBIT(AnimalType.RABBIT, "rabbit"),
    DINOSAUR(AnimalType.DINOSAUR, "dinosaur"),
    COW(AnimalType.COW, "whiteCow"),
    GOAT(AnimalType.GOAT, "goat"),
    SHEEP(AnimalType.SHEEP, "sheep"),
    PIG(AnimalType.PIG, "pig");

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
