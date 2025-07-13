package com.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;

public class PlayerAnimationController {
    private PlayerState currentState;
    private float stateTime;
    private ArrayMap<PlayerState, Animation<TextureRegion>[]> animations;
    private FacingDirection facingDirection;

    public PlayerAnimationController(PlayerState currentState , FacingDirection facingDirection) {
        stateTime = 0;
        animations = new ArrayMap<>();
        this.facingDirection = facingDirection;
        this.currentState = currentState;
        initializeAnimations();
    }

    private void initializeAnimations() {
        animations.put(PlayerState.WALKING, loadAnimation(2));
    }

    private Animation<TextureRegion>[] loadAnimation(int frameCount) {
        Animation<TextureRegion>[] res = new Animation[4];
        for (int i = 0; i < 4; i++) {
            TextureRegion[] regions = new TextureRegion[frameCount];
            for (int frame = 0; frame < frameCount; frame++) {
                TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal("images/player/" + i + "-" + (frame + 2) + ".png")));
                regions[frame] = region;
            }
            Animation<TextureRegion> anim = new Animation<TextureRegion>(.2f, regions);
            res[i] = anim;
        }
        return res;
    }

    public void update(float delta, FacingDirection direction, PlayerState state) {
        stateTime += delta;
        currentState = state;
        facingDirection = direction;
    }


    public void changeState(PlayerState newState) {
        if (currentState != newState) {
            currentState = newState;
            stateTime = 0;
        }
    }

    public TextureRegion getCurrentFrame() {
        if (currentState == PlayerState.IDLE) {
            Texture t = new Texture("images/player/" + facingDirection.getAnimationRow() + "-1.png");
            return new TextureRegion(t);
        }
        Animation<TextureRegion> anim = animations.get(currentState)[facingDirection.getAnimationRow()];
        return anim.getKeyFrame(stateTime, true);
    }

}
