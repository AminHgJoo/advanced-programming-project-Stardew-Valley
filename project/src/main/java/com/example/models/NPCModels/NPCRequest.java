package com.example.models.NPCModels;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class NPCRequest {
    private Item[] requestedItems;

    public NPCRequest() {}

    public NPCRequest(Item[] requestedItems) {
        this.requestedItems = requestedItems;
    }

    public Item[] getRequestedItems() {
        return requestedItems;
    }

    public void setRequestedItems(Item[] requestedItems) {
        this.requestedItems = requestedItems;
    }
}
