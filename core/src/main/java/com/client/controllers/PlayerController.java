package com.client.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.client.ClientApp;
import com.client.utils.FacingDirection;
import com.client.utils.HTTPUtil;
import com.client.utils.PlayerAnimationController;
import com.client.utils.PlayerState;
import com.client.views.inGameMenus.FarmMenu;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.mapModels.Coordinate;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.HashMap;

public class PlayerController {
    private PlayerAnimationController playerAnimationController;
    private FacingDirection facingDirection;
    private PlayerState currState;
    private boolean isMoving = false;
    private Player player = ClientApp.currentPlayer;
    private GameData game = ClientApp.currentGameData;

    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;
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
        float x = 3f;
        batch.draw(playerTexture, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * x),
            playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * x), playerTexture.getRegionWidth() / x
            , playerTexture.getRegionHeight() / x);
        if (currState == PlayerState.TOOL_SWINGING) {
            if (facingDirection == FacingDirection.DOWN) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-1.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-2.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * x) + (float) t.getWidth() / (2 * x) - 3f,
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * x) + (float) t.getHeight() / (2 * x) - 3f,
                    (float) t.getWidth() / x, (float) t.getHeight() / x);
            } else if (facingDirection == FacingDirection.UP) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-4.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-5.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * x) + (float) t.getWidth() / (2 * x) - 3f,
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * x) + (float) t.getHeight() / (2 * x) + 15f,
                    (float) t.getWidth() / x, (float) t.getHeight() / x);
            } else if (facingDirection == FacingDirection.LEFT) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-7.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-8.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * x) + (float) t.getWidth() / (2 * x) - 15f,
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * x) + (float) t.getHeight() / (2 * x) - 3f,
                    (float) t.getWidth() / x, (float) t.getHeight() / x);
            } else if (facingDirection == FacingDirection.RIGHT) {
                int index = playerAnimationController.getCurrentFrameIndex();
                Texture t;
                if (index == 0) {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-3.png"));
                } else {
                    t = new Texture(Gdx.files.internal("images/player/tool/hoe/hoe-6.png"));
                }
                batch.draw(t, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2 * x) + (float) t.getWidth() / (2 * x) - 3f,
                    playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2 * x) + (float) t.getHeight() / (2 * x) - 3f,
                    (float) t.getWidth() / x, (float) t.getHeight() / x);
            }
        }
    }

    private void handleStateChanges() {
        if (isMoving) {
            setState(PlayerState.WALKING);
        } else if (currState == PlayerState.WALKING) {
            setState(PlayerState.IDLE);
        }
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
        JsonObject req = new JsonObject();
        req.addProperty("x", playerPosition.x);
        req.addProperty("y", playerPosition.y);
        var postResponse = HTTPUtil.post("http://localhost:8080/api/game/" + game.get_id() + "/movementWalk", req);

        Response res = HTTPUtil.deserializeHttpResponse(postResponse);
        if (res.getStatus() == 200) {
            player.setCoordinate(new Coordinate(playerPosition.x, playerPosition.y));
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
}
