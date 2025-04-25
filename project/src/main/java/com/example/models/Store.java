package com.example.models;

import com.example.models.NPCModels.NPC;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;
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
}
