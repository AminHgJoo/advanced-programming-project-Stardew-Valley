package com.client.controllers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.client.ClientApp;
import com.client.utils.FacingDirection;
import com.client.utils.NpcAnimationController;
import com.client.utils.PlayerState;
import com.client.views.inGameMenus.VillageMenu;
import com.common.models.GameData;
import com.common.models.NPCModels.NPC;

public class NpcController {
    private Vector2 npcPosition;
    private FacingDirection facingDirection;
    private PlayerState currState;
    private NPC npc;
    private GameData game = ClientApp.currentGameData;
    private VillageMenu villageMenu;
    private NpcAnimationController npcAnimationController;

    public NpcController(VillageMenu villageMenu, NPC npc) {
        this.npc = npc;
        this.villageMenu = villageMenu;
        npcPosition = new Vector2(npc.getCoordinate().getX(), npc.getCoordinate().getY());
        facingDirection = FacingDirection.DOWN;
        currState = PlayerState.IDLE;
        this.npcAnimationController = new NpcAnimationController(currState, facingDirection, npc.getName());
    }

    public void render(Batch batch) {
        TextureRegion npcTexture = npcAnimationController.getCurrentFrame();
        float scale = 3f;
        batch.draw(npcTexture, npcPosition.x - (float) npcTexture.getTexture().getWidth() / (2 * scale),
            npcPosition.y - (float) npcTexture.getTexture().getHeight() / (2 * scale), npcTexture.getRegionWidth() / scale
            , npcTexture.getRegionHeight() / scale);

    }

    public void setState(PlayerState state) {
        currState = state;
        npcAnimationController.changeState(state);
    }

    public boolean checkToBeInSideWalk() {
        float x = npcPosition.x;
        float y = npcPosition.y;
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

    public void updateNpc(){
        this.npc = game.findNpcByName(npc.getName());

    }

    public void updateGame(){
        this.game = ClientApp.currentGameData;
        updateNpc();
    }

}
