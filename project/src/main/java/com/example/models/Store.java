package com.example.models;

import com.example.models.NPCModels.NPC;

import com.example.java.time.LocalDateTime;
import com.example.java.util.ArrayList;

public class Store {
    private final NPC owner;
    private final LocalDateTime openHour;
    private final LocalDateTime closeHour;
    private final ArrayList<StoreProduct> products = new ArrayList<>();
    private final String name;

    public Store(NPC owner, LocalDateTime openHour, LocalDateTime closeHour, String name) {
        this.owner = owner;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.name = name;
    }

    public NPC getOwner() {
        return owner;
    }

    public LocalDateTime getOpenHour() {
        return openHour;
    }

    public LocalDateTime getCloseHour() {
        return closeHour;
    }

    public ArrayList<StoreProduct> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }
}
