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
    private final int ITEMS_PER_PAGE = 36;
    private int scrollIndex = 0;
    private final int ITEM_SIZE = 64;
    private final int PADDING = 10;
    private final int COLUMNS = 6;
    private MyScreen farmScreen;

    public InventoryMenu(GameMain gameMain, MyScreen farmScreen) {
        this.gameMain = gameMain;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getTextures().get("mainMenuBackground");
        this.farmScreen = farmScreen;
        //TODO get current player's backpack
        backpack = new Backpack();
        backpack.addSlot(new Slot(new Misc(MiscType.EGG), 1));
        backpack.addSlot(new Slot(new Food(FoodTypes.LEEK), 1));

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
        int maxScroll = Math.max(0, totalSlots - ITEMS_PER_PAGE);

        scrollIndex += (int) amountY * COLUMNS;
        scrollIndex = Math.max(0, Math.min(scrollIndex, maxScroll));

        return true;
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        int startX = (Gdx.graphics.getWidth() - (COLUMNS * (ITEM_SIZE + PADDING))) / 2;
        int startY = (Gdx.graphics.getHeight() - (COLUMNS * (ITEM_SIZE + PADDING))) / 2;

        List<Slot> slots = backpack.getSlots();
        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            int actualIndex = scrollIndex + i;
            if (actualIndex >= slots.size()) break;

            int col = i % COLUMNS;
            int row = i / COLUMNS;

            int x = startX + col * (ITEM_SIZE + PADDING);
            int y = startY + row * (ITEM_SIZE + PADDING);

            Texture itemTexture = slots.get(actualIndex).getItem().getTexture();
            batch.draw(itemTexture, x, y, ITEM_SIZE, ITEM_SIZE);
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
