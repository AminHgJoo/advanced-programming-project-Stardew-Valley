package com.example.models.mapModels;

import com.example.models.NPCModels.NPC;
import com.example.models.Store;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Village {
    private final ArrayList<Store> stores = new ArrayList<>();
    private final ArrayList<NPC> npcs = new ArrayList<>();
    private final ArrayList<Cell> cells = new ArrayList<>();

    public Village() {
        initializeStores();
    }

    public void initializeStores() {
        addStore(new Store("Clint", 9, 16, "Blacksmith"));
        addStore(new Store("Morris", 9, 23, "JojaMart"));
        addStore(new Store("Pierre", 9, 17, "Pierre's General Store"));
        addStore(new Store("Robin", 9, 20, "Carpenter's Shop"));
        addStore(new Store("Willy", 9, 17, "Fish Shop"));
        addStore(new Store("Marnie", 9, 16, "Marnie's Shop"));
        addStore(new Store("Gus", 12, 24, "The Stardrop Saloon"));
    }

    public void addStore(Store store) {
        stores.add(store);
    }

    public Store getStore(String storeName) {
        for (Store store : stores) {
            if (store.getName().compareToIgnoreCase(storeName) == 0) {
                return store;
            }
        }
        return null;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}
