package com.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;

public class NpcAnimationController {
    private PlayerState currentState;
    private float stateTime;
    private ArrayMap<PlayerState, Animation<TextureRegion>[]> animations;
    private FacingDirection facingDirection;
    private String name;

    public NpcAnimationController(PlayerState currentState, FacingDirection facingDirection, String name) {
        this.name = name;
        stateTime = 0;
        animations = new ArrayMap<>();
        this.facingDirection = facingDirection;
        this.currentState = currentState;
        initializeAnimations();
    }

    private void initializeAnimations() {
        animations.put(PlayerState.WALKING, loadAnimation(2, .2f));
    }

    private Animation<TextureRegion>[] loadAnimation(int frameCount, float duration) {
        Animation<TextureRegion>[] res = new Animation[4];
        for (int i = 0; i < 4; i++) {
            TextureRegion[] regions = new TextureRegion[frameCount];
            for (int frame = 0; frame < frameCount; frame++) {
                try {
                    Texture t = new Texture(Gdx.files
                        .internal("images/npc/" + name + "/" + i + "-" + (frame + 1) + ".png"));
                    TextureRegion region = new TextureRegion(t);
                    regions[frame] = region;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Animation<TextureRegion> anim = new Animation<TextureRegion>(duration, regions);
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
            Texture t = new Texture("images/npc/" + name + "/" + facingDirection.getAnimationRow() + "-0.png");
            return new TextureRegion(t);
        }
        Animation<TextureRegion> anim = animations.get(currentState)[facingDirection.getAnimationRow()];
        return anim.getKeyFrame(stateTime, true);
    }

    public int getCurrentFrameIndex() {
        if (currentState == PlayerState.IDLE) {
            return 0;
        } else {
            Animation<TextureRegion> anim = animations.get(currentState)[facingDirection.getAnimationRow()];
            return anim.getKeyFrameIndex(stateTime);
        }
    }

}
