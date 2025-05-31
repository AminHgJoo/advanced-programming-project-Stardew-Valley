package com.example.models;

import com.example.models.enums.worldEnums.Season;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Store {
    private String owner;
    private int openHour;
    private int closeHour;
    private ArrayList<StoreProduct> products = new ArrayList<>();
    private String name;

    public Store() {
    }

    public Store(String owner, int openHour, int closeHour, String name) {
        this.owner = owner;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public int getOpenHour() {
        return openHour;
    }

    public int getCloseHour() {
        return closeHour;
    }

    public ArrayList<StoreProduct> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen(int hour) {
        return openHour <= hour && hour <= closeHour;
    }

    public String productsToString(Season season) {
        StringBuilder message = new StringBuilder();
        for (StoreProduct product : products) {
            message.append("name ").append(": ").append(product.getType().getName()).append("\n");
            message.append("price ").append(": ");
            if (product.getType().isInSeason(season)) {
                message.append(product.getType().getPrice());
            } else {
                message.append(product.getType().getOutOfSeasonPrice());
            }
            message.append("\n");
        }
        return message.toString();
    }

    public ArrayList<StoreProduct> getAvailableProducts() {
        ArrayList<StoreProduct> availableProducts = new ArrayList<>();
        for (StoreProduct product : products) {
            if (product.getAvailableCount() > 0) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

    public String availableProductsToString(Season season) {
        ArrayList<StoreProduct> availableProducts = getAvailableProducts();
        StringBuilder message = new StringBuilder();
        for (StoreProduct product : availableProducts) {
            message.append("name ").append(": ").append(product.getType().getName()).append("\n");
            message.append("price ").append(": ");
            if (product.getType().isInSeason(season)) {
                message.append(product.getType().getPrice());
            } else {
                message.append(product.getType().getOutOfSeasonPrice());
            }
            message.append("\n");
        }
        return message.toString();
    }

    public StoreProduct getProduct(String productName) {
        for (StoreProduct product : products) {
            if (product.getType().getName().compareToIgnoreCase(productName) == 0) {
                return product;
            }
        }
        return null;
    }
}

