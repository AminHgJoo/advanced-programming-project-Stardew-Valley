package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.client.GameMain;
import com.client.utils.Keybinds;
import com.client.utils.MyScreen;

public class FarmMenu implements MyScreen, InputProcessor {
    public static final float SCREEN_WIDTH = 450;
    public static final float SCREEN_HEIGHT = 300;
    public static final float BASE_SPEED_FACTOR = 20;

    public static final float TILE_PIX_SIZE = 32;
    public static final float FARM_X_SPAN = 75; //32 * 75 == 2400
    public static final float FARM_Y_SPAN = 50; //32 * 50 == 1600

    private final GameMain gameMain;

    private SpriteBatch batch;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;

    private final Texture defaultTile;
    //TODO: TEST AND PROTOTYPE
    private final Texture playerTexture = new Texture("images/T_BatgunProjectile.png");

    //These are graphical coordinates, not the x and y coordinates in the game logic.
    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;

    public FarmMenu(GameMain gameMain) {
        this.gameMain = gameMain;

        this.playerPosition = new Vector2(TILE_PIX_SIZE * FARM_X_SPAN / 2, TILE_PIX_SIZE * FARM_Y_SPAN / 2);
        this.playerVelocity = new Vector2();

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        this.defaultTile = new Texture("images/grass.png");
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Keybinds.DOWN.keycodes.contains(keycode)) {
            playerVelocity.y = -BASE_SPEED_FACTOR;
        } else if (Keybinds.UP.keycodes.contains(keycode)) {
            playerVelocity.y = BASE_SPEED_FACTOR;
        } else if (Keybinds.LEFT.keycodes.contains(keycode)) {
            playerVelocity.x = -BASE_SPEED_FACTOR;
        } else if (Keybinds.RIGHT.keycodes.contains(keycode)) {
            playerVelocity.x = BASE_SPEED_FACTOR;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (Keybinds.DOWN.keycodes.contains(keycode)) {
            playerVelocity.y = 0;
        } else if (Keybinds.UP.keycodes.contains(keycode)) {
            playerVelocity.y = 0;
        } else if (Keybinds.LEFT.keycodes.contains(keycode)) {
            playerVelocity.x = 0;
        } else if (Keybinds.RIGHT.keycodes.contains(keycode)) {
            playerVelocity.x = 0;
        }
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
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        clearAndResetScreen();

        updatePlayerPos(delta);

        renderGraphics(delta, batch, defaultTile);
    }

    private void updatePlayerPos(float delta) {
        playerPosition.x += playerVelocity.x * delta * BASE_SPEED_FACTOR;
        playerPosition.x = MathUtils.clamp(playerPosition.x, (float) playerTexture.getWidth() / 2, FARM_X_SPAN * TILE_PIX_SIZE - (float) playerTexture.getWidth() / 2);

        playerPosition.y += playerVelocity.y * delta * BASE_SPEED_FACTOR;
        playerPosition.y = MathUtils.clamp(playerPosition.y, (float) playerTexture.getHeight() /2, FARM_Y_SPAN * TILE_PIX_SIZE - (float) playerTexture.getHeight() / 2);
    }

    private void clearAndResetScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleCamera();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
    }

    private void handleCamera() {
        camera.position.set(playerPosition.x, playerPosition.y, 0);

        camera.position.x = MathUtils.clamp(playerPosition.x, SCREEN_WIDTH / 2, TILE_PIX_SIZE * FARM_X_SPAN - SCREEN_WIDTH / 2);
        camera.position.y = MathUtils.clamp(playerPosition.y, SCREEN_HEIGHT /2,  TILE_PIX_SIZE * FARM_Y_SPAN - SCREEN_HEIGHT/ 2);

        camera.update();
    }

    private void renderGraphics(float delta, SpriteBatch batch, Texture defaultTile) {
        //TODO:
        batch.begin();
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 75; j++) {
                batch.draw(defaultTile, j * TILE_PIX_SIZE, i * TILE_PIX_SIZE);
            }
        }
        batch.draw(playerTexture, playerPosition.x - (float) playerTexture.getWidth() / 2, playerPosition.y - (float) playerTexture.getHeight() / 2);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        defaultTile.dispose();
    }

    public int convertYCoordinate(int yCoordinate) {
        return 50 - yCoordinate;
    }
}
