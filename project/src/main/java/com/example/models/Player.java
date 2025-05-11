package com.example.models;

import com.example.Repositories.UserRepository;
import com.example.models.NPCModels.NPC;
import com.example.models.NPCModels.NPCFriendship;
import com.example.models.NPCModels.NPCReward;
import com.example.models.enums.Quality;
import com.example.models.enums.recipes.CookingRecipes;
import com.example.models.enums.recipes.CraftingRecipes;
import com.example.models.enums.types.inventoryEnums.BackpackType;
import com.example.models.enums.types.inventoryEnums.TrashcanType;
import com.example.models.enums.types.itemTypes.ToolTypes;
import com.example.models.items.Item;
import com.example.models.items.Tool;
import com.example.models.items.buffs.ActiveBuff;
import com.example.models.mapModels.Coordinate;
import com.example.models.mapModels.Farm;
import com.example.models.skills.*;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Embedded
public class Player {
    private static final Logger log = LoggerFactory.getLogger(Player.class);
    private Coordinate coordinate;
    /// DO NOT USE THIS FIELD DIRECTLY
    public int money;
    private Backpack inventory;
    private Farm farm;
    private ArrayList<Skill> skills = new ArrayList<>();
    private ArrayList<Quest> quests = new ArrayList<>();
    private ArrayList<CookingRecipes> unlockedCookingRecipes = new ArrayList<>();
    private ArrayList<CraftingRecipes> unlockedCraftingRecipes = new ArrayList<>();
    private ArrayList<ActiveBuff> activeBuffs = new ArrayList<>();
    private ArrayList<Slot> refrigeratorSlots = new ArrayList<>();
    private ObjectId user_id;
    private String partnerName;
    private int currentFarmNumber;
    private boolean isInVillage;
    @Transient
    private User user;
    private ArrayList<Friendship> friendships = new ArrayList<>();
    private ArrayList<NPCFriendship> npcs = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<MarriageRequest> marriageRequests = new ArrayList<>();
    private double energy;
    private double maxEnergy;
    private boolean isPlayerFainted;
    private TrashcanType trashcanType;
    private Item equippedItem;
    private double usedEnergyInTurn;
    @Transient
    private ArrayList<Message> notifications = new ArrayList<>();
    private int moneyInNextDay = 0;

    public Slot getRefrigeratorSlotByName(String slotName) {
        for (Slot slot : refrigeratorSlots) {
            if (slot.getItem().getName().compareToIgnoreCase(slotName) == 0) {
                return slot;
            }
        }
        return null;
    }

    public int getCurrentFarmNumber() {
        return currentFarmNumber;
    }

    public void setCurrentFarmNumber(int currentFarmNumber) {
        this.currentFarmNumber = currentFarmNumber;
    }

    public Player() {
    }

    public Player(User user) {
        this.isInVillage = false;
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
        this.partnerName = null;
        initializeInventory();
        initializeSkills();
        initializeRecipes();
    }

    public Animal getAnimalByName(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equals(name)) {
                return animal;
            }
        }
        return null;
    }

    private void initializeRecipes() {
        this.unlockedCraftingRecipes.add(CraftingRecipes.FURNACE);
        this.unlockedCraftingRecipes.add(CraftingRecipes.SCARE_CROW);
        this.unlockedCraftingRecipes.add(CraftingRecipes.MAYONNAISE_MACHINE);
        this.unlockedCookingRecipes.add(CookingRecipes.FRIED_EGG);
        this.unlockedCookingRecipes.add(CookingRecipes.BAKED_FISH);
        this.unlockedCookingRecipes.add(CookingRecipes.SALAD);
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
                Farming farming = (Farming) skill;
                Farming clone = new Farming(farming.getLevel(), farming.getXp());

                for (ActiveBuff activeBuff : activeBuffs) {
                    if (activeBuff.getFoodBuff().getAffectedField().compareToIgnoreCase("farming") == 0) {
                        for (int i = 0; i < activeBuff.getFoodBuff().getIncrement(); i++) {
                            if (clone.getLevel().getNextLevel() != null) {
                                clone.setLevel(clone.getLevel().getNextLevel());
                            } else {
                                break;
                            }
                        }
                    }
                }

                return clone;
            }
        }
        return null;
    }

    public Fishing getFishingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Fishing) {
                Fishing fishing = (Fishing) skill;
                Fishing clone = new Fishing(fishing.getLevel(), fishing.getXp());

                for (ActiveBuff activeBuff : activeBuffs) {
                    if (activeBuff.getFoodBuff().getAffectedField().compareToIgnoreCase("fishing") == 0) {
                        for (int i = 0; i < activeBuff.getFoodBuff().getIncrement(); i++) {
                            if (clone.getLevel().getNextLevel() != null) {
                                clone.setLevel(clone.getLevel().getNextLevel());
                            } else {
                                break;
                            }
                        }
                    }
                }

                return clone;
            }
        }
        return null;
    }

    public Foraging getForagingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Foraging) {
                Foraging foraging = (Foraging) skill;
                Foraging clone = new Foraging(foraging.getLevel(), foraging.getXp());

                for (ActiveBuff activeBuff : activeBuffs) {
                    if (activeBuff.getFoodBuff().getAffectedField().compareToIgnoreCase("foraging") == 0) {
                        for (int i = 0; i < activeBuff.getFoodBuff().getIncrement(); i++) {
                            if (clone.getLevel().getNextLevel() != null) {
                                clone.setLevel(clone.getLevel().getNextLevel());
                            } else {
                                break;
                            }
                        }
                    }
                }

                return clone;
            }
        }
        return null;
    }

    public Mining getMiningSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Mining) {
                Mining mining = (Mining) skill;
                Mining clone = new Mining(mining.getLevel(), mining.getXp());

                for (ActiveBuff activeBuff : activeBuffs) {
                    if (activeBuff.getFoodBuff().getAffectedField().compareToIgnoreCase("mining") == 0) {
                        for (int i = 0; i < activeBuff.getFoodBuff().getIncrement(); i++) {
                            if (clone.getLevel().getNextLevel() != null) {
                                clone.setLevel(clone.getLevel().getNextLevel());
                            } else {
                                break;
                            }
                        }
                    }
                }

                return clone;
            }
        }
        return null;
    }

    public Farming getUnbuffedFarmingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Farming) {
                Farming farming = (Farming) skill;
                return farming;
            }
        }
        return null;
    }

    public Fishing getUnbuffedFishingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Fishing) {
                Fishing fishing = (Fishing) skill;
                return fishing;
            }
        }
        return null;
    }

    public Foraging getUnbuffedForagingSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Foraging) {
                Foraging foraging = (Foraging) skill;
                return foraging;
            }
        }
        return null;
    }

    public Mining getUnbuffedMiningSkill() {
        for (Skill skill : skills) {
            if (skill instanceof Mining) {
                Mining mining = (Mining) skill;
                return mining;
            }
        }
        return null;
    }

    /// Debug Only Constructor. Not Usable.
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

    public Farm getCurrentFarm(Game game) {
        return game.getFarmByNumber(currentFarmNumber);
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

    public int getMoney(Game game) {
        if (partnerName == null) {
            return money;
        }
        Player partner = game.getPartner(this);
        return partner.money + money;
    }

    public void setMoney(int money, Game game) {
        if (partnerName == null) {
            this.money = money;
        } else {
            Player partner = game.getPartner(this);
            partner.money = 0;
            this.money = money;
        }

    }

    public void setFarm(Farm farm) {
        this.farm = farm;
        this.currentFarmNumber = farm.getFarmNumber();
    }

    public Backpack getInventory() {
        return inventory;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public User getUser() {
        if (user == null) {
            // Lazy load user when needed
            user = UserRepository.findUserById(user_id.toString());
        }
        return user;
    }

    public ArrayList<NPCFriendship> getNpcs() {
        return npcs;
    }

    public TrashcanType getTrashcanType() {
        return trashcanType;
    }

    public void setTrashcanType(TrashcanType trashcanType) {
        this.trashcanType = trashcanType;
    }

    public ArrayList<Animal> getAnimals() {
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
        double clone = maxEnergy;
        for (ActiveBuff activeBuff : activeBuffs) {
            if (activeBuff.getFoodBuff().getAffectedField().compareToIgnoreCase("maxEnergy") == 0) {
                clone += activeBuff.getFoodBuff().getIncrement();
            }
        }
        return clone;
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

    public ArrayList<CookingRecipes> getUnlockedCookingRecipes() {
        return unlockedCookingRecipes;
    }

    public ArrayList<CraftingRecipes> getUnlockedCraftingRecipes() {
        return unlockedCraftingRecipes;
    }

    public ArrayList<Friendship> getFriendships() {
        return friendships;
    }

    public ArrayList<ActiveBuff> getActiveBuffs() {
        return activeBuffs;
    }

    public ArrayList<Slot> getRefrigeratorSlots() {
        return refrigeratorSlots;
    }

    public String friendShipToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Friends : \n");
        for (Friendship friendship : friendships) {
            builder.append("Friend : ").append(friendship.getPlayer()).append("\n");
            builder.append("XP : ").append(friendship.getXp()).append("\n");
            builder.append("Level : ").append(friendship.getLevel()).append("\n");
            builder.append("------------------------------------------------------\n");
        }
        return builder.toString();
    }

    public String npcFriendShipToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NPC Friends : \n");
        for (NPCFriendship friendship : npcs) {
            builder.append("Npc : ").append(friendship.getNpc()).append("\n");
            builder.append("XP : ").append(friendship.getXp()).append("\n");
            builder.append("Level : ").append(friendship.getLevel()).append("\n");
            builder.append("------------------------------------------------------\n");
        }
        return builder.toString();
    }

    public Friendship findFriendshipByFriendName(String player) {
        for (Friendship friendship : friendships) {
            if (friendship.getPlayer().equals(player)) {
                return friendship;
            }
        }
        return null;
    }

    public NPCFriendship findFriendshipByNPC(String npc) {
        for (NPCFriendship friendship : npcs) {
            if (friendship.getNpc().equals(npc)) {
                return friendship;
            }
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return player.getUser_id().toString().equals(this.getUser_id().toString());
    }

    public ArrayList<Message> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Message> notifications) {
        this.notifications = notifications;
    }

    public String getNotificationsString(Game game) {
        StringBuilder builder = new StringBuilder();
        builder.append("Notifications : ").append("\n");
        int counter = 1;
        for (Message message : notifications) {
            builder.append(counter++).append("- \n");
            builder.append("From : ").append(message.getSender()).append("\n");
            builder.append("Message : ").append(message.getMessage()).append("\n");
            builder.append("------------------------------------------------------\n");
        }
        counter = 1;
        builder.append("Marriage Requests : ").append("\n");
        for (MarriageRequest req : marriageRequests) {
            builder.append(counter++).append("- \n");
            builder.append("From : ").append(req.getFrom()).append("\n");
            builder.append("------------------------------------------------------\n");
        }
        counter = 1;
        builder.append("Gifts : ").append("\n");
        for (Gift g : game.getGifts()) {
            if (g.getTo().equals(user.getUsername())) {
                builder.append(counter++).append("- \n");
                builder.append("From : ").append(g.getFrom()).append("\n");
                builder.append("Item : ").append(g.getItem().getName()).append("\n");
                builder.append("Count : ").append(g.getAmount()).append("\n");
                builder.append("------------------------------------------------------\n");
            }
        }
        return builder.toString();
    }

    public String getGiftsString(Game game) {
        StringBuilder builder = new StringBuilder();
        builder.append("Gifts : ").append("\n");
        int counter = 1;
        for (Gift g : game.getGifts()) {
            if (g.getTo().equals(user.getUsername())) {
                builder.append(counter++).append("- \n");
                builder.append("From : ").append(g.getFrom()).append("\n");
                builder.append("Item : ").append(g.getItem().getName()).append("\n");
                builder.append("Count : ").append(g.getAmount()).append("\n");
                builder.append("------------------------------------------------------\n");
            }
        }
        return builder.toString();
    }

    public String getGiftHistoryString(Game game, String username) {
        StringBuilder builder = new StringBuilder();
        builder.append("Gifts : ").append("\n");
        int counter = 1;
        for (Gift g : game.getGifts()) {
            if (g.getTo().equals(user.getUsername()) || g.getFrom().equals(user.getUsername())) {
                if (g.getTo().compareToIgnoreCase(username) == 0 || g.getFrom().compareToIgnoreCase(username) == 0) {
                    builder.append(counter++).append("- \n");
                    builder.append("From : ").append(g.getFrom()).append("\n");
                    builder.append("To : ").append(g.getTo()).append("\n");
                    builder.append("Item : ").append(g.getItem().getName()).append("\n");
                    builder.append("Count : ").append(g.getAmount()).append("\n");
                    builder.append("------------------------------------------------------\n");
                }
            }
        }
        return builder.toString();
    }

    public void reInitNotifications() {
        notifications.clear();
    }

    public void addXpToFriendShip(int xp, Player player) {
        Friendship friendship = null;
        for (Friendship friendship1 : friendships) {
            if (friendship1.getPlayer().equals(player.getUser().getUsername())) {
                friendship = friendship1;
                break;
            }
        }
        if (friendship != null) {
            friendship.setXp(friendship.getXp() + xp);
        }
    }

    public void setFriendShipLevel(int level, Player player) {
        Friendship friendship = null;
        for (Friendship friendship1 : friendships) {
            if (friendship1.getPlayer().equals(player.getUser().getUsername())) {
                friendship = friendship1;
                break;
            }
        }
        if (friendship != null) {
            friendship.setLevel(level);
        }
    }

    public void addXpToNpcFriendship(int xp, NPC npc) {
        NPCFriendship friendship = null;
        for (NPCFriendship friendship1 : npcs) {
            if (friendship1.getNpc().equals(npc.getName())) {
                friendship = friendship1;
                break;
            }
        }
        if (friendship != null) {
            friendship.setXp(friendship.getXp() + xp);
        }
    }

    public int getMoneyInNextDay() {
        return moneyInNextDay;
    }

    public void setMoneyInNextDay(int moneyInNextDay) {
        this.moneyInNextDay = moneyInNextDay;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public ArrayList<MarriageRequest> getMarriageRequests() {
        return marriageRequests;
    }

    public void removeMarriageRequest(MarriageRequest marriageRequest) {
        marriageRequests.remove(marriageRequest);
    }

    public MarriageRequest findRequestByUsername(String username) {
        for (MarriageRequest marriageRequest : marriageRequests) {
            if (marriageRequest.getFrom().compareToIgnoreCase(username) == 0) {
                return marriageRequest;
            }
        }
        return null;
    }

    public void addNpcReward(NPCReward reward, NPC npc, Game game) {
        if (reward.getRewardItems() != null) {
            Slot slot = this.getInventory().getSlotByItemName(reward.getRewardItems().getName());
            int level = npc.findFriendshipByName(user.getUsername()).getLevel();
            int mp = level >= 2 ? 2 : 1;
            if (slot == null) {
                slot = new Slot(reward.getRewardItems(), reward.count * mp);
                this.getInventory().addSlot(slot);
            } else {
                slot.setCount(slot.getCount() + reward.count * mp);
            }
            this.addXpToNpcFriendship(200 * reward.friendshipLevel, npc);
            this.setMoney(this.money + reward.money * mp, game);
        }
    }

    public boolean isInVillage() {
        return isInVillage;
    }

    public void setInVillage(boolean inVillage) {
        isInVillage = inVillage;
    }
}
