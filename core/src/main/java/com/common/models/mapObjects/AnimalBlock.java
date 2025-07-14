package com.common.models.mapObjects;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.Animal;
import com.common.models.enums.types.mapObjectTypes.AnimalMapObjectType;
import dev.morphia.annotations.Embedded;

@Embedded
public class AnimalBlock extends MapObject {
    public Animal animal;
    public AnimalMapObjectType mapObjectType;

    public AnimalBlock() {
        super();
    }

    public AnimalBlock(Animal animal) {
        super(false, "animal", "purple", null);
        this.animal = animal;
        this.mapObjectType = AnimalMapObjectType.getAnimalMapObjectType(animal.getType());
        this.texture = AssetManager.getImage(mapObjectType.getTextureName());
    }
    @Override
    public Texture getTexture(){
        if(texture == null){
            this.texture = AssetManager.getImage(mapObjectType.getTextureName());
        }
        return texture;
    }
}
