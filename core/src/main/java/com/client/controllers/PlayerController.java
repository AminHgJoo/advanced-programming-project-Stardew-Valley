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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerController {
    private final ExecutorService networkThreadPool = Executors.newFixedThreadPool(4);
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
    private FarmMenu farmMenu;
    public float loadingTimer;


    public PlayerController(Vector2 x, Vector2 y, FarmMenu farmMenu, Player player) {
        this.farmMenu = farmMenu;
        this.player = player;
        facingDirection = FacingDirection.DOWN;
        currState = PlayerState.IDLE;
        playerAnimationController = new PlayerAnimationController(currState, facingDirection);
        playerService = new PlayerService(player, game);
        this.playerPosition = x;
        this.playerVelocity = y;
        this.width = playerAnimationController.getCurrentFrame().getTexture().getWidth();
        this.height = playerAnimationController.getCurrentFrame().getTexture().getHeight();
        loadingTimer = -1;
    }

    public void showLoading() {
        loadingTimer = 0.0f;
    }

    public void update(float delta) {
        playerAnimationController.update(delta, facingDirection, currState);
    }

    public void render(Batch batch) {
        TextureRegion playerTexture = playerAnimationController.getCurrentFrame();
        float scale = 3f;
        String playerName = player.getUser().getUsername();
        float nameOffset = 15f;
        float nameX = playerPosition.x - playerName.length() - 4;
        float nameY = playerPosition.y + (playerTexture.getRegionHeight() / (2 * scale)) + nameOffset;
        BitmapFont font = AssetManager.getStardewFont();
        font.getData().setScale(0.5f);
        font.draw(batch, playerName, nameX, nameY);
        font.getData().setScale(1f);
        if (player.emojiCounter >= 0) {
            batch.draw(player.currentEmoji, nameX, nameY, 32, 32);
            player.emojiCounter += 0.01f;
            if (player.emojiCounter >= 5) {
                player.emojiCounter = -1;
            }
        }


        batch.draw(playerTexture, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * scale),
            playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * scale), playerTexture.getRegionWidth() / scale
            , playerTexture.getRegionHeight() / scale);

        if (currState != PlayerState.TOOL_SWINGING && player.getEquippedItem() != null && player.getEquippedItem() instanceof Tool) {
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
        if (currState == PlayerState.TOOL_SWINGING &&player.getEquippedItem() instanceof Tool ) {
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
                    req.addProperty("x", playerPosition.x);
                    req.addProperty("y", playerPosition.y);
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

    public void updatePlayerPos(float delta) {
        float prev_x = playerPosition.x;
        float prev_y = playerPosition.y;
        playerPosition.x += playerVelocity.x * delta * FarmMenu.BASE_SPEED_FACTOR;
        playerPosition.x = MathUtils.clamp(playerPosition.x, width / 2, FarmMenu.FARM_X_SPAN * FarmMenu.TILE_PIX_SIZE - width / 2);

        playerPosition.y += playerVelocity.y * delta * FarmMenu.BASE_SPEED_FACTOR;
        playerPosition.y = MathUtils.clamp(playerPosition.y, height / 2, FarmMenu.FARM_Y_SPAN * FarmMenu.TILE_PIX_SIZE - height / 2);

        if ((prev_x != playerPosition.x || prev_y != playerPosition.y) && !player.isInVillage())
            updatePlayerCoordinate(prev_x, prev_y);
    }

    public void updatePlayerCoordinate(float prev_x, float prev_y) {
        boolean checkWalk = playerService.walk(playerPosition.x, playerPosition.y);
        if (checkWalk) {
            networkThreadPool.execute(() -> {
                JsonObject req = new JsonObject();
                req.addProperty("x", playerPosition.x);
                req.addProperty("y", playerPosition.y);
                var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/movementWalk", req);

                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                Gdx.app.postRunnable(() -> {
                    if (res.getStatus() == 200) {
                        player.setCoordinate(new Coordinate(playerPosition.x, playerPosition.y));
                    } else {
                        playerPosition.x = prev_x;
                        playerPosition.y = prev_y;
                    }
                });
            });
        } else {
            playerPosition.x = prev_x;
            playerPosition.y = prev_y;
        }
        if (playerPosition.x == 2366.5f && playerPosition.y >= 67f) {
            System.out.println("hello");
            playerVelocity.y = 0;
            playerVelocity.x = 0;
            currState = PlayerState.IDLE;
            farmMenu.showGoToVillagePopUp();
        }
    }

    public void goToVillage() {
        player.setInVillage(true);
        var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/movementGoToVillage", new JsonObject());
        Response res = HTTPUtil.deserializeHttpResponse(postResponse);
        System.out.println(res.getMessage());
        if (res.getStatus() == 200) {

        } else {

        }
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
        networkThreadPool.execute(() -> {
            JsonObject req = new JsonObject();
            if (item != null) {
                req.addProperty("toolName", item.getName());
            }
            var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/inventoryToolEquip", req);
            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
            Gdx.app.postRunnable(() -> {

                if (res.getStatus() == 200) {
//                    String player = res.getBody().toString();
//                    updatePlayerObject(player);
                } else {

                }
            });
        });
    }

    public void updateInventory(Backpack backpack) {
        System.out.println("HI");
        networkThreadPool.execute(() -> {
            String json = GameGSON.gson.toJson(backpack);
            JsonObject req = new JsonObject();
            req.addProperty("backpack", json);
            var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/inventoryChangeInventory", req);
            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
            Gdx.app.postRunnable(() -> {
                if (res.getStatus() == 200) {
//            String player = res.getBody().toString();
//            updatePlayerObject(player);
                }
            });
        });
    }

    public void updateFridge(ArrayList<Slot> slots) {
        networkThreadPool.execute(() -> {
            String json = GameGSON.gson.toJson(slots);
            JsonObject req = new JsonObject();
            req.addProperty("slots", json);
            var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/inventoryUpdateFridge", req);

            Response res = HTTPUtil.deserializeHttpResponse(postResponse);

            if (res.getStatus() == 200) {
                System.out.println("Fridge updated");
            } else {
                System.out.println("Fridge update failed");
            }

            System.out.println(res.getBody().toString());
        });
    }
}
