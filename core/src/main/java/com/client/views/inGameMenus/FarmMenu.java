package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.controllers.PlayerController;
import com.client.utils.*;
import com.common.models.enums.worldEnums.Weather;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.BuildingBlock;

import java.time.LocalDateTime;

public class FarmMenu implements MyScreen, InputProcessor {
    //TODO: Draw destroyed/renovated greenhouse based on it being fixed or not.
    public static final float SCREEN_WIDTH = 450 * 1.5f;
    public static final float SCREEN_HEIGHT = 300 * 1.5f;
    public static final float BASE_SPEED_FACTOR = 20;
    public static final float TILE_PIX_SIZE = 32;
    public static final float FARM_X_SPAN = 75; //32 * 75 == 2400
    public static final float FARM_Y_SPAN = 50; //32 * 50 == 1600

    private final GameMain gameMain;

    private SpriteBatch batch;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    private Farm farm;
    private Texture grassTexture;

    //TODO: TEST AND PROTOTYPE
    private final Texture playerTexture = new Texture("images/T_BatgunProjectile.png");

    //These are graphical coordinates, not the x and y coordinates in the game logic.
    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;

    private final PlayerController playerController;

    private LightningHelper lightningHelper = new LightningHelper();
    private ShaderProgram dayNightShader;
    private float nightFactor;

    private ParticleEffect rainEffect;
    private ParticleEffect snowEffect;


    public FarmMenu(GameMain gameMain) {
        this.gameMain = gameMain;

        this.playerPosition = new Vector2(TILE_PIX_SIZE * FARM_X_SPAN / 2, TILE_PIX_SIZE * FARM_Y_SPAN / 2);
        this.playerVelocity = new Vector2();

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        this.farm = Farm.makeFarm(1);
        this.grassTexture = AssetManager.getImage("grassfall");
        playerController = new PlayerController(playerPosition, playerVelocity);

        initializeShader();
        initializeParticles();
    }

    private void initializeParticles() {
        rainEffect = new ParticleEffect();
        rainEffect.load(
            Gdx.files.internal("particles/rain.p"),
            Gdx.files.internal("particles")
        );
        rainEffect.getEmitters().first().setContinuous(true);
        rainEffect.start();

        // Snow
        snowEffect = new ParticleEffect();
        snowEffect.load(
            Gdx.files.internal("particles/rain.p"),
            Gdx.files.internal("particles")
        );
        snowEffect.getEmitters().first().setContinuous(true);
        snowEffect.start();
    }

    private void initializeShader() {
        ShaderProgram.pedantic = false;
        dayNightShader = new ShaderProgram(
            Gdx.files.internal("shaders/daynight.vert"),
            Gdx.files.internal("shaders/daynight.frag")
        );
        if (!dayNightShader.isCompiled()) {
            throw new GdxRuntimeException("Shader compile error:\n"
                + dayNightShader.getLog());
        }
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
        } else if (Keybinds.OPEN_INVENTORY.keycodes.contains(keycode)) {
            gameMain.setScreen(new InventoryMenu(gameMain, this));
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
//        if (Keybinds.DOWN.keycodes.contains(keycode)) {
//            playerVelocity.y = 0;
//        } else if (Keybinds.UP.keycodes.contains(keycode)) {
//            playerVelocity.y = 0;
//        } else if (Keybinds.LEFT.keycodes.contains(keycode)) {
//            playerVelocity.x = 0;
//        } else if (Keybinds.RIGHT.keycodes.contains(keycode)) {
//            playerVelocity.x = 0;
//        }
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
        //TODO: ?
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    private void updateSeasonGrassTexture() {
        //TODO: NullPtr Failsafe.
        if (ClientApp.currentGameData == null) return;

        grassTexture = SeasonTextures.giveSeasonTexture(ClientApp.currentGameData.getSeason());
    }

    private void handleLightning(OrthographicCamera camera, float delta) {
        //TODO: Failsafe to not throw exception.
        if (ClientApp.currentGameData == null) {
            return;
        }

        if (ClientApp.currentGameData.getWeatherToday() == Weather.STORM) {
            float rand = MathUtils.random(0f, 1f);

            if (rand < 0.0001f && !lightningHelper.flashing) {
                lightningHelper.trigger();
            }
        } else {
            return;
        }

        lightningHelper.render(camera, delta);
    }

    //TODO: Advance time on server-side.
    private void updateTime(float delta) {
        //TODO: NullPtr Failsafe.
        if (ClientApp.currentGameData == null) {
            return;
        }
        LocalDateTime time = ClientApp.currentGameData.getDate();
        float timeOfDay = (time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond()) / 3600f;

        if (timeOfDay >= 18f) {
            nightFactor = (timeOfDay - 18f) / 12f;
        } else if (timeOfDay <= 6f) {
            nightFactor = (timeOfDay + 6f) / 12f;
        } else {
            nightFactor = 0f;
        }
        nightFactor = MathUtils.clamp(nightFactor, 0f, 0.8f);
    }

    @Override
    public void render(float delta) {
        batch.setShader(dayNightShader);
        clearAndResetScreen();
        updateSeasonGrassTexture();

        updateTime(delta);
        updatePlayerPos(delta);
        playerController.update(delta);
        playerController.handlePlayerMove();

        renderMap(dayNightShader, nightFactor);

        handleLightning(camera, delta);
        batch.setShader(null);

        handleWeatherFX(delta);
    }

    private void handleWeatherFX(float delta) {
        float halfW = camera.viewportWidth  * 0.5f * camera.zoom;
        float halfH = camera.viewportHeight * 0.5f * camera.zoom;
        float left   = camera.position.x - halfW;
        float top    = camera.position.y + halfH;

        rainEffect.setPosition(left + halfW, top);
        rainEffect.update(delta);
        batch.begin();
        rainEffect.draw(batch);
        batch.end();
//        snowEffect.setPosition(left + halfW, top);
//        snowEffect.update(delta);
//        snowEffect.draw(batch);
    }

    private void updatePlayerPos(float delta) {
        playerPosition.x += playerVelocity.x * delta * BASE_SPEED_FACTOR;
        playerPosition.x = MathUtils.clamp(playerPosition.x, (float) playerTexture.getWidth() / 2, FARM_X_SPAN * TILE_PIX_SIZE - (float) playerTexture.getWidth() / 2);

        playerPosition.y += playerVelocity.y * delta * BASE_SPEED_FACTOR;
        playerPosition.y = MathUtils.clamp(playerPosition.y, (float) playerTexture.getHeight() / 2, FARM_Y_SPAN * TILE_PIX_SIZE - (float) playerTexture.getHeight() / 2);
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
        camera.position.y = MathUtils.clamp(playerPosition.y, SCREEN_HEIGHT / 2, TILE_PIX_SIZE * FARM_Y_SPAN - SCREEN_HEIGHT / 2);

        camera.update();
    }

    public void renderMap(ShaderProgram dayNightShader, float nightFactor) {
        batch.begin();
        dayNightShader.setUniformf("u_nightFactor", nightFactor);
        Texture texture = grassTexture;
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 75; j++) {
                modifiedDraw(batch, texture, j, i);
            }
        }
        for (Cell cell : farm.getCells()) {
            Coordinate coordinate = cell.getCoordinate();

            int xOfCell = coordinate.getX();
            int yOfCell = coordinate.getY();
            Texture texture1 = grassTexture;

            if (!(cell.getObjectOnCell() instanceof BuildingBlock buildingBlock)) {
                texture1 = cell.getObjectOnCell().texture;
            } else if (buildingBlock.buildingType.equals("Mine")) {
                texture1 = AssetManager.getImage("mineCell");
            }

            if (texture1 == SeasonTextures.SPRING.texture ||
                texture1 == SeasonTextures.SUMMER.texture ||
                texture1 == SeasonTextures.FALL.texture ||
                texture1 == SeasonTextures.WINTER.texture) {
                continue;
            }

            modifiedDraw(batch, texture1, xOfCell, yOfCell);
        }

        //Draw buildings
        Texture house = AssetManager.getImage("playerhouse");
        Texture greenhouseDestroyed = AssetManager.getImage("greenhousedestroyed");

        modifiedDraw(batch, house, 61, 8);
        modifiedDraw(batch, greenhouseDestroyed, 22, 6);

//        batch.draw(playerTexture, playerPosition.x - (float) playerTexture.getWidth() / 2, playerPosition.y - (float) playerTexture.getHeight() / 2);
        playerController.render(batch);
        batch.end();
    }

    public void modifiedDraw(SpriteBatch batch, Texture texture, int x, int y) {
        batch.draw(texture, x * TILE_PIX_SIZE, (convertYCoordinate(y) - 1) * TILE_PIX_SIZE);
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
        rainEffect.dispose();
        snowEffect.dispose();
        dayNightShader.dispose();
        lightningHelper.dispose();
    }

    public int convertYCoordinate(int yCoordinate) {
        return 50 - yCoordinate;
    }
}
