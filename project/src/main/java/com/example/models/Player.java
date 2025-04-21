package com.example.models;

import com.example.models.NPCModels.NPCFriendship;
import com.example.models.enums.Quality;
import com.example.models.enums.types.BackpackType;
import com.example.models.enums.types.ToolTypes;
import com.example.models.enums.types.TrashcanType;
import com.example.models.items.Item;
import com.example.models.items.Tool;
import com.example.models.mapModels.Coordinate;
import com.example.models.mapModels.Farm;
import com.example.models.skills.*;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Transient;
import org.bson.types.ObjectId;

import java.util.ArrayList;

@Embedded
public class Player {
    private Coordinate coordinate;
    private int money;
    private Backpack inventory;
    private Farm farm;
    private ArrayList<Skill> skills = new ArrayList<>();
    private ArrayList<Quest> quests = new ArrayList<>();
    private ObjectId user_id;
    @Transient
    private User user;
    private ArrayList<Friendship> friendships = new ArrayList<>();
    private ArrayList<NPCFriendship> npcFriendships = new ArrayList<>();
    private ArrayList<PlayerAnimal> animals = new ArrayList<>();
    private double energy;
    private double maxEnergy;
    private boolean isPlayerFainted;

    private TrashcanType trashcanType;
    private Item equippedItem;

    // TODO: handle energy usage in one turn.
    private double usedEnergyInTurn;

    public Player() {}

    public Player(User user) {
        this.user = user;
        this.user_id = user.get_id();
        this.inventory = new Backpack(BackpackType.DEFAULT);
        this.trashcanType = TrashcanType.DEFAULT;
        this.usedEnergyInTurn = 0;
        this.energy = 200;
        this.maxEnergy = 200;
        this.money = 0;
        this.coordinate = new Coordinate(0, 0);
        this.equippedItem = null;
        this.isPlayerFainted = false;
        initializeInventory();
        initializeSkills();
    }

    private void initializeSkills() {
        this.skills.add(new Farming());
        this.skills.add(new Fishing());
        this.skills.add(new Foraging());
        this.skills.add(new Mining());
    }

    private void initializeInventory() {
        this.inventory.getSlots().add(
                new Slot(new Tool(Quality.DEFAULT, 0, 5, "Default Hoe", ToolTypes.HOE, 0), 1));
        this.inventory.getSlots().add(
                new Slot(new Tool(Quality.DEFAULT, 0, 5, "Default Pickaxe", ToolTypes.PICKAXE, 0), 1));
        this.inventory.getSlots().add(
                new Slot(new Tool(Quality.DEFAULT, 0, 5, "Default Axe", ToolTypes.AXE, 0), 1));
        this.inventory.getSlots().add(
                new Slot(new Tool(Quality.DEFAULT, 0, 5, "Default Water Can", ToolTypes.WATERING_CAN_DEFAULT, 40), 1));
        this.inventory.getSlots().add(
                new Slot(new Tool(Quality.DEFAULT, 0, 5, "Default Scythe", ToolTypes.SCYTHE, 0), 1));
    }

    public Farming getFarmingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Farming) {
                return (Farming) skill;
            }
        }
        return null;
    }

    public Fishing getFishingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Fishing) {
                return (Fishing) skill;
            }
        }
        return null;
    }

    public Foraging getForagingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Foraging) {
                return (Foraging) skill;
            }
        }
        return null;
    }

    public Mining getMiningSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Mining) {
                return (Mining) skill;
            }
        }
        return null;
    }

    //TODO: Debug Only Constructor. Not Usable.
    private Player(Coordinate coordinate, int money, Farm farm, User user, double energy) {
        this.coordinate = coordinate;
        this.trashcanType = TrashcanType.DEFAULT;
        this.inventory = new Backpack(BackpackType.DEFAULT);
        this.money = money;
        this.farm = farm;
        this.user = user;
        this.user_id = user.get_id();
        this.energy = energy;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTrashcanRefundPercentage() {
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

//    public ArrayList<Skill> getSkills() {
//        return skills;
//    }

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

    public ObjectId getUser_id() {
        return user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return player.getUser_id().toString().equals(this.getUser_id().toString());
    }
}
