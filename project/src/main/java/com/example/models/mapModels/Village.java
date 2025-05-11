package com.example.models.mapModels;

import com.example.models.NPCModels.NPC;
import com.example.models.Store;
import com.example.models.StoreProduct;
import com.example.models.enums.types.storeProductTypes.*;
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
        Store s1 = new Store("Clint", 9, 16, "Blacksmith");
        Store s2 = new Store("Morris", 9, 23, "JojaMart");
        Store s3 = new Store("Pierre", 9, 17, "Pierre's General Store");
        Store s4 = new Store("Robin", 9, 20, "Carpenter's Shop");
        Store s5 = new Store("Willy", 9, 17, "Fish Shop");
        Store s6 = new Store("Marnie", 9, 16, "Marnie's Shop");
        Store s7 = new Store("Gus", 12, 24, "The Stardrop Saloon");

        for (BlackSmithProducts p : BlackSmithProducts.values()) {
            StoreProduct sp = new StoreProduct(AllProducts.valueOf(p.name()), s1.getName());
            s1.getProducts().add(sp);
        }
        for (JojaMartProducts p : JojaMartProducts.values()) {
            StoreProduct sp = new StoreProduct(AllProducts.valueOf(p.name()), s2.getName());
            s2.getProducts().add(sp);
        }
        for (PierreProducts p : PierreProducts.values()) {
            StoreProduct sp = new StoreProduct(AllProducts.valueOf(p.name()), s3.getName());
            s3.getProducts().add(sp);
        }
        for (FishProducts p : FishProducts.values()) {
            StoreProduct sp = new StoreProduct(AllProducts.valueOf(p.name()), s5.getName());
            s5.getProducts().add(sp);
        }
        for (StardropProducts p : StardropProducts.values()) {
            StoreProduct sp = new StoreProduct(AllProducts.valueOf(p.name()), s7.getName());
            s7.getProducts().add(sp);
        }

        addStore(s1);
        addStore(s2);
        addStore(s3);
        addStore(s4);
        addStore(s5);
        addStore(s6);
        addStore(s7);
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
