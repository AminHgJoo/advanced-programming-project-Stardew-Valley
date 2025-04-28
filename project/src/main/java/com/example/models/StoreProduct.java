package com.example.models;

import com.example.models.enums.types.storeProductTypes.StoreProductInterface;
import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class StoreProduct {
    // TODO add StoreProduct type , reinitialize the availableCount end of the day
    private StoreProductInterface type;
    private int price;
    private Item item;
    // TODO initialize this field
    private double availableCount;
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

    public double getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(double availableCount) {
        this.availableCount = availableCount;
    }

    public StoreProductInterface getType() {
        return type;
    }

    public void setType(StoreProductInterface type) {
        this.type = type;
    }
}
