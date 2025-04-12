package models.mapModels;

import models.NPCModels.NPC;
import models.Store;

import java.util.ArrayList;

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
