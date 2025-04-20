package com.example.models;

import com.example.models.NPCModels.NPCFriendship;
import com.example.models.enums.types.BackpackType;
import com.example.models.enums.types.TrashcanType;
import com.example.models.items.Item;
import com.example.models.mapModels.Coordinate;
import com.example.models.mapModels.Farm;
import com.example.models.skills.Skill;
import dev.morphia.annotations.Reference;

import java.util.ArrayList;

public class Player {
    private Coordinate coordinate;
    private int money;
    private final Backpack inventory;
    private Farm farm;
    private final ArrayList<Skill> skills = new ArrayList<>();
    private final ArrayList<Quest> quests = new ArrayList<>();
    @Reference
    private final User user;
    private final ArrayList<Friendship> friendships = new ArrayList<>();
    private final ArrayList<NPCFriendship> npcFriendships = new ArrayList<>();
    private final ArrayList<PlayerAnimal> animals = new ArrayList<>();
    private double energy;
    private double maxEnergy;
    private boolean isPlayerFainted;

    private TrashcanType trashcanType;
    private Item equippedItem;

    // TODO: handle energy usage in one turn.
    private double usedEnergyInTurn;

    public Player(User user) {
        this.user = user;
        this.inventory = new Backpack(BackpackType.DEFAULT);
        this.trashcanType = TrashcanType.DEFAULT;
        this.usedEnergyInTurn = 0;
        this.energy = 200;
        this.maxEnergy = 200;
        this.money = 0;
        this.coordinate = new Coordinate(0, 0);
        this.equippedItem = null;
        this.isPlayerFainted = false;
    }

    //TODO: Debug Only Constructor. Not Usable.
    private Player(Coordinate coordinate, int money, Farm farm, User user, double energy) {
        this.coordinate = coordinate;
        this.trashcanType = TrashcanType.DEFAULT;
        this.inventory = new Backpack(BackpackType.DEFAULT);
        this.money = money;
        this.farm = farm;
        this.user = user;
        this.energy = energy;
    }

    public int getTrashcanRefundPercentage(){
        return trashcanType.refundPercentage;
    }

    public double getEnergy() {
        return energy;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public Backpack getInventory() {
        return inventory;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Friendship> getFriendships() {
        return friendships;
    }

    public ArrayList<NPCFriendship> getNpcFriendships() {
        return npcFriendships;
    }

    public TrashcanType getTrashcanType() {
        return trashcanType;
    }

    public void setTrashcanType(TrashcanType trashcanType) {
        this.trashcanType = trashcanType;
    }

    public ArrayList<PlayerAnimal> getAnimals() {
        return animals;
    }


    public Item getEquippedItem() {
        return equippedItem;
    }

    public void setEquippedItem(Item equippedItem) {
        this.equippedItem = equippedItem;
    }

    public double getUsedEnergyInTurn() {
        return usedEnergyInTurn;
    }

    public void setUsedEnergyInTurn(double usedEnergyInTurn) {
        this.usedEnergyInTurn = usedEnergyInTurn;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(double maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public boolean isPlayerFainted() {
        return isPlayerFainted;
    }

    public void setPlayerFainted(boolean playerFainted) {
        isPlayerFainted = playerFainted;
    }
}
