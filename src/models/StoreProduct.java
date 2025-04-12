package models;

import models.items.Item;

public class StoreProduct {
    private final int price;
    private final Item item;

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
}
