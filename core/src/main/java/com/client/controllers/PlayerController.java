package com.client.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.client.utils.FacingDirection;
import com.client.utils.PlayerAnimationController;
import com.client.utils.PlayerState;

public class PlayerController {
    private PlayerAnimationController playerAnimationController;
    private FacingDirection facingDirection;
    private PlayerState currState;
    private boolean isMoving = false;

    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;

    public PlayerController(Vector2 x, Vector2 y) {
        facingDirection = FacingDirection.DOWN;
        currState = PlayerState.IDLE;
        playerAnimationController = new PlayerAnimationController(currState, facingDirection);
        this.playerPosition = x;
        this.playerVelocity = y;
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


}
