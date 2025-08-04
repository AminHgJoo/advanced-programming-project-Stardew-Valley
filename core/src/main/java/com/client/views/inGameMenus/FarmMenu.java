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
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.worldEnums.Weather;
import com.common.models.items.buffs.ActiveBuff;
import com.common.models.mapModels.Cell;
import com.common.models.mapModels.Coordinate;
import com.common.models.mapModels.Farm;
import com.common.models.mapObjects.*;
import com.common.models.mapObjects.Tree;
import com.common.utils.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.server.utilities.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FarmMenu implements MyScreen, InputProcessor {

    public static final float SCREEN_WIDTH = 450 * 1.5f;
    public static final float SCREEN_HEIGHT = 300 * 1.5f;
    public static final float BASE_SPEED_FACTOR = 16;
    public static final float TILE_PIX_SIZE = 32;
    public static final float FARM_X_SPAN = 75; //32 * 75 == 2400
    public static final float FARM_Y_SPAN = 50; //32 * 50 == 1600
    public final Gson gson = new GsonBuilder()
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
    private final GameMain gameMain;
    private final PauseMenu pauseMenu;
    private final OrthographicCamera camera;
    private final StretchViewport viewport;
    //These are graphical coordinates, not the x and y coordinates in the game logic.
    private final Vector2 playerPosition;
    private final Vector2 playerVelocity;
    private final PlayerController playerController;
    private final Stage chatNotifStage;
    public boolean voteFlag = false;
    public Player votedPlayer;
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
    private ArrayList<Texture> emojiTextures;
    private Stage stage;
    private Image clockArrow;
    private Image clockBase;
    private Image weather;
    private Label dateLabel;
    private Label timeLabel;
    private Label moneyLabel;
    private Stage popupStage;
    private boolean crowFlag = false;
    private InputProcessor temp;

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
        playerController = new PlayerController(playerPosition, playerVelocity, this, ClientApp.currentPlayer);
        this.farm = playerController.getPlayer().getFarm();
        this.inventory = AssetManager.getImage("aks");
        //TODO tuf zadam
        for (Slot slot : ClientApp.currentPlayer.getInventory().getSlots()) {
            slot.getItem().setTexture(slot.getItem().getTexture());
        }
        initializeShader();
        initializeParticles();
        initializeEmoji();
        playerController.getPlayer().emojiCounter = -1;
        initializeStage(0.1f);
        pauseMenu = new PauseMenu(AssetManager.getSkin(), this, gameMain);
        shapeRenderer = new ShapeRenderer();

        this.chatNotifStage = new Stage(new ScreenViewport());
    }

    public GameMain getGameMain() {
        return gameMain;
    }

    private void initializeEmoji() {
        emojiTextures = new ArrayList<>();
        emojiTextures.add(AssetManager.getImage("happy"));
        emojiTextures.add(AssetManager.getImage("sad"));
        emojiTextures.add(AssetManager.getImage("anger"));
        emojiTextures.add(AssetManager.getImage("laugh"));
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void showGoToVillagePopUp() {
        System.out.println("FARM MENU");
        ConfirmAlert alert = new ConfirmAlert("question", "Do you want to go to the village ?", AssetManager.getSkin()) {
            @Override
            protected void result(Object object) {
                boolean result = (boolean) object;
                if (result) {
                    playerController.goToVillage();
                } else {
                }
                Gdx.input.setInputProcessor(stage);

                remove();
            }
        };
        alert.show(popupStage);
        Gdx.input.setInputProcessor(popupStage);
    }

    private void initializeStage(float delta) {
        stage = new Stage(new ScreenViewport());

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = AssetManager.getStardewFont();
        style.fontColor = Color.BLACK;

        Player player = ClientApp.currentPlayer;

        ProgressBar energyBar = new ProgressBar(0, (float) (player.getMaxEnergy()), 1, true, AssetManager.getPixthulhu());
        energyBar.setValue((float) player.getEnergy());
        energyBar.setSize(50, 200);
        stage.addActor(energyBar);
        energyBar.setPosition(stage.getWidth() - energyBar.getWidth(), 0);

        Label buffs = new Label("Active Buffs:", style);
        stage.addActor(buffs);
        buffs.setPosition(0, stage.getHeight() - buffs.getHeight());

        Table bufferTable = new Table();
        for (ActiveBuff buff : player.getActiveBuffs()) {
            bufferTable.add(new Label(buff.toString(), style)).row();
        }
        ScrollPane buffsScrollPane = new ScrollPane(bufferTable);
        buffsScrollPane.setFadeScrollBars(false);
        buffsScrollPane.setScrollingDisabled(false, false);
        buffsScrollPane.setPosition(0, buffs.getY() - buffsScrollPane.getHeight());
        stage.addActor(buffsScrollPane);

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

        clockArrow.setRotation(180 - clockRotation);

        weather = new Image(AssetManager.giveWeatherIcon(gameData.getWeatherToday()));
        weather.scaleBy(scaleFactor - 1);
        stage.addActor(weather);

        weather.setPosition(stage.getWidth() - 43 * scaleFactor, stage.getHeight() - scaleFactor * 24);

        Image season = new Image(AssetManager.giveSeasonIcon(gameData.getSeason()));
        season.scaleBy(scaleFactor - 1);
        stage.addActor(season);

        season.setPosition(stage.getWidth() - scaleFactor * 19, stage.getHeight() - scaleFactor * 24);

        dateLabel.setText(DayOfWeek.values()[(currentDateTime.getDayOfMonth() - 1) % 7].toString().toLowerCase().substring(0, 3)
            + ". " + currentDateTime.getDayOfMonth());

        timeLabel.setText((currentHour > 12 ? (currentHour - 12) : currentHour) + ":" + (currentDateTime.getMinute() < 10 ? "0" + currentDateTime.getMinute() : currentDateTime.getMinute()) + " " + (currentHour > 12 ? "PM" : "AM"));
        float emojiSize = 64f;
        float spacing = 20f;
        int count = 4;
        float totalWidth = count * emojiSize + (count - 1) * spacing;
        float startX = (stage.getWidth() - totalWidth) / 2f + 400;
        float y = stage.getHeight() - emojiSize - 20f;
        Skin skin = AssetManager.getSkin();
        TextButton friendsButton = new TextButton("Friends", skin);
        friendsButton.setPosition(startX, y);
        stage.addActor(friendsButton);
        moneyLabel.setText(gameData.getCurrentPlayer().getMoney(gameData));
        showTools();
        emojiShow(delta);

    }

    public void updateBackPack() {
        playerController.updateInventory(ClientApp.currentPlayer.getInventory());
    }

    private void emojiShow(float delta) {
        float emojiSize = 64f;
        float spacing = 20f;
        int count = emojiTextures.size();

        float totalWidth = count * emojiSize + (count - 1) * spacing;
        float startX = (stage.getWidth() - totalWidth) / 2f;
        float y = stage.getHeight() - emojiSize - 20f;

        for (int i = 0; i < count; i++) {
            Texture texture5 = emojiTextures.get(i);
            float x = startX + i * (emojiSize + spacing);
            Image image = new Image(texture5);
            image.setPosition(x, y);
            image.setSize(emojiSize, emojiSize);
            stage.addActor(image);
        }

        //      if (emojiCounter >= 0) {
//            Image image = new Image(currentEmoji);
//            image.setPosition(stage.getWidth() / 2, 20 + stage.getHeight() / 2);
//            image.setSize(emojiSize, emojiSize);
//            image.setSize(emojiSize, emojiSize);
//            stage.addActor(image);
//            emojiCounter += delta;
//            if (emojiCounter >= 2) {
//                emojiCounter = -1;
//            }
//      }
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
            playerController.equipItem(backpack.getSlots().get(selectedSave).getItem());

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
                playerController.updateInventory(backpack);

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
        } else if (Keybinds.SPAWN_CROW.keycodes.contains(keycode)) {
            crowFlag = true;
        } else if (Keybinds.OPEN_MINIMAP.keycodes.contains(keycode)) {
            gameMain.setScreen(new MapMenu(gameMain, this));
        } else if (Keybinds.OPEN_FRIDGE.keycodes.contains(keycode)) {
            gameMain.setScreen(new CookingMenu(gameMain, this));
        } else if (Keybinds.INSPECT_GREENHOUSE.keycodes.contains(keycode)) {
            showPopUp("This greenhouse can be repaired with 500 wood & 1000 gold.", "Message");
        } else if (Keybinds.OPEN_LEADERBOARDS.keycodes.contains(keycode)) {
            gameMain.setScreen(new Leaderboards(gameMain, this));
        } else if (Keybinds.OPEN_CHAT.keycodes.contains(keycode)) {
            gameMain.setScreen(new ChatScreen(this, gameMain));
        } else if (keycode == Input.Keys.H) {
            //shipping menu jjjjjjjjjjjj
            gameMain.setScreen(new ShippingMenu(gameMain, this));
        } else if (keycode == Input.Keys.N) {
            gameMain.setScreen(new JournalMenu(gameMain, this));
        } else if (keycode == Input.Keys.U) {
            for (Player player : ClientApp.currentGameData.getPlayers()) {
                if ((player != ClientApp.currentPlayer) && (Coordinate.calculateEuclideanDistance(ClientApp.currentPlayer.getCoordinate(), player.getCoordinate()) <= Math.sqrt(2))) {
                    Gdx.input.setInputProcessor(popupStage);
                    Vector2 stageCoords = popupStage.screenToStageCoordinates(new Vector2(player.getCoordinate().getX(), player.getCoordinate().getX()));
                    Skin skin = AssetManager.getSkin();


                    Window popup = new Window(player.getUser().getUsername(), skin);
                    popup.setSize(300, 500);
                    popup.setPosition(stageCoords.x, stageCoords.y, Align.topLeft);
                    Label label = new Label("Interactions with " + player.getUser().getUsername(), skin);
                    label.setWrap(true);
                    label.setAlignment(Align.center);

                    TextButton hugBtn = new TextButton("Hug", skin);
                    TextButton marryBtn = new TextButton("Marry", skin);
                    TextButton flowerBtn = new TextButton("Flower", skin);
                    TextButton exitBtn = new TextButton("Exit", skin);

                    //TODO animation
                    //TODO update game
                    hugBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            JsonObject req = new JsonObject();
                            req.addProperty("username", player.getUser().getUsername());
                            var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData + "/friendshipHug", req);
                            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                            if (res.getStatus() == 200) {
                                Gdx.input.setInputProcessor(FarmMenu.this);
                                popup.remove();
                            }
                        }
                    });
                    marryBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            JsonObject req = new JsonObject();
                            req.addProperty("username", player.getUser().getUsername());
                            req.addProperty("ring", "ring");
                            var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData + "/friendshipAskMarriage", req);
                            //TODO dastan dare hanooz kiram toosh bayad accept kne yaroo
                            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                            if (res.getStatus() == 200) {
                                Gdx.input.setInputProcessor(FarmMenu.this);
                                popup.remove();
                            }
                        }
                    });
                    flowerBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            Slot flowerSlot = null;
                            for (Slot slot : ClientApp.currentPlayer.getInventory().getSlots()) {
                                if (isFlower(slot.getItem().getName()))
                                    flowerSlot = slot;
                            }
                            if (flowerSlot != null) {
                                JsonObject req = new JsonObject();
                                req.addProperty("username", player.getUser().getUsername());
                                req.addProperty("flowerName", flowerSlot.getItem().getName());
                                var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData + "/friendshipFlower", req);
                                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                                if (res.getStatus() == 200) {
                                    Gdx.input.setInputProcessor(FarmMenu.this);
                                    popup.remove();
                                }
                            }
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
                    popup.add(hugBtn).row();
                    popup.add(marryBtn).row();
                    popup.add(flowerBtn);
                    popup.add(exitBtn);

                    popupStage.addActor(popup);
                }
            }
        }
        return false;
    }

    public boolean isFlower(String flowerName) {
        return flowerName.equals("Sweat Pea") || flowerName.equals("Crocus") || flowerName.equals("Tulip") || flowerName.equals("Blue Jazz") || flowerName.equals("Summer Spangle") || flowerName.equals("Poppy") || flowerName.equals("Sunflower") || flowerName.equals("Fairy Rose");
    }

    @Override
    public boolean keyUp(int keycode) {
        if (Keybinds.SPAWN_CROW.keycodes.contains(keycode)) {
            crowFlag = false;
        }
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
            if (playerController.getPlayer().getEquippedItem().getName().contains("Rod")) {
                boolean check = playerController.useFishingRod();
                if (check) {
                    boolean flag = false;
                    Slot slot = playerController.getPlayer().getInventory().getSlotByItemName("Sonar Bobber");
                    if (slot != null) {
                        flag = true;
                    }
                    gameMain.setScreen(new FishingMiniGame(gameMain, this, flag, playerController.getPlayer().
                        getEquippedItem().getQuality()));
                }
            } else
                playerController.toolUse();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            boolean success = playerController.dropItem(ClientApp.currentPlayer, farm);
            if (success) {
                selectedIndex = -1;
                selectedSave = -1;
                selected = false;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            System.out.println("hello");
            pauseMenu.togglePauseMenu();
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
                    playerController.equipItem(null);
                } else {
                    selectedIndex = actualIndex;
                    playerController.equipItem(null);
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
                            if (!artisanBlock.canBeCollected) {
                                JsonObject req = new JsonObject();
                                req.addProperty("artisanName", artisanBlock.getArtisanType().name);
                                var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData + "/artisanCheat", req);
                                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                                if (res.getStatus() == 200) {
                                    String player = res.getBody().toString();
                                    ((FarmMenu) farmScreen).getPlayerController().updatePlayerObject(player);
                                    Gdx.input.setInputProcessor(FarmMenu.this);
                                    popup.remove();
                                }
                            }
                        }
                    });
                    collectBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (artisanBlock.canBeCollected) {
                                JsonObject req = new JsonObject();
                                req.addProperty("artisanName", artisanBlock.getArtisanType().name);
                                var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData + "/artisanHandleArtisanGet", req);
                                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                                if (res.getStatus() == 200) {
                                    String player = res.getBody().toString();
                                    ((FarmMenu) farmScreen).getPlayerController().updatePlayerObject(player);
                                    Gdx.input.setInputProcessor(FarmMenu.this);
                                    popup.remove();
                                }
                            }
                        }
                    });
                    cancelBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            JsonObject req = new JsonObject();
                            req.addProperty("artisanName", artisanBlock.getArtisanType().name);
                            var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData + "/artisanCancel", req);
                            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                            if (res.getStatus() == 200) {
                                String player = res.getBody().toString();
                                ((FarmMenu) farmScreen).getPlayerController().updatePlayerObject(player);
                                Gdx.input.setInputProcessor(FarmMenu.this);
                                popup.remove();
                            }
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

        emojiSelection(screenX, prev_screen_y);
        Vector2 worldsCoords = stage.screenToStageCoordinates(new Vector2(screenX, prev_screen_y));
        float spacing = 20f;
        int count = 4;
        float totalWidth = count * 64 + (count - 1) * spacing;
        float startXasghar = (stage.getWidth() - totalWidth) / 2f + 400;
        float y = stage.getHeight() - 64 - 20f;
        if (worldsCoords.x >= startXasghar && worldsCoords.x <= startXasghar + totalWidth &&
            worldsCoords.y >= y && worldsCoords.y <= y + 64) {
            gameMain.setScreen(new InteractionsMenu(gameMain, FarmMenu.this));
            return true;
        }
        return true;
    }

    private void emojiSelection(float screenX, float screenY) {
        float emojiSize = 64f;
        float spacing = 20f;
        int count = emojiTextures.size();
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float totalWidth = count * emojiSize + (count - 1) * spacing;
        float starX = (screenWidth - totalWidth) / 2f;
        float y = screenHeight - emojiSize - 20f;

        float touchY = screenHeight - screenY;

        for (int i = 0; i < count; i++) {
            float x = starX + i * (emojiSize + spacing);

            if (screenX >= x && screenX <= x + emojiSize &&
                touchY >= y && touchY <= y + emojiSize) {
                //TODO Texture
                playerController.getPlayer().currentEmoji = emojiTextures.get(i);
                playerController.getPlayer().emojiCounter = 0f;
                break;
            }
        }
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
            if (!Objects.equals(id, playerController.getPlayer().getUser_id())) {
                String player = res.get("player");
                playerController.updateAnotherPlayerObject(player);
            }
        } else if (type.equals("GAME_UPDATED")) {
            String game = res.get("game");
            playerController.updateGame(game);
        } else if (type.equals("MESSAGE_ADDED")) {
            String messageJson = res.get("message");
            System.out.println(messageJson);
            ChatMessage chatMsg = gson.fromJson(messageJson, ChatMessage.class);

            String prefix = "@" + ClientApp.loggedInUser.getUsername() + " ";

            if (chatMsg.message.startsWith(prefix)) {
                showPopUp(chatMsg.sender + ": " + chatMsg.message.substring(prefix.length()), "Chat Message");
            }
        } else if (type.equals("EMOJI_SENT")) {
            // TODO pouya do something

        } else if (type.equals("MUSIC_QUERY")) {
            var req = new JsonObject();

            if (gameMain.music != null) {
                req.addProperty("isPlaying", gameMain.music.isPlaying());
                req.addProperty("musicName", gameMain.playingMusicName);
                req.addProperty("musicPos", (double) gameMain.music.getPosition());
            } else {
                req.addProperty("isPlaying", false);
                req.addProperty("musicName", "");
                req.addProperty("musicPos", 0.0);
            }

            var postRes = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/music/sync_res", req);

        } else if (type.equals("PLAYER_VOTING")) {
            String playerId = res.get("player_user_id");
            Player player = ClientApp.currentGameData.findPlayerByUserId(playerId);
            voteFlag = true;
            votedPlayer = player;
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    private void updateSeasonGrassTexture() {

        grassTexture = SeasonTextures.giveSeasonTexture(ClientApp.currentGameData.getSeason());
    }

    private void handleLightning(OrthographicCamera camera, float delta) {

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

    private void updateTime(float delta) {
        LocalDateTime time = ClientApp.currentGameData.getDate();
        float timeOfDay = (time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond()) / 3600f;

        if (timeOfDay >= 18f) {
            nightFactor = (timeOfDay - 18f) / 6f;
        } else if (timeOfDay <= 6f) {
            nightFactor = (timeOfDay + 6f) / 6f;
        } else {
            nightFactor = 0f;
        }
        nightFactor = MathUtils.clamp(nightFactor, 0f, 0.8f);
    }

    @Override
    public void render(float delta) {
        if (voteFlag) {
            gameMain.setScreen(new VoteMenu(gameMain, this, votedPlayer));
        }
        if (playerController.getPlayer().isInVillage()) {
            gameMain.setScreen(new VillageMenu(this, gameMain));
        } else {
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
    }

    private void handleUI(float delta) {
        pauseMenu.render();

        stage.act(delta);
        stage.draw();

        chatNotifStage.act(delta);
        chatNotifStage.draw();
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
        Texture mineTexture = AssetManager.getImage("mineCell");
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 75; j++) {
                modifiedDraw(batch, texture, j, i);

                if (isMineCell(j, i)) {
                    modifiedDraw(batch, mineTexture, j, i);
                }
            }
        }

        for (Cell cell : farm.getCells()) {
            Coordinate coordinate = cell.getCoordinate();

            float xOfCell = coordinate.getX();
            float yOfCell = coordinate.getY();
            Texture texture1 = grassTexture;

            if (cell.getObjectOnCell() instanceof Tree) {
                continue;
            }

            if (!(cell.getObjectOnCell() instanceof BuildingBlock)) {
                texture1 = cell.getObjectOnCell().getTexture();
            }

            if (texture1 == SeasonTextures.SPRING.texture ||
                texture1 == SeasonTextures.SUMMER.texture ||
                texture1 == SeasonTextures.FALL.texture ||
                texture1 == SeasonTextures.WINTER.texture) {
                continue;
            }

            if (texture1 == grassTexture && cell.isTilled()) {
                texture1 = AssetManager.getImage("tilled");
            }
            // TODO check for giant crop correct rendering
            if (cell.getObjectOnCell() instanceof DroppedItem || cell.getObjectOnCell() instanceof ArtisanBlock) {
                modifiedDraw(batch, texture1, xOfCell, yOfCell, 30, 30);
            } else if (cell.getObjectOnCell() instanceof ForagingMineral) {
                modifiedDraw(batch, texture1, xOfCell, yOfCell, 30, 30);
            } else {
                modifiedDraw(batch, texture1, xOfCell, yOfCell);
            }
        }

        if (crowFlag) {
            batch.draw(AssetManager.getImage("crow"), playerPosition.x - 64, playerPosition.y, 32, 32);
        }

        playerController.render(batch);
        for (Cell cell : farm.getCells()) {
            if (cell.getObjectOnCell() instanceof Tree) {
                Coordinate coordinate = cell.getCoordinate();
                drawFuckingTreeProperly(batch, cell.getObjectOnCell().getTexture(), coordinate.getX(), coordinate.getY());
            }
            if (cell.getObjectOnCell() instanceof BuildingBlock) {
                if (((BuildingBlock) cell.getObjectOnCell()).buildingType.equals("shippingBin")) {
                    modifiedDraw(batch, AssetManager.getImage("shippingbin"), cell.getCoordinate().getX(), cell.getCoordinate().getY(), 32, 32);
                }
            }
        }
        //Draw buildings
        Texture house = AssetManager.getImage("playerhouse");
        Texture greenhouse = isGreenhouseBuilt() ? AssetManager.getImage("greenhouse") : AssetManager.getImage("greenhousedestroyed");

        modifiedDraw(batch, house, 61, 8);
        modifiedDraw(batch, greenhouse, 22, 6);

        //Dummy texture to solve buffering.
        modifiedDraw(batch, AssetManager.getImage("speedgro"), playerPosition.x, playerPosition.y);
        popupStage.act(delta);
        popupStage.draw();

        artisanTimeBar();

        stage.dispose();
        initializeStage(delta);
        batch.end();
    }

    private boolean isGreenhouseBuilt() {
        try (FileInputStream fis = new FileInputStream(System.getenv("address") + "/greenhouse.txt")) {
            byte[] data = fis.readAllBytes();
            String greenhouse = new String(data);
            greenhouse = greenhouse.trim();
            return Boolean.parseBoolean(greenhouse);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    public void drawFuckingTreeProperly(SpriteBatch batch, Texture texture, float xOfCell, float yOfCell) {
        modifiedDraw(batch, texture, xOfCell - 1, yOfCell);
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

    private boolean isMineCell(int x, int y) {
        return x <= 9 && y <= 11;
    }

    private boolean isGreenhouseCell(int x, int y) {
        // Greenhouse runs from x : [22, 25] & y : [3, 6] (4 by 4)
        return x >= 20 && x <= 27 && y >= 1 && y <= 8;
    }

    private synchronized void showPopUp(String message, String promptType) {
        temp = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(chatNotifStage);
        UIPopupHelper uiPopupHelper = new UIPopupHelper(chatNotifStage, AssetManager.getSkin());
        uiPopupHelper.showDialog(message, promptType, temp);
    }
}
