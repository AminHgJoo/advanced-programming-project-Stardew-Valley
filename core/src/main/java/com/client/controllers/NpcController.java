
package com.client.controllers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.client.ClientApp;
import com.client.utils.FacingDirection;
import com.client.utils.HTTPUtil;
import com.client.utils.NpcAnimationController;
import com.client.utils.PlayerState;
import com.client.views.inGameMenus.VillageMenu;
import com.common.models.GameData;
import com.common.models.NPCModels.NPC;
import com.common.models.mapModels.Coordinate;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NpcController {
    private final ExecutorService networkThreadPool = Executors.newFixedThreadPool(2);
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

    public void render(Batch batch, float delta) {
        if (npc.getTargetPosition() != null) {
            currState = PlayerState.WALKING;
            walk(delta);
        } else {
            currState = PlayerState.IDLE;
        }
        npcAnimationController.update(delta, facingDirection, currState);

        TextureRegion npcTexture = npcAnimationController.getCurrentFrame();
        float scale = 3f;
        batch.draw(npcTexture, npcPosition.x - (float) npcTexture.getTexture().getWidth() / (2 * scale),
            npcPosition.y - (float) npcTexture.getTexture().getHeight() / (2 * scale), npcTexture.getRegionWidth() / scale
            , npcTexture.getRegionHeight() / scale);

    }

    public void walk(float delta) {
        Vector2 direction = new Vector2(npc.getTargetPosition().x - npcPosition.x, npc.getTargetPosition().y - npcPosition.y);
        if (!(direction.x > -5 && direction.x < 5)) {
            if (direction.x > 0) {
                facingDirection = FacingDirection.RIGHT;
            } else {
                facingDirection = FacingDirection.LEFT;
            }
            npcPosition.x += direction.x / 10f * delta;
        } else if (!(direction.y > -5 && direction.y < 5)) {
            if (direction.y > 0) {
                facingDirection = FacingDirection.UP;
            } else {
                facingDirection = FacingDirection.DOWN;
            }
            npcPosition.y += direction.y / 10f * delta;
        } else {
            currState = PlayerState.IDLE;
            npc.setCoordinate(new Coordinate(npc.getTargetPosition().x, npc.getTargetPosition().y));
            updatePositionNpc();
        }
    }

    public void updatePositionNpc() {
        networkThreadPool.execute(() -> {
            JsonObject req = new JsonObject();
            req.addProperty("x", npc.getCoordinate().getX());
            req.addProperty("y", npc.getCoordinate().getY());
            req.addProperty("name", npc.getName());

            var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/npcChangePosition", req);
            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
            System.out.println(res.getMessage());
        });
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

    public boolean isInPosition(int x, int y) {
        if (x >= npcPosition.x - 100 && x <= npcPosition.x + 100 && y >= npcPosition.y - 100 && y <= npcPosition.y + 100) {
            return true;
        }
        return false;
    }

    public NPC getNpc() {
        return npc;
    }

    public boolean isInRange(float x, float min, float max) {
        return x >= min && x <= max;
    }

    public void updateNpc() {
        this.npc = game.findNpcByName(npc.getName());
//        npcPosition.x = npc.getCoordinate().getX();
//        npcPosition.y = npc.getCoordinate().getY();
    }

    private FacingDirection getFacingDirection(float x, float y, float x2, float y2) {
        FacingDirection facingDirection = null;
        if (x < x2) {
            facingDirection = FacingDirection.RIGHT;
        } else if (x > x2) {
            facingDirection = FacingDirection.LEFT;
        } else if (y < y2) {
            facingDirection = FacingDirection.DOWN;
        } else if (y > y2) {
            facingDirection = FacingDirection.UP;
        }
        return facingDirection;
    }

    public void updateGame() {
        this.game = ClientApp.currentGameData;
        updateNpc();
    }

}
