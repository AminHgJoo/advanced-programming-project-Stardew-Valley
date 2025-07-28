package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.client.GameMain;
import com.client.controllers.PlayerController;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Player;

public class VillageMenu implements MyScreen {
    private FarmMenu farmMenu;
    private PlayerController playerController;
    private Stage stage;
    private GameMain gameMain;
    private SpriteBatch batch;
    private Texture backgroundTexture;

    public VillageMenu(FarmMenu farmMenu, PlayerController playerController, GameMain gameMain) {
        this.farmMenu = farmMenu;
        this.playerController = playerController;
        this.gameMain = gameMain;
        batch = new SpriteBatch();
        stage = new Stage();
        backgroundTexture = AssetManager.getImage("stardewvillageday");

    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0 , Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
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

    }
}
