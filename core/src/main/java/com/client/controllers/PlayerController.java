package com.client.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.client.utils.FacingDirection;
import com.client.utils.PlayerAnimationController;
import com.client.utils.PlayerState;
import com.common.models.Player;

public class PlayerController {
    private PlayerAnimationController playerAnimationController;
    private FacingDirection facingDirection;
    private PlayerState currState;
    private boolean isMoving = false;

    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;

    public PlayerController(Vector2 x, Vector2 y) {
        facingDirection = FacingDirection.DOWN;
        playerAnimationController = new PlayerAnimationController();
        currState = PlayerState.WALKING;
        this.playerPosition = x;
        this.playerVelocity = y;
    }

    public void update(float delta) {
        playerAnimationController.update(delta, facingDirection, currState);
    }

    public void render(Batch batch) {
        TextureRegion playerTexture = playerAnimationController.getCurrentFrame();
        float x = 20f;
        batch.draw(playerTexture, playerPosition.x - (float) playerTexture.getTexture().getWidth() / (2*x),
            playerPosition.y - (float) playerTexture.getTexture().getHeight() / (2*x), playerTexture.getRegionWidth()/x
            , playerTexture.getRegionHeight()/x);
    }

    private void handleStateChanges() {
        if (isMoving) {
            setState(PlayerState.WALKING);
        } else if (currState == PlayerState.WALKING) {
            setState(PlayerState.IDLE);
        }
    }

    public void setState(PlayerState state) {
        if (currState != state) {
            currState = state;
            playerAnimationController.changeState(state);
        }
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setFacingDirection(FacingDirection direction) {
        facingDirection = direction;
    }

    public void handlePlayerMove() {
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerVelocity.y = -20;
        }
    }
}
