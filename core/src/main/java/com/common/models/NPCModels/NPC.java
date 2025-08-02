package com.common.models.NPCModels;

import com.badlogic.gdx.math.Vector2;
import com.client.utils.NpcState;
import com.common.models.GameData;
import com.common.models.Quest;
import com.common.models.enums.types.itemTypes.*;
import com.common.models.items.*;
import com.common.models.mapModels.Coordinate;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Embedded
public class NPC {
    private String name;
    private ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
    private ArrayList<NPCRequest> npcRequests = new ArrayList<>();
    private ArrayList<NPCReward> rewards = new ArrayList<>();
    private Coordinate coordinate;
    private ArrayList<NPCFriendship> friendships = new ArrayList<>();
    private ArrayList<Quest> quests = new ArrayList<>();
    private HashMap<String, Boolean> hasTalked = new HashMap<>();
    private HashMap<String, Boolean> gift = new HashMap<>();

    @Transient
    private Vector2 targetPosition;
    @Transient
    private NpcState state = NpcState.IDLE;
    @Transient
    private float waitTimer;
    @Transient
    private Vector2 position;

    public NPC() {
        targetPosition = null;
    }

    public NPC(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
        initializeNpcQuest(name);
        initializeNpcFavorites(name);
        initializeNpcRewards(name);
        position = new Vector2(coordinate.getX(), coordinate.getY());
        targetPosition = null;
    }

    public void update(float delta) {
        switch (state) {
            case IDLE:
                chooseNewTarget();
                break;
            case WALKING_TO_TARGET:
                if (coordinate.getY() == targetPosition.y && coordinate.getX() == targetPosition.x) {
                    state = NpcState.WAITING_AT_TARGET;
                }
                break;
            case WAITING_AT_TARGET:
                targetPosition = null;
                waitTimer -= delta;
                if (waitTimer <= 0) {
                    state = NpcState.IDLE;
                }
                break;

        }
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    private void moveTowardsTarget(float deltaTime) {
        Vector2 direction = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y);
        float distance = direction.len();
        Random random = new Random();
        if (distance < 5f) {
            position.set(targetPosition);
            waitTimer = 10;
            targetPosition = null;
            state = NpcState.WAITING_AT_TARGET;
            return;
        }

        if (!(direction.x > -5 && direction.x < 5)) {
            position.x += direction.x * deltaTime;
        } else if (!(direction.y > -5 && direction.y < 5)) {
            position.y += direction.y * deltaTime;
        }

        this.coordinate = new Coordinate(position.x, position.y);

        if (!isInSidewalk(position.x, position.y)) {
            chooseNewTarget();
        }
    }

    private void chooseNewTarget() {
        float x, y;
        Random random = new Random();
        do {
            x = 33.5f + random.nextFloat() * (1889.6036f - 33.5f);
            y = 0 + random.nextFloat() * (927.7919f - 0);
        } while (!isInSidewalk(x, y));

        targetPosition = new Vector2(x, y);
        state = NpcState.WALKING_TO_TARGET;
    }

    private boolean isInSidewalk(float x, float y) {
        if (isInRange(x, 33.5f, 822.5166f) && isInRange(y, 426.72534f, 524.69574f)) return true;
        if (isInRange(x, 822.5166f, 984.9261f) && isInRange(y, 0, 927.7919f)) return true;
        if (isInRange(x, 984.1325f, 1889.6036f) && isInRange(y, 426.72534f, 524.69574f)) return true;
        if (isInRange(x, 984.1325f, 1889.6036f) && isInRange(y, 740.0952f, 770.0764f)) return true;
        if (isInRange(x, 385.32944f, 822.5166f) && isInRange(y, 740.0952f, 770.0764f)) return true;
        if (isInRange(x, 984.1325f, 1889.6036f) && isInRange(y, 185.20575f, 254.1898f)) return true;
        return false;
    }

    private boolean isInRange(float value, float min, float max) {
        return value >= min && value <= max;
    }

    public String getName() {
        return name;
    }

    public ArrayList<FavoriteItem> getFavoriteItems() {
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

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void initializeNpcQuest(String name) {
        if (name.equals("Sebastian")) {
            quests.add(new Quest(ForagingMineralsType.IRON_ORE, 50));
            quests.add(new Quest(FoodTypes.PUMPKIN_PIE, 1));
            quests.add(new Quest(ForagingMineralsType.STONE, 150));
        } else if (name.equals("Abigail")) {
            quests.add(new Quest(MiscType.GOLD_BAR, 1));
            quests.add(new Quest(FoodTypes.PUMPKIN, 1));
            quests.add(new Quest(FoodTypes.WHEAT, 50));
        } else if (name.equals("Harvey")) {
            quests.add(new Quest(CropSeedsType.BLUE_JAZZ, 12));
            quests.add(new Quest(FishType.SALMON, 1));
            quests.add(new Quest(FoodTypes.WINE, 1));
        } else if (name.equals("Leah")) {
            quests.add(new Quest(MiscType.WOOD, 10));
            quests.add(new Quest(FishType.SALMON, 1));
            quests.add(new Quest(MiscType.WOOD, 200));
        } else {
            quests.add(new Quest(MiscType.WOOD, 80));
            quests.add(new Quest(MiscType.IRON_BAR, 10));
            quests.add(new Quest(MiscType.WOOD, 1000));
        }
    }

    public void initializeNpcFavorites(String name) {
        if (name.equals("Sebastian")) {
            favoriteItems.add(new FavoriteItem(MiscType.WOOL));
            favoriteItems.add(new FavoriteItem(FoodTypes.PUMPKIN_PIE));
            favoriteItems.add(new FavoriteItem(FoodTypes.PIZZA));
        } else if (name.equals("Abigail")) {
            favoriteItems.add(new FavoriteItem(ForagingMineralsType.STONE));
            favoriteItems.add(new FavoriteItem(ForagingMineralsType.IRON_ORE));
            favoriteItems.add(new FavoriteItem(FoodTypes.COFFEE));
        } else if (name.equals("Harvey")) {
            favoriteItems.add(new FavoriteItem(FoodTypes.COFFEE));
            favoriteItems.add(new FavoriteItem(FoodTypes.PICKLES));
            favoriteItems.add(new FavoriteItem(FoodTypes.WINE));
        } else if (name.equals("Leah")) {
            favoriteItems.add(new FavoriteItem(FoodTypes.SALAD));
            favoriteItems.add(new FavoriteItem(FoodTypes.GRAPE));
            favoriteItems.add(new FavoriteItem(FoodTypes.WINE));
        } else {
            favoriteItems.add(new FavoriteItem(FoodTypes.SPAGHETTI));
            favoriteItems.add(new FavoriteItem(MiscType.WOOD));
            favoriteItems.add(new FavoriteItem(MiscType.IRON_BAR));
        }
    }

    public void initializeNpcRewards(String name) {
        if (name.equals("Sebastian")) {
            rewards.add(new NPCReward(0, ForagingMineralsType.DIAMOND, 0, 3));
            rewards.add(new NPCReward(5000, null, 0, 0));
            rewards.add(new NPCReward(0, ForagingMineralsType.QUARTZ, 0, 50));
        } else if (name.equals("Abigail")) {
            rewards.add(new NPCReward(0, null, 1, 0));
            rewards.add(new NPCReward(500, null, 0, 0));
            rewards.add(new NPCReward(0, MiscType.IRIDIUM_SPRINKLER, 0, 1));
        } else if (name.equals("Harvey")) {
            rewards.add(new NPCReward(750, null, 0, 0));
            rewards.add(new NPCReward(0, null, 1, 0));
            rewards.add(new NPCReward(0, FoodTypes.SALAD, 0, 5));
        } else if (name.equals("Leah")) {
            rewards.add(new NPCReward(500, null, 0, 0));
            rewards.add(new NPCReward(0, FoodTypes.SALMON_DINNER, 0, 1));
            rewards.add(new NPCReward(0, MiscType.DELUXE_SCARE_CROW, 0, 3));
        } else {
            rewards.add(new NPCReward(1000, null, 0, 0));
            rewards.add(new NPCReward(0, MiscType.BEE_HOUSE, 0, 3));
            rewards.add(new NPCReward(25000, MiscType.BEE_HOUSE, 0, 0));
        }
    }

    public HashMap<String, Boolean> getHasTalked() {
        return hasTalked;
    }

    public void setHasTalked(HashMap<String, Boolean> hasTalked) {
        this.hasTalked = hasTalked;
    }

    public HashMap<String, Boolean> getGift() {
        return gift;
    }

    public void setGift(HashMap<String, Boolean> gift) {
        this.gift = gift;
    }

    public boolean isItemInFavorite(Item item) {
        ItemType type = null;
        if (item instanceof Fish) {
            type = ((Fish) item).getFishType();
        } else if (item instanceof Food) {
            type = ((Food) item).foodTypes;
        } else if (item instanceof ForagingMineralItem) {
            type = ((ForagingMineralItem) item).getType();
        } else if (item instanceof Misc) {
            type = ((Misc) item).getMiscType();
        } else if (item instanceof Seed) {
            type = ((Seed) item).getCropType();
        } else if (item instanceof TreeSeed) {
            type = ((TreeSeed) item).getTreeSeedsType();
        }
        if (favoriteItems.contains(new FavoriteItem(type))) {
            return true;
        }
        return false;
    }

    public String questsToString(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("Quests : \n");
        for (int i = 0; i < (level >= 1 ? quests.size() : quests.size() - 1); i++) {
            if (!quests.get(i).isCompleted())
                sb.append(i).append(" : ").append(quests.get(i).toString()).append("\n");
        }
        return sb.toString();
    }

    public NPCFriendship findFriendshipByName(String name) {
        for (NPCFriendship friendship : friendships) {
            if (friendship.getPlayer().compareToIgnoreCase(name) == 0) {
                return friendship;
            }
        }
        return null;
    }

    public String context(GameData game, String username) {
        return "You are a NPC in stardew valley game  , you are in the village , your name is " +
            name + " and " + username + " is talking to you. The date of now is " + game.getDate().toString() +
            "and the weather is " + game.getWeatherToday();
    }
}
