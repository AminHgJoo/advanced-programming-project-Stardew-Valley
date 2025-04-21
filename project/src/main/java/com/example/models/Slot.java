package com.example.models;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class Slot {
    private Item item;
    private int count;

    public Slot(){}

    public Slot(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Slot [header=" + item.getName() + ", count=" + count + "]";
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
