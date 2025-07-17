package com.common.models;

import com.common.models.enums.types.storeProductTypes.AllProducts;
import com.common.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class StoreProduct {
    private AllProducts type;
    private int price;
    private Item item;
    private double availableCount;
    private String storeName;

    public StoreProduct() {
    }

    public StoreProduct(AllProducts type, String storeName) {
        this.type = type;
        this.availableCount = type.getDailyLimit();
        this.storeName = storeName;
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

    public AllProducts getType() {
        return type;
    }

    public void setType(AllProducts type) {
        this.type = type;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
