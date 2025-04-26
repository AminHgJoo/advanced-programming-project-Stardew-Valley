package com.example.models;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class StoreProduct {
    private int price;
    private Item item;
    // TODO initialize this field
    private int availableCount;
    public StoreProduct() {
    }

    public StoreProduct(Item item, int price) {
        this.item = item;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Item getItem() {
        return item;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }
}
