package com.client.views.inGameMenus;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.client.GameMain;
import com.client.utils.MyScreen;

public class FishingMiniGame implements MyScreen, InputProcessor {
    private Stage stage;
    private Texture backgroundTexture;

    private Rectangle fishHitbox;
    private Image fishImage;
    private float fishPosition;
    private float fishVelocity;
    private float fishAcceleration;

    private Rectangle bobberHitbox;
    private Image bobberImage;
    private float bobberPosition;
    private float bobberVelocity;
    private float bobberAcceleration;

    private float catchingProgress;
    private ProgressBar catchingProgressBar;

    private final GameMain gameMain;
    private final FarmMenu farmMenu;

    public FishingMiniGame(GameMain gameMain, FarmMenu farmMenu) {
        this.gameMain = gameMain;
        this.farmMenu = farmMenu;

        initializeStage();
    }

    private void initializeStage() {
        //TODO
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
        backgroundTexture.dispose();
    }
}
