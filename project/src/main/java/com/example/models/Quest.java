package com.example.models;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class Quest {
    private int count;
    private ItemType item;
    private boolean completed = false;

    public Quest() {

    }
    public Quest(ItemType item , int count) {
        this.item = item;
        this.count = count;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public ItemType getItem() {
        return item;
    }
    public void setItem(ItemType item) {
        this.item = item;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
