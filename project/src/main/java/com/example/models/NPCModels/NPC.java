package com.example.models.NPCModels;

import models.Quest;
import models.items.Item;
import models.mapModels.Coordinate;

import java.util.ArrayList;

public class NPC {
    final private String name;
    final private ArrayList<Item> favoriteItems = new ArrayList<>();
    final private ArrayList<NPCRequest> npcRequests = new ArrayList<>();
    final private ArrayList<NPCReward> rewards = new ArrayList<>();
    private Coordinate coordinate;
    private final ArrayList<NPCFriendship> friendships = new ArrayList<>();
    private final ArrayList<NPCDialogue> dialogues = new ArrayList<>();
    private final ArrayList<Quest> quests = new ArrayList<>();

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
