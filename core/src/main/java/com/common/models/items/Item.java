package com.common.models.items;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.*;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

@Embedded
public class Item {
    protected Quality quality;
    protected int maxStackSize;
    protected int value;
    protected double energyCost;
    protected String name;
    @Transient
    protected Texture texture;

    public Item() {
    }

    public Item(Quality quality, int maxStackSize, int value, double energyCost, String name) {
        this.quality = quality;
        this.maxStackSize = maxStackSize;
        this.value = value;
        this.energyCost = energyCost;
        this.name = name;
        ItemType itemType = getItemType(name);
        this.texture = AssetManager.getImage(itemType.getTextureName());
    }


    public ItemType getItemType(String name) {
        for (CropSeedsType type : CropSeedsType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (FishType type : FishType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (FoodTypes type : FoodTypes.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (ForagingMineralsType type : ForagingMineralsType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (MiscType type : MiscType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (ToolTypes type : ToolTypes.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (TreeSeedsType type : TreeSeedsType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }


    public void useItem() {
    }

    ;

    public void deleteItem() {
    }

    ;

    public void dropItem() {
    }

    ;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Quality getQuality() {
        return quality;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

}
