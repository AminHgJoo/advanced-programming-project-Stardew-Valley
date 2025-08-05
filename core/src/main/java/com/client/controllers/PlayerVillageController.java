package com.client.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.client.ClientApp;
import com.client.services.PlayerService;
import com.client.utils.*;
import com.client.views.inGameMenus.FarmMenu;
import com.client.views.inGameMenus.VillageMenu;
import com.common.GameGSON;
import com.common.models.Backpack;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.items.Item;
import com.common.models.items.Tool;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.EmptyCell;
import com.common.models.mapObjects.Water;
import com.google.gson.JsonObject;
import com.server.controllers.InGameControllers.WorldController;
import com.server.utilities.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerVillageController {
    private final ExecutorService networkThreadPool = Executors.newFixedThreadPool(2);
    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;
    private PlayerAnimationController playerAnimationController;
    private FacingDirection facingDirection;
    private PlayerState currState;
    private boolean isMoving = false;
    private Player player;
    private GameData game = ClientApp.currentGameData;
    private PlayerService playerService;
    private float width;
    private float height;
    private VillageMenu villageMenu;
    private HashMap<String, Coordinate> stores = new HashMap<>();

    public PlayerVillageController(Vector2 x, Vector2 y, VillageMenu villageMenu, Player player) {
        this.villageMenu = villageMenu;
        this.player = player;
        facingDirection = FacingDirection.DOWN;
        currState = PlayerState.IDLE;
        playerAnimationController = new PlayerAnimationController(currState, facingDirection);
        playerService = new PlayerService(player, game);
        this.playerPosition = x;
        this.playerVelocity = y;
        this.width = playerAnimationController.getCurrentFrame().getTexture().getWidth();
        this.height = playerAnimationController.getCurrentFrame().getTexture().getHeight();
        initStores();
    }

    public void initStores() {
        // 1232.6101 , 527.6693 ->  star drop
        // 1480.6656 , 535.89667 -> jojamart
        // 597.03845 , 522.557 -> pierre
        // 1572.1227 , 772.8281 -> fish
        // 555.7794 , 766.0151 -> carpenter
        // 1176.92 , 761.74866 -> blacksmith
        // 1262.0074 , 254.55693 -> ranch
        stores.put("The Stardrop Saloon", new Coordinate(1232.6101f, 527.6693f));
        stores.put("JojaMart", new Coordinate(1480.6656f, 535.89667f));
        stores.put("Pierre's General Store", new Coordinate(597.03845f, 522.557f));
        stores.put("Fish Shop", new Coordinate(1572.1227f, 772.8281f));
        stores.put("Carpenter's Shop", new Coordinate(555.7794f, 766.0151f));
        stores.put("Blacksmith", new Coordinate(1176.92f, 761.74866f));
        stores.put("Marnie's Ranch", new Coordinate(1262.0074f, 254.55693f));
    }

    public void update(float delta) {
        playerAnimationController.update(delta, facingDirection, currState);
    }

    public void render(Batch batch) {
        TextureRegion playerTexture = playerAnimationController.getCurrentFrame();
        float scale = 3f;
        String playerName = player.getUser().getUsername();
        float nameOffset = 20f;
        float nameX = playerPosition.x - (playerTexture.getRegionWidth() / (2 * scale));
        float nameY = playerPosition.y + (playerTexture.getRegionHeight() / scale) + nameOffset;
        BitmapFont font = AssetManager.getStardewFont();
        font.draw(batch, playerName, nameX, nameY);
        if (player.emojiCounter >= 0) {
            batch.draw(player.currentEmoji, nameX + 4, nameY, 64, 64);
            player.emojiCounter += 0.01f;
            if (player.emojiCounter >= 5) {
                player.emojiCounter = -1;
            }
        }

        batch.draw(playerTexture, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale),
            playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale), playerTexture.getRegionWidth() / scale
            , playerTexture.getRegionHeight() / scale);
        if (currState != PlayerState.TOOL_SWINGING && player.getEquippedItem() != null) {
            if (facingDirection == FacingDirection.DOWN) {
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());
                t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "1.png"));
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale),
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            } else if (facingDirection == FacingDirection.UP) {
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());
                t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "3.png"));
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale),
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            } else if (facingDirection == FacingDirection.RIGHT) {
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());
                t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "7.png"));
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale),
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            } else if (facingDirection == FacingDirection.LEFT) {
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());
                t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "5.png"));
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale),
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            }
        }
        if (currState == PlayerState.TOOL_SWINGING) {
            if (player.getEquippedItem().getName().contains("Rod")) {
                return;
            }
            if (facingDirection == FacingDirection.DOWN) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());
                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "1.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "2.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale),
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            } else if (facingDirection == FacingDirection.UP) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());

                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "3.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "4.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale),
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            } else if (facingDirection == FacingDirection.RIGHT) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());

                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "7.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "8.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale),
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            } else if (facingDirection == FacingDirection.LEFT) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                String picName = StringUtils.convertToolNameToAssetName(player.getEquippedItem().getActualName());

                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "5.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/" + picName + "/" + picName + "6.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale) + (float) t.getWidth() / (2 * scale) - 10f,
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale) + (float) t.getHeight() / (2 * scale),
                    (float) t.getWidth() / scale, (float) t.getHeight() / scale);
            }
        }
    }

    public void toolUse() {
        try {
            Tool tool = (Tool) player.getEquippedItem();
            boolean check = playerService.toolUse();
            if (check) {
                networkThreadPool.execute(() -> {
                    JsonObject req = new JsonObject();
                    String dir = facingDirection.getDirection();
                    req.addProperty("direction", dir);
                    req.addProperty("toolName", tool.getName());
                    var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/worldToolUse", req);

                    Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                    Gdx.app.postRunnable(() -> {
                        if (res.getStatus() == 200) {
                            System.out.println(res.getMessage());
                            String playerJson = res.getBody().toString();
                            updatePlayerObject(playerJson);
                        } else {
                            System.out.println(res.getMessage());
                        }
                    });
                });
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean useFishingRod() {
        Tool tool = (Tool) player.getEquippedItem();
        String direction = facingDirection.getDirection();
        float[] dxAndDy = WorldController.getXAndYIncrement(direction);
        float dx = dxAndDy[0];
        float dy = dxAndDy[1];

        Farm farm = player.getCurrentFarm(game);

        float playerX = (player.getCoordinate().getX() + dx) / 32;
        float playerY = 50 - (player.getCoordinate().getY() + dy) / 32;
        Cell targetCell = farm.findCellByCoordinate(playerX, playerY);

        if (targetCell == null) {
            return false;
        }
        if (!(targetCell.getObjectOnCell() instanceof Water)) {
            return false;
        }
        return true;
    }

    private void handleStateChanges() {
        if (isMoving) {
            setState(PlayerState.WALKING);
        } else if (currState == PlayerState.WALKING) {
            setState(PlayerState.IDLE);
        }
    }

    public boolean dropItem(Player player, Farm farm) {
        Item item = player.getEquippedItem();
        Cell cell = farm.findCellByCoordinate(player.getCoordinate().getX() / 32
            , 49 - player.getCoordinate().getY() / 32);
        boolean check = playerService.dropItem(player, farm, item, cell);
        if (check) {
            networkThreadPool.execute(() -> {
                JsonObject req = new JsonObject();
                req.addProperty("itemName", ((Tool) item).getName());
                var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/inventoryPlaceItem", req);

                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                System.out.println(res.getMessage());
                Gdx.app.postRunnable(() -> {
                    if (res.getStatus() != 200) {
                        player.setEquippedItem(item);
                        player.getInventory().addSlot(new Slot(item, 1));
                        cell.setObjectOnCell(new EmptyCell());
                    }
                });
            });
        }
        return check;
    }

    private Cell findCell(Coordinate coordinate, ArrayList<Cell> cells) {
        int x = (int) coordinate.getX();
        int y = (int) coordinate.getY();
        for (Cell cell : cells) {
            if (x == cell.getCoordinate().getX() && y == cell.getCoordinate().getY()) {
                return cell;
            }
        }
        return null;
    }

    public void setState(PlayerState state) {
        currState = state;
        playerAnimationController.changeState(state);
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setFacingDirection(FacingDirection direction) {
        facingDirection = direction;
    }

    public void handleKeyUp(float x, float y) {
        playerVelocity.x = x;
        playerVelocity.y = y;
        if (x != 0) {
            if (x > 0) {
                facingDirection = FacingDirection.RIGHT;
            } else {
                facingDirection = FacingDirection.LEFT;
            }
        } else {
            if (y > 0) {
                facingDirection = FacingDirection.UP;
            } else {
                facingDirection = FacingDirection.DOWN;
            }
        }
    }

    public void handleKeyDown() {
        playerVelocity.x = 0;
        playerVelocity.y = 0;
    }
    // x : 33.5 - 822.5166 | y : 426.72534 - 524.69574
    // x : 822.5166 - 984.9261 | y : 0 - 927.7919
    // x : 984.1325 - 1889.6036 | y : 426.72534 , 524.69574
    // x : 984.1325 , 1889.6036 | y : 740.0952 , 770.0764

    // x : 822.5166 , 385.32944 | y : 740.0952 , 770.0764
    // x : 984.1325 , 1889.6036 | Y : 185.20575  , 254.1898


    public boolean checkToBeInSideWalk() {
        float x = playerPosition.x;
        float y = playerPosition.y;
        if (isInRange(x, 33.5f, 822.5166f) && isInRange(y, 426.72534f, 524.69574f)) return true;
        if (isInRange(x, 822.5166f, 984.9261f) && isInRange(y, 0, 927.7919f)) return true;
        if (isInRange(x, 984.1325f, 1889.6036f) && isInRange(y, 426.72534f, 524.69574f)) return true;
        if (isInRange(x, 984.1325f, 1889.6036f) && isInRange(y, 740.0952f, 770.0764f)) return true;
        if (isInRange(x, 385.32944f, 822.5166f) && isInRange(y, 740.0952f, 770.0764f)) return true;
        if (isInRange(x, 984.1325f, 1889.6036f) && isInRange(y, 185.20575f, 254.1898f)) return true;
        return false;
    }

    public boolean isInRange(float x, float min, float max) {
        return x >= min && x <= max;
    }

    public void updatePlayerPos(float delta) {
        float prev_x = playerPosition.x;
        float prev_y = playerPosition.y;
        playerPosition.x += playerVelocity.x * delta * FarmMenu.BASE_SPEED_FACTOR;
        playerPosition.x = MathUtils.clamp(playerPosition.x, width / 2, 1889.6036f);

        playerPosition.y += playerVelocity.y * delta * FarmMenu.BASE_SPEED_FACTOR;
        playerPosition.y = MathUtils.clamp(playerPosition.y, height / 2, 927.7919f);

        if (!checkToBeInSideWalk()) {
            playerPosition.y = prev_y;
            playerPosition.x = prev_x;
        }
        if ((prev_x != playerPosition.x || prev_y != playerPosition.y))
            updatePlayerCoordinate();
        if (playerPosition.y >= 850) {
            playerVelocity.x = 0;
            playerVelocity.y = 0;
            currState = PlayerState.IDLE;
            villageMenu.showGoToFarmPopUp();
        }
    }

    public void checkStoreEntry() {
        String storeName = null;
        for (Map.Entry<String, Coordinate> entry : stores.entrySet()) {
            Coordinate coordinate = entry.getValue();
            if (checkInRange(playerPosition.x, coordinate.getX(), 20) && checkInRange(playerPosition.y, coordinate.getY(), 20)) {
                storeName = entry.getKey();
                break;
            }
        }
        if (storeName != null && facingDirection == FacingDirection.UP) {
            System.out.println(storeName);
            playerVelocity.x = 0;
            playerVelocity.y = 0;
            currState = PlayerState.IDLE;
            this.villageMenu.showGoToStorePopUp(storeName);
        }
    }

    public boolean checkInRange(float x, float y, float range) {
        return (x >= y - range) && (x <= y + range);
    }

    public void updatePlayerCoordinate() {
        networkThreadPool.execute(() -> {
            JsonObject req = new JsonObject();
            req.addProperty("x", playerPosition.x);
            req.addProperty("y", playerPosition.y);
            var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/movementWalkInVillage", req);

            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
            Gdx.app.postRunnable(() -> {
                if (res.getStatus() == 200) {
                    player.setCoordinate(new Coordinate(playerPosition.x, playerPosition.y));
                } else {
                    playerPosition.x = player.getCoordinate().getX();
                    playerPosition.y = player.getCoordinate().getY();
                }
            });
        });
        checkStoreEntry();
    }

    public void goToVillage() {
        player.setInVillage(true);
        networkThreadPool.execute(() -> {
            var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/movementGoToVillage", null);
            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
            Gdx.app.postRunnable(() -> {
                if (res.getStatus() == 200) {

                } else {

                }
            });
        });
    }

    public void goToFarm() {

    }

    public Player getPlayer() {
        return player;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void handleServerPlayerMove(HashMap<String, String> res) {
        String playerId = res.get("player_user_id");
        if (playerId.equals(player.getUser_id())) return;
        Player p = game.findPlayerByUserId(playerId);
        if (p == null) return;
        float x = Float.parseFloat(res.get("x"));
        float y = Float.parseFloat(res.get("y"));
        p.setCoordinate(new Coordinate(x, y));
    }

    public void updatePlayerObject(String json) {
        Player p = GameGSON.gson.fromJson(json, Player.class);
        this.player = p;
        ClientApp.currentPlayer = player;
        game.setPlayerById(p.getUser_id(), p);
    }

    public void updateAnotherPlayerObject(String json) {
        Player p = GameGSON.gson.fromJson(json, Player.class);
        game.setPlayerById(p.getUser_id(), p);
    }

    public void updateGame(String json) {
        GameData gameData = GameGSON.gson.fromJson(json, GameData.class);
        this.game = gameData;
        ClientApp.currentGameData = gameData;
        this.player = gameData.findPlayerByUserId(player.getUser_id());
        ClientApp.currentPlayer = this.player;
    }

    public void sendEmoji(int index) {
        networkThreadPool.execute(() -> {
            JsonObject req = new JsonObject();
            req.addProperty("index", index);
            var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/friendshipSendEmoji", req);
            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
            Gdx.app.postRunnable(() -> {
                if (res.getStatus() == 200) {

                } else {

                }
            });
        });
    }

    public void equipItem(Item item) {
        player.setEquippedItem(item);
        JsonObject req = new JsonObject();
        if (item != null) {
            req.addProperty("toolName", item.getName());
        }
        var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/inventoryToolEquip", req);
        Response res = HTTPUtil.deserializeHttpResponse(postResponse);
        if (res.getStatus() == 200) {
            String player = res.getBody().toString();
            updatePlayerObject(player);
        } else {

        }
    }

    public void updateInventory(Backpack backpack) {
        System.out.println("HI");
        String json = GameGSON.gson.toJson(backpack);
        JsonObject req = new JsonObject();
        req.addProperty("backpack", json);
        var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/inventoryChangeInventory", req);
        Response res = HTTPUtil.deserializeHttpResponse(postResponse);
        if (res.getStatus() == 200) {
            String player = res.getBody().toString();
            updatePlayerObject(player);
        }
    }

    public void updatePlayer(Player player) {
        this.player = player;
    }

    public void updateGamePlayer(Player player) {
        game.setPlayerById(this.player.getUser_id(), player);
        updatePlayer(player);
    }

    public void updateGame() {
        this.game = ClientApp.currentGameData;
        updatePlayer(game.findPlayerByUserId(player.getUser_id()));
    }
}
