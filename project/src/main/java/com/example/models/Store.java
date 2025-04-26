package com.example.models;

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


    // TODO do the to string stuff
    public String productsToString() {
        StringBuilder message = new StringBuilder();
        return message.toString();
    }
    public ArrayList<StoreProduct> getAvailableProducts(){
        ArrayList<StoreProduct> availableProducts = new ArrayList<>();
        for (StoreProduct product : products) {
            if(product.getAvailableCount()>0){
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }
    public String availableProductsToString() {
        ArrayList<StoreProduct> availableProducts = getAvailableProducts();
        StringBuilder message = new StringBuilder();
        return message.toString();
    }
}

