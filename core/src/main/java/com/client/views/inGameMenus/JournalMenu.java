package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Player;
import com.common.models.Quest;

import java.util.ArrayList;

public class JournalMenu implements MyScreen, InputProcessor {
    private final Skin skin;
    private GameMain gameMain;
    private MyScreen farmScreen;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private Player player;
    private ArrayList<Quest> quests;

    public JournalMenu(GameMain gameMain, MyScreen farmScreen) {
        this.gameMain = gameMain;
        this.farmScreen = farmScreen;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getImage("profileBackground");
        this.skin = AssetManager.getSkin();
        titleFont = AssetManager.getStardewFont();
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.WHITE);
        this.layout = new GlyphLayout();
        this.player = ClientApp.currentPlayer;
        this.quests = player.getQuests();

    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE || i == Input.Keys.F) {
            gameMain.setScreen(farmScreen);
            this.dispose();
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
    public boolean scrolled(float v, float v1) {
        return false;
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        float xInfo = 100f;
        float yInfo = Gdx.graphics.getHeight() - 150f;
        BitmapFont font = AssetManager.getStardewFont();
        font.getData().setScale(1);
        font.setColor(Color.WHITE);

        font.draw(batch, "Active Quests:", xInfo, yInfo);
        yInfo -= 40;

//        for (Friendship f : friendships) {
//            font.draw(batch, "Name: " + f.getPlayer() + " | Level: " + f.getLevel() + " | XP: " + f.getXp(), xInfo, yInfo);
//            yInfo -= 30;
//        }

        for (Quest quest : quests) {
            font.draw(batch, quest.toString(), xInfo, yInfo);
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
