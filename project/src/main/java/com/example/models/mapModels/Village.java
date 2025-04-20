package com.example.models.mapModels;

import com.example.models.NPCModels.NPC;
import com.example.models.Store;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Village {
    private final ArrayList<Store> stores = new ArrayList<>();
    private final ArrayList<NPC> npcs = new ArrayList<>();

    public Village() {
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }
}
