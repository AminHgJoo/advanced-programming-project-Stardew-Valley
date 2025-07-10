package com.common.models.mapObjects;

import com.common.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class DroppedItem extends MapObject {
    private int quantity;
    private Item item;

    public DroppedItem() {
        super();
    }

    public DroppedItem(int quantity, Item item) {
        //TODO texture
        super(true, "droppedItem", "red", null);
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
