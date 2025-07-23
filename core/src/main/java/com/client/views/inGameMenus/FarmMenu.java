package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.controllers.PlayerController;
import com.client.utils.*;
import com.common.models.Backpack;
import com.common.models.GameData;
import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.Slot;
import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.worldEnums.Weather;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.ArtisanBlock;
import com.common.models.mapObjects.BuildingBlock;
import com.common.models.mapObjects.DroppedItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.server.controllers_old.gameMenuControllers.ArtisanController;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.List;

public class FarmMenu implements MyScreen, InputProcessor {
    //TODO: Draw destroyed/renovated greenhouse based on it being fixed or not.
    public static final float SCREEN_WIDTH = 450 * 1.5f;
    public static final float SCREEN_HEIGHT = 300 * 1.5f;
    public static final float BASE_SPEED_FACTOR = 16;
    public static final float TILE_PIX_SIZE = 32;
    public static final float FARM_X_SPAN = 75; //32 * 75 == 2400
    public static final float FARM_Y_SPAN = 50; //32 * 50 == 1600

    private final GameMain gameMain;
    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
            @Override
            public void write(JsonWriter out, LocalDateTime value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public LocalDateTime read(JsonReader in) throws IOException {
                return LocalDateTime.parse(in.nextString());
            }
        })
        .serializeSpecialFloatingPointValues()
        .create();
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    //TODO: TEST AND PROTOTYPE
    private final Texture playerTexture = new Texture("images/T_BatgunProjectile.png");
    //These are graphical coordinates, not the x and y coordinates in the game logic.
    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;
    private final PlayerController playerController;
    private SpriteBatch batch;
    private Farm farm;
    private Texture grassTexture;
    private boolean isToolSwinging = false;
    private Texture inventory;
    private int scrollIndex = 0;
    private MyScreen farmScreen;
    private int GRID_SIZE = 9;
    private int GRID_PADDING = 8;
    private int selectedIndex = -1;
    private int GRID_ITEM_SIZE = 95;
    private boolean selected = false;
    private int selectedSave = -1;
    private LightningHelper lightningHelper = new LightningHelper();
    private ShaderProgram dayNightShader;
    private float nightFactor;
    private ShapeRenderer shapeRenderer;
    private ParticleEffect rainEffect;
    private ParticleEffect snowEffect;

    private Stage stage;
    private Image clockArrow;
    private Image clockBase;
    private Image weather;
    private Label dateLabel;
    private Label timeLabel;
    private Label moneyLabel;
    private Stage popupStage;

    public FarmMenu(GameMain gameMain) {
        this.gameMain = gameMain;
        this.playerPosition = new Vector2(TILE_PIX_SIZE * FARM_X_SPAN / 2, TILE_PIX_SIZE * FARM_Y_SPAN / 2);
        this.playerVelocity = new Vector2();

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);

        this.viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        popupStage = new Stage(new ScreenViewport());
        this.grassTexture = AssetManager.getImage("grassfall");
        playerController = new PlayerController(playerPosition, playerVelocity);
        this.farm = playerController.getPlayer().getFarm();
        this.inventory = AssetManager.getImage("aks");
        //TODO tuf zadam
        for (Slot slot : ClientApp.currentPlayer.getInventory().getSlots()) {
            slot.getItem().setTexture(slot.getItem().getTexture());

        }
        initializeShader();
        initializeParticles();
        initializeStage();
        ClientApp.currentPlayer.getInventory().addSlot(new Slot(FoodTypes.HONEY, 10));
        shapeRenderer = new ShapeRenderer();
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    //TODO: Update clock UI when needed. Dispose stage and call this function.
    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        clockBase = new Image(AssetManager.getImage("clockbase"));
        float scaleFactor = 5;
        clockBase.scaleBy(scaleFactor - 1);
        stage.addActor(clockBase);

        clockBase.setPosition(stage.getWidth() - clockBase.getWidth() * scaleFactor, stage.getHeight() - clockBase.getHeight() * scaleFactor);

        clockArrow = new Image(AssetManager.getImage("clockarrow"));
        clockArrow.scaleBy(scaleFactor - 1);
        stage.addActor(clockArrow);

        clockArrow.setOrigin(3.5f, 1f);
        clockArrow.setPosition(stage.getWidth() - 250, stage.getHeight() - 100);

        BitmapFont font = AssetManager.getStardewFont();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.BLACK;

        dateLabel = new Label("Mon. 1", labelStyle);
        stage.addActor(dateLabel);
        dateLabel.setPosition(stage.getWidth() - 43 * scaleFactor, stage.getHeight() - 13 * scaleFactor);

        timeLabel = new Label("6:50 am", labelStyle);
        stage.addActor(timeLabel);
        timeLabel.setPosition(stage.getWidth() - 43 * scaleFactor, stage.getHeight() - 36 * scaleFactor);

        Image inventoryImage = new Image(inventory);
        stage.addActor(inventoryImage);
        inventoryImage.setPosition(stage.getWidth() / 2 - inventoryImage.getWidth() / 2, 0);

        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = font;
        labelStyle2.fontColor = Color.RED;
        moneyLabel = new Label("5000", labelStyle2);
        stage.addActor(moneyLabel);
        moneyLabel.setPosition(stage.getWidth() - 55 * scaleFactor, stage.getHeight() - 55 * scaleFactor);

        if (ClientApp.currentGameData == null) {
            return;
        }

        GameData gameData = ClientApp.currentGameData;
        LocalDateTime currentDateTime = gameData.getDate();

        final int morningHour = 6;
        final int nightHour = 22;
        final int currentHour = currentDateTime.getHour();
        float clockRotation = (float) (currentHour - morningHour) / (nightHour - morningHour) * 180f;

        clockArrow.setRotation(clockRotation);

        weather = new Image(AssetManager.giveWeatherIcon(gameData.getWeatherToday()));
        weather.scaleBy(scaleFactor - 1);
        stage.addActor(weather);

        weather.setPosition(stage.getWidth() - 43 * scaleFactor, stage.getHeight() - scaleFactor * 24);

        Image season = new Image(AssetManager.giveSeasonIcon(gameData.getSeason()));
        season.scaleBy(scaleFactor - 1);
        stage.addActor(season);

        season.setPosition(stage.getWidth() - scaleFactor * 19, stage.getHeight() - scaleFactor * 24);

        dateLabel.setText(DayOfWeek.values()[(currentDateTime.getDayOfMonth() - 1) % 7].toString().toLowerCase()
            + ". " + currentDateTime.getDayOfWeek());

        timeLabel.setText((currentHour > 12 ? (currentHour - 12) : currentHour) + ":" + currentDateTime.getMinute() + " " + (currentHour > 12 ? "PM" : "AM"));

        moneyLabel.setText(gameData.getCurrentPlayer().getMoney(gameData));
        showTools();
    }

    private void showTools() {
        int GRID_ITEM_SIZE = 95;
        int GRID_PADDING = 8;
        Backpack backpack = ClientApp.currentPlayer.getInventory();
        int startX = (int) (stage.getWidth() / 2 - inventory.getWidth() / 2 + 50);
        int startY = 10;
        if (selectedIndex >= 0 && selected == false) {
            selected = true;
            selectedSave = selectedIndex;
            ClientApp.currentPlayer.setEquippedItem(backpack.getSlots().get(selectedSave).getItem());
            selectedIndex = -1;
        }

        for (int j = 0; j < 9; j++) {
            int actualIndex = scrollIndex * GRID_SIZE + j;
            if (selectedIndex >= 0 && selected) {
                Slot temp = backpack.getSlots().get(selectedIndex);
                backpack.getSlots().set(selectedIndex, backpack.getSlots().get(selectedSave));
                backpack.getSlots().set(selectedSave, temp);
                selectedIndex = -1;
                selected = false;
                selectedSave = -1;
            }
        }
        List<Slot> slots = backpack.getSlots();
        for (int i = 0; i < 9; i++) {
            int actualIndex = scrollIndex * GRID_SIZE + i;
            if (actualIndex >= slots.size()) break;

            int col = i % GRID_SIZE;
            int row = i / GRID_SIZE;

            int x = startX + col * (GRID_ITEM_SIZE + GRID_PADDING);
            int y = startY - row * (GRID_ITEM_SIZE + GRID_PADDING);


            if (actualIndex == selectedSave) {
                Image red = new Image(AssetManager.getImage("red"));
                stage.addActor(red);
                red.setPosition(x, y);
                red.setSize(GRID_ITEM_SIZE, GRID_ITEM_SIZE);
            }


            Texture itemTexture = slots.get(actualIndex).getItem().getTexture();
            Image itemImage = new Image(itemTexture);
            stage.addActor(itemImage);
            itemImage.setPosition(x, y);
            itemImage.setSize(GRID_ITEM_SIZE, GRID_ITEM_SIZE);

        }
    }

    private void initializeParticles() {
        rainEffect = new ParticleEffect();
        rainEffect.load(
            Gdx.files.internal("particles/rain.p"),
            Gdx.files.internal("particles")
        );
        rainEffect.getEmitters().first().setContinuous(true);
        rainEffect.start();

        snowEffect = new ParticleEffect();
        snowEffect.load(
            Gdx.files.internal("particles/snow.p"),
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
        if (Keybinds.OPEN_INVENTORY.keycodes.contains(keycode)) {
            gameMain.setScreen(new InventoryMenu(gameMain, this));
        } else if (Keybinds.OPEN_CRAFTING.keycodes.contains(keycode)) {
            gameMain.setScreen(new CraftingMenu(gameMain, this));
        } else if (Keybinds.OPEN_COOKING.keycodes.contains(keycode)) {
            gameMain.setScreen(new CookingMenu(gameMain, this));
        } else if (Keybinds.OPEN_RADIO.keycodes.contains(keycode)) {
            gameMain.setScreen(new RadioStardrop(gameMain, this));
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            System.out.println(playerController.getPlayer().getEquippedItem());
            if (playerController.getPlayer().getEquippedItem() == null) {
                return;
            }
            playerController.setState(PlayerState.TOOL_SWINGING);
            isToolSwinging = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isToolSwinging = false;
                    this.cancel();
                }
            }, .6f, 2);
            playerController.toolUse();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            boolean success = playerController.dropItem(ClientApp.currentPlayer, farm);
            if (success) {
                selectedIndex = -1;
                selectedSave = -1;
                selected = false;
            }
        } else {
            if (!isToolSwinging) {
                playerController.setState(PlayerState.IDLE);
                playerVelocity.x = 0;
                playerVelocity.y = 0;
            }
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int startX = (int) (stage.getWidth() / 2 - inventory.getWidth() / 2 + 50);
        int startY = 10;
        float prev_screen_y = screenY;
        screenY = Gdx.graphics.getHeight() - screenY;

        for (int i = 0; i < 9; i++) {
            Backpack backpack = ClientApp.currentPlayer.getInventory();
            int actualIndex = scrollIndex * GRID_SIZE + i;
            if (actualIndex >= backpack.getSlots().size()) break;

            int col = i % GRID_SIZE;
            int row = i / GRID_SIZE;
            int x = startX + col * (GRID_ITEM_SIZE + GRID_PADDING);
            int y = startY - row * (GRID_ITEM_SIZE + GRID_PADDING);

            if (screenX >= x && screenX <= x + GRID_ITEM_SIZE &&
                screenY >= y && screenY <= y + GRID_ITEM_SIZE) {

                if (selectedIndex >= 0 && selectedIndex == actualIndex) {
                    selectedIndex = -1;
                    ClientApp.currentPlayer.setEquippedItem(null);
                } else {
                    selectedIndex = actualIndex;
                    ClientApp.currentPlayer.setEquippedItem(null);
                }
                break;
            }

        }
        camera.update();
        Vector3 worldCoords = new Vector3(screenX, prev_screen_y, 0);
        viewport.unproject(worldCoords);
        float cellX = (worldCoords.x / 32);

        float cellY = (50 - (worldCoords.y / 32));

        Cell clickedCell = farm.findCellByCoordinate(cellX, cellY);
        if (clickedCell != null) {
            if (clickedCell.getObjectOnCell() instanceof ArtisanBlock) {
                ArtisanBlock artisanBlock = (ArtisanBlock) clickedCell.getObjectOnCell();
                if (!artisanBlock.beingUsed) {
                    gameMain.setScreen(new ArtisanMenu(gameMain, this, artisanBlock.getArtisanType().name));
                } else {
                    Gdx.input.setInputProcessor(popupStage);
                    Vector2 stageCoords = popupStage.screenToStageCoordinates(new Vector2(screenX, screenY));
                    Skin skin = AssetManager.getSkin();

                    Window popup = new Window(artisanBlock.getArtisanType().name, skin);
                    popup.setSize(300, 500);
                    popup.setPosition(stageCoords.x, stageCoords.y, Align.topLeft);

                    //TODO information
                    Label label = new Label("information", skin);
                    label.setWrap(true);
                    label.setAlignment(Align.center);

                    TextButton cheatBtn = new TextButton("Cheat", skin);
                    TextButton collectBtn = new TextButton("Collect", skin);
                    TextButton cancelBtn = new TextButton("Cancel", skin);
                    TextButton exitBtn = new TextButton("Exit", skin);

                    cheatBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (!artisanBlock.canBeCollected)
                                artisanBlock.canBeCollected = true;
                        }
                    });
                    collectBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (artisanBlock.canBeCollected) {
                                //TODO server
                                Request request = new Request("Collect");
                                request.body.put("artisanName", artisanBlock.getArtisanType().name);
                                Response response = ArtisanController.handleArtisanGet(request);
                                if (response.isSuccess()) {
                                    Gdx.input.setInputProcessor(FarmMenu.this);
                                    popup.remove();
                                }
                            }
                        }
                    });
                    cancelBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            //TODO server
                            artisanBlock.beingUsed = false;
                            artisanBlock.canBeCollected = false;
                            artisanBlock.productSlot = null;
                            Gdx.input.setInputProcessor(FarmMenu.this);
                            popup.remove();
                        }
                    });
                    exitBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            Gdx.input.setInputProcessor(FarmMenu.this);
                            popup.remove();
                        }
                    });

                    popup.pad(10);
                    label.setWrap(false);
                    popup.add(label).row();
                    popup.add(cheatBtn).row();
                    popup.add(collectBtn).row();
                    popup.add(cancelBtn);
                    popup.add(exitBtn);

                    popupStage.addActor(popup);
                }
            }
        }

        return true;
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
        HashMap<String, String> res = (HashMap<String, String>) gson.fromJson(message, HashMap.class);
        String type = res.get("type");
        if (type.equals("PLAYER_MOVED")) {
            playerController.handleServerPlayerMove(res);
        } else if (type.equals("PLAYER_UPDATED")) {
            String id = res.get("player_user_id");
            if (id != playerController.getPlayer().getUser_id()) {
                String player = res.get("player");
                playerController.updateAnotherPlayerObject(player);
            }
        } else if (type.equals("GAME_UPDATED")) {
            String game = res.get("game");
            playerController.updateGame(game);
        }
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
        this.farm = playerController.getPlayer().getFarm();
        batch.setShader(dayNightShader);
        clearAndResetScreen();
        updateSeasonGrassTexture();

        updateTime(delta);
        playerController.updatePlayerPos(delta);
        playerController.update(delta);
        handleEvents();

        renderMap(dayNightShader, nightFactor, delta);

        handleLightning(camera, delta);
        batch.setShader(null);

        handleWeatherFX(delta);

        handleUI(delta);
    }

    private void handleUI(float delta) {
        stage.act(delta);
        stage.draw();
    }

    private void handleWeatherFX(float delta) {
        float x = camera.position.x - SCREEN_WIDTH / 2;
        float y = camera.position.y + SCREEN_HEIGHT / 2;

        if (ClientApp.currentGameData == null) return;

        if (ClientApp.currentGameData.getWeatherToday() == Weather.STORM
            || ClientApp.currentGameData.getWeatherToday() == Weather.RAIN) {
            rainEffect.setPosition(x, y);
            rainEffect.update(delta);
            batch.begin();
            rainEffect.draw(batch);
            batch.end();
        }

        if (ClientApp.currentGameData.getWeatherToday() == Weather.SNOW) {
            snowEffect.setPosition(x, y);
            snowEffect.update(delta);
            batch.begin();
            snowEffect.draw(batch);
            batch.end();
        }
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

    public void renderMap(ShaderProgram dayNightShader, float nightFactor, float delta) {
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

            float xOfCell = coordinate.getX();
            float yOfCell = coordinate.getY();
            Texture texture1 = grassTexture;

            if (!(cell.getObjectOnCell() instanceof BuildingBlock buildingBlock)) {
                texture1 = cell.getObjectOnCell().getTexture();
            } else if (buildingBlock.buildingType.equals("Mine")) {
                texture1 = AssetManager.getImage("mineCell");
            }


            if (texture1 == SeasonTextures.SPRING.texture ||
                texture1 == SeasonTextures.SUMMER.texture ||
                texture1 == SeasonTextures.FALL.texture ||
                texture1 == SeasonTextures.WINTER.texture) {
                continue;
            }

            if (cell.getObjectOnCell() instanceof DroppedItem || cell.getObjectOnCell() instanceof ArtisanBlock)
                modifiedDraw(batch, texture1, xOfCell, yOfCell, 30, 30);
            else
                modifiedDraw(batch, texture1, xOfCell, yOfCell);
        }

        playerController.render(batch);
        //Draw buildings
        Texture house = AssetManager.getImage("playerhouse");
        Texture greenhouseDestroyed = AssetManager.getImage("greenhousedestroyed");

        modifiedDraw(batch, house, 61, 8);
        modifiedDraw(batch, greenhouseDestroyed, 22, 6);
        popupStage.act(delta);
        popupStage.draw();

        artisanTimeBar();

        stage.dispose();
        initializeStage();
        batch.end();
    }

    //TODO server
    private void artisanTimeBar() {
        for (Cell cell : farm.getCells()) {
            if (cell.getObjectOnCell() instanceof ArtisanBlock) {
                ArtisanBlock artisanBlock = (ArtisanBlock) cell.getObjectOnCell();
                if (artisanBlock.beingUsed) {
                    float artisanHeight = 30.0f;
                    float artisanWidth = 30.0f;
                    //TODO gameData
                    GameData gameData = ClientApp.currentPlayer.getUser().getCurrentGame();
                    LocalDateTime start = artisanBlock.startTime;
                    LocalDateTime end = artisanBlock.prepTime;
                    LocalDateTime now = ClientApp.currentPlayer
                        .getUser()
                        .getCurrentGame()
                        .getDate();
                    float totalSec = Duration.between(start, end).toMillis();
                    float elapsed = Duration.between(start, now).toMillis();
                    float progress = MathUtils.clamp(elapsed / totalSec, 0f, 1f);
                    float barWidth = artisanWidth;
                    float barHeight = 5f;
                    float barX = cell.getCoordinate().getX();
                    float barY = cell.getCoordinate().getY() + artisanHeight + 2;
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.DARK_GRAY);
                    shapeRenderer.rect(barX, barY, barWidth, barHeight);
                    shapeRenderer.setColor(Color.LIME);
                    shapeRenderer.rect(barX, barY, barWidth * progress, barHeight);
                    shapeRenderer.end();
                }
            }
        }
    }

    public void modifiedDraw(SpriteBatch batch, Texture texture, float x, float y) {
        batch.draw(texture, x * TILE_PIX_SIZE, (convertYCoordinate(y) - 1) * TILE_PIX_SIZE);
    }

    public void modifiedDraw(SpriteBatch batch, Texture texture, float x, float y, float width, float height) {
        batch.draw(texture, x * TILE_PIX_SIZE, (convertYCoordinate(y) - 1) * TILE_PIX_SIZE, width, height);
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
        rainEffect.dispose();
        snowEffect.dispose();
        dayNightShader.dispose();
        lightningHelper.dispose();
        stage.dispose();
        shapeRenderer.dispose();
        popupStage.dispose();
    }

    public float convertYCoordinate(float yCoordinate) {
        return 50 - yCoordinate;
    }
}
