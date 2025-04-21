package com.example.models.mapObjects;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class DroppedItem extends MapObject {
    private int quantity;
    private Item item;

    public DroppedItem(String color, int quantity, Item item) {
        super(true, "droppedItem", color);
        this.quantity = quantity;
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item getItem() {
        return item;
    }
}
