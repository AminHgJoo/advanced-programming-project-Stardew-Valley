package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Backpack;
import com.common.models.Slot;
import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.types.itemTypes.MiscType;
import com.common.models.items.Food;
import com.common.models.items.Misc;
import org.apache.tools.ant.taskdefs.Sync;

import java.util.List;

public class InventoryMenu implements MyScreen, InputProcessor {
    private SpriteBatch batch;
    private final GameMain gameMain;
    private Backpack backpack;
    private Texture backgroundTexture;
    private Texture inventoryTexture;
    private final int ITEMS_PER_PAGE = 27;
    private int scrollIndex = 0;
    private MyScreen farmScreen;
    private int GRID_SIZE = 9;

    public InventoryMenu(GameMain gameMain, MyScreen farmScreen) {
        this.gameMain = gameMain;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getTextures().get("profileBackground");
        inventoryTexture = AssetManager.getTextures().get("inventory");
        this.farmScreen = farmScreen;
        //TODO get current player's backpack
        backpack = new Backpack();
        int i =0;
        for(FoodTypes foodType : FoodTypes.values()) {
            if(i == 43)
                break;
            if(!foodType.equals(FoodTypes.FARMERS_LUNCH) && !foodType.equals(FoodTypes.DISH_OF_THE_SEA) && !foodType.equals(FoodTypes.MINERS_TREAT))
            backpack.addSlot(new Slot(new Food(Quality.DEFAULT,foodType, 10, 10), 1));
            i++;
        }

    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE){
            dispose();
            gameMain.setScreen(farmScreen);

        }
        return false;
    }



    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        int totalSlots = backpack.getSlots().size();

        int maxRows = Math.max(0, (totalSlots - ITEMS_PER_PAGE + GRID_SIZE - 1) / GRID_SIZE);
        scrollIndex += (int) amountY;
        scrollIndex = Math.max(0, Math.min(scrollIndex, maxRows));
        return true;
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(inventoryTexture,
            Gdx.graphics.getWidth()/2 - inventoryTexture.getWidth()/2,
            Gdx.graphics.getHeight()/2 - inventoryTexture.getHeight()/2,
            inventoryTexture.getWidth(), inventoryTexture.getHeight());

        int GRID_ITEM_SIZE = 95;
        int GRID_PADDING = 8;

        int startX = (Gdx.graphics.getWidth()/2 - inventoryTexture.getWidth()/2)
            + GRID_PADDING + 45;
        int startY = Gdx.graphics.getHeight()/2 + inventoryTexture.getHeight()/2
            + GRID_PADDING - 160;

        List<Slot> slots = backpack.getSlots();
        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            int actualIndex = scrollIndex * GRID_SIZE + i;
            if (actualIndex >= slots.size()) break;

            int col = i % GRID_SIZE;
            int row = i / GRID_SIZE;

            int x = startX + col * (GRID_ITEM_SIZE + GRID_PADDING);
            int y = startY - row * (GRID_ITEM_SIZE + GRID_PADDING);

            Texture itemTexture = slots.get(actualIndex).getItem().getTexture();
            batch.draw(itemTexture, x, y, GRID_ITEM_SIZE, GRID_ITEM_SIZE);
        }
        batch.end();
    }
    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
