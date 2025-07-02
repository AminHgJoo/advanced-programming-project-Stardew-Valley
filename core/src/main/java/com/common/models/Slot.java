package com.common.models;

import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.items.Food;
import com.common.models.items.ForagingMineralItem;
import com.common.models.items.Item;
import com.common.models.items.Misc;
import dev.morphia.annotations.Embedded;

@Embedded
public class Slot {
    private Item item;
    private int count;

    public Slot() {
    }

    public Slot(ItemType type, int count) {
        Item item = null;
        if (type instanceof FoodTypes) {
            item = new Food((FoodTypes) type);
        } else if (type instanceof MiscType) {
            item = new Misc((MiscType) type);
        } else if (type instanceof ForagingMineralsType) {
            item = new ForagingMineralItem(Quality.DEFAULT, (ForagingMineralsType) type);
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
        return "item=" + item.getName() + ", count=" + count + ", value=" + item.getValue();
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
