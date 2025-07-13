package com.client.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;

public class PlayerAnimationController {
    private PlayerState currentState;
    private float stateTime;
    private ArrayMap<PlayerState, Animation<TextureRegion>[]> animations;
    private FacingDirection facingDirection;

    public PlayerAnimationController() {
        stateTime = 0;
        animations = new ArrayMap<>();
        facingDirection = FacingDirection.DOWN;
        currentState = PlayerState.WALKING;
        initializeAnimations();
    }

    private void initializeAnimations() {
        animations.put(PlayerState.WALKING, loadAnimation(3));
    }

    private Animation<TextureRegion>[] loadAnimation(int frameCount) {
        Animation<TextureRegion>[] res = new Animation[4];
        for (int i = 0; i < 1; i++) {
            TextureRegion[] regions = new TextureRegion[frameCount];
            for (int frame = 0; frame < frameCount; frame++) {
                TextureRegion region = new TextureRegion(new Texture("images/player/down/0"+(frame + 1)+".png"));
                regions[frame] = region;
            }
            Animation<TextureRegion> anim = new Animation<TextureRegion>(.2f, regions);
            res[i] = anim;
        }
        return res;
    }

    public void update(float delta, FacingDirection direction , PlayerState state) {
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
        Animation<TextureRegion> anim = animations.get(currentState)[facingDirection.getAnimationRow()];
        return anim.getKeyFrame(stateTime, true);
    }

}
