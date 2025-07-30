package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.controllers.PlayerController;
import com.client.controllers.PlayerVillageController;
import com.client.utils.AssetManager;
import com.client.utils.Keybinds;
import com.client.utils.MyScreen;
import com.client.utils.PlayerState;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.mapModels.Coordinate;

import java.util.HashMap;

public class VillageMenu implements MyScreen {
    private FarmMenu farmMenu;
    private PlayerVillageController playerController;
    private Stage stage;
    private GameMain gameMain;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    public static final float SCREEN_WIDTH = 450 * 1.2f;
    public static final float SCREEN_HEIGHT = 300 * 1f;
    public static final float BASE_SPEED_FACTOR = 16;
    public static final float TILE_PIX_SIZE = 32;
    public static final float FARM_X_SPAN = 60; //32 * 75 == 2400
    public static final float FARM_Y_SPAN = 29.5f; //32 * 50 == 1600
    private final OrthographicCamera camera;
    private final StretchViewport viewport;

    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;
    private HashMap<String, PlayerVillageController> playerControllers = new HashMap<>();
    private GameData game = ClientApp.currentGameData;
    private Player player = ClientApp.currentPlayer;

    public VillageMenu(FarmMenu farmMenu, GameMain gameMain) {
        player.setCoordinate(new Coordinate(TILE_PIX_SIZE * FARM_X_SPAN / 2, TILE_PIX_SIZE * FARM_Y_SPAN / 2));
        this.farmMenu = farmMenu;
        this.gameMain = gameMain;
        batch = new SpriteBatch();
        stage = new Stage();
        this.playerPosition = new Vector2(player.getCoordinate().getX(), player.getCoordinate().getY());
        this.playerVelocity = new Vector2();
        for (Player p : game.getPlayers()) {
            Vector2 x = new Vector2(p.getCoordinate().getX(), p.getCoordinate().getY());
            Vector2 y = new Vector2();
            if (p.getUser_id().equals(ClientApp.currentPlayer.getUser_id())) {
                x = playerPosition;
                y = playerVelocity;
            }
            playerControllers.put(p.getUser_id(), new PlayerVillageController(x, y, farmMenu, p));
        }
        this.playerController = playerControllers.get(player.getUser_id());
        backgroundTexture = AssetManager.getImage("stardewvillageday");
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);
    }

    public void renderPlayers() {
        for (Player p : ClientApp.currentGameData.getPlayers()) {
            playerControllers.get(p.getUser_id()).render(batch);
        }
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {

    }

    public void handleEvents() {
        if (Gdx.input.isKeyPressed(Keybinds.UP.keycodes.get(0))) {
            playerController.setState(PlayerState.WALKING);
            playerController.handleKeyUp(0, BASE_SPEED_FACTOR);
        } else if (Gdx.input.isKeyPressed(Keybinds.DOWN.keycodes.get(0))) {
            playerController.setState(PlayerState.WALKING);
            playerController.handleKeyUp(0, -BASE_SPEED_FACTOR);
        } else if (Gdx.input.isKeyPressed(Keybinds.LEFT.keycodes.get(0))) {
            playerController.setState(PlayerState.WALKING);
            playerController.handleKeyUp(-BASE_SPEED_FACTOR, 0);
        } else if (Gdx.input.isKeyPressed(Keybinds.RIGHT.keycodes.get(0))) {
            playerController.setState(PlayerState.WALKING);
            playerController.handleKeyUp(BASE_SPEED_FACTOR, 0);
        }else if(Gdx.input.isKeyPressed(Input.Keys.P)){
            System.out.println(playerPosition.x + " , " + playerPosition.y);

        }else {
            playerController.setState(PlayerState.IDLE);
            playerVelocity.x = 0;
            playerVelocity.y = 0;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        playerController.updatePlayerPos(delta);
        playerController.update(delta);
        clearAndResetScreen();
        handleEvents();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderPlayers();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

    private void handleCamera() {
        camera.position.set(playerPosition.x, playerPosition.y, 0);

        camera.position.x = MathUtils.clamp(playerPosition.x, SCREEN_WIDTH / 2, TILE_PIX_SIZE * FARM_X_SPAN - SCREEN_WIDTH / 2);
        camera.position.y = MathUtils.clamp(playerPosition.y, SCREEN_HEIGHT / 2, TILE_PIX_SIZE * FARM_Y_SPAN - SCREEN_HEIGHT / 2);

        camera.update();
    }

    private void clearAndResetScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleCamera();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
    }

}
