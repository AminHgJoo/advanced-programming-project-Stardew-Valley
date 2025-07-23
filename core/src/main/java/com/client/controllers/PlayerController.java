package com.client.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.client.ClientApp;
import com.client.services.PlayerService;
import com.client.utils.*;
import com.client.views.inGameMenus.FarmMenu;
import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.items.Item;
import com.common.models.items.Tool;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.EmptyCell;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerController {
    private final ExecutorService networkThreadPool = Executors.newFixedThreadPool(2);
    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;
    private PlayerAnimationController playerAnimationController;
    private FacingDirection facingDirection;
    private PlayerState currState;
    private boolean isMoving = false;
    private Player player = ClientApp.currentPlayer;
    private GameData game = ClientApp.currentGameData;
    private PlayerService playerService = new PlayerService(player, game);
    private float width;
    private float height;

    public PlayerController(Vector2 x, Vector2 y) {
        facingDirection = FacingDirection.DOWN;
        currState = PlayerState.IDLE;
        playerAnimationController = new PlayerAnimationController(currState, facingDirection);
        this.playerPosition = x;
        this.playerVelocity = y;
        this.width = playerAnimationController.getCurrentFrame().getTexture().getWidth();
        this.height = playerAnimationController.getCurrentFrame().getTexture().getHeight();
    }

    public void update(float delta) {
        playerAnimationController.update(delta, facingDirection, currState);
    }

    public void render(Batch batch) {
        TextureRegion playerTexture = playerAnimationController.getCurrentFrame();
        float scale = 3f;
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
                    var postResponse = HTTPUtil.post("http://localhost:8080/api/game/" + game.get_id() + "/worldToolUse", req);

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
                var postResponse = HTTPUtil.post("http://localhost:8080/api/game/" + game.get_id() + "/inventoryPlaceItem", req);

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

        if (prev_x != playerPosition.x || prev_y != playerPosition.y)
            updatePlayerCoordinate();
    }

    public void updatePlayerCoordinate() {
        boolean checkWalk = playerService.walk(playerPosition.x, playerPosition.y);
        if (checkWalk) {
            networkThreadPool.execute(() -> {
                JsonObject req = new JsonObject();
                req.addProperty("x", playerPosition.x);
                req.addProperty("y", playerPosition.y);
                var postResponse = HTTPUtil.post("http://localhost:8080/api/game/" + game.get_id() + "/movementWalk", req);

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
        } else {
            playerPosition.x = player.getCoordinate().getX();
            playerPosition.y = player.getCoordinate().getY();
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
}
