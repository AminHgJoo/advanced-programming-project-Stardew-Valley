package com.example.models.NPCModels;

import com.example.models.Quest;
import com.example.models.items.Item;
import com.example.models.mapModels.Coordinate;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class NPC {
    private String name;
    private ArrayList<Item> favoriteItems = new ArrayList<>();
    private ArrayList<NPCRequest> npcRequests = new ArrayList<>();
    private ArrayList<NPCReward> rewards = new ArrayList<>();
    private Coordinate coordinate;
    private ArrayList<NPCFriendship> friendships = new ArrayList<>();
    private ArrayList<NPCDialogue> dialogues = new ArrayList<>();
    private ArrayList<Quest> quests = new ArrayList<>();

    public NPC() {
    }

    public NPC(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getFavoriteItems() {
        return favoriteItems;
    }

    public ArrayList<NPCRequest> getNpcRequests() {
        return npcRequests;
    }

    public ArrayList<NPCReward> getRewards() {
        return rewards;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public ArrayList<NPCFriendship> getFriendships() {
        return friendships;
    }

    public ArrayList<NPCDialogue> getDialogues() {
        return dialogues;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }
}
