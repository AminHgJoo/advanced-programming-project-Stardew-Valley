package com.example.models;

import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.*;
import com.example.models.enums.types.storeProductTypes.FishProducts;
import com.example.models.items.*;
import dev.morphia.annotations.Embedded;

@Embedded
public class Slot {
    private Item item;
    private int count;

    public Slot() {
    }
    
    public Slot(ItemType type , int count) {
        Item item = null;
        if (type instanceof FoodTypes) {
            item = new Food((FoodTypes) type);
        } else if (type instanceof MiscType) {
            item = new Misc((MiscType) type);
        } else if (type instanceof ForagingMineralsType) {
            item = new ForagingMineral(Quality.DEFAULT, (ForagingMineralsType) type);
        }
        this.item = item;
        this.count = count;
    }

    public Slot(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Slot [item=" + item.getName() + ", count=" + count + "]";
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
