package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.controllers.NpcController;
import com.client.controllers.PlayerVillageController;
import com.client.utils.*;
import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.NPCModels.NPC;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.mapModels.Coordinate;
import com.common.utils.ChatMessage;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.common.GameGSON.gson;

public class VillageMenu implements MyScreen, InputProcessor {
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
    private FarmMenu farmMenu;

    public PlayerVillageController getPlayerController() {
        return playerController;
    }

    private PlayerVillageController playerController;
    private Stage stage;
    private Stage popUpStage;
    private Stage chatNotifStage;
    private GameMain gameMain;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private boolean showingQuestion = false;
    private HashMap<String, PlayerVillageController> playerControllers = new HashMap<>();
    private HashMap<String, NpcController> npcControllers = new HashMap<>();
    private GameData game = ClientApp.currentGameData;
    private Player player = ClientApp.currentPlayer;
    private Stage popupStage;
    private ArrayList<Texture> emojiTextures;

    private InputProcessor temp;

    public VillageMenu(FarmMenu farmMenu, GameMain gameMain) {
        player.setCoordinate(new Coordinate(TILE_PIX_SIZE * FARM_X_SPAN / 2, TILE_PIX_SIZE * FARM_Y_SPAN / 2));
        this.farmMenu = farmMenu;
        this.gameMain = gameMain;
        batch = new SpriteBatch();
        stage = new Stage();
        chatNotifStage = new Stage();
        Gdx.input.setInputProcessor(this);
        popUpStage = new Stage();
        this.playerPosition = new Vector2(player.getCoordinate().getX(), player.getCoordinate().getY());
        this.playerVelocity = new Vector2();
        for (Player p : game.getPlayers()) {
            Vector2 x = new Vector2(p.getCoordinate().getX(), p.getCoordinate().getY());
            Vector2 y = new Vector2();
            if (p.getUser_id().equals(ClientApp.currentPlayer.getUser_id())) {
                x = playerPosition;
                y = playerVelocity;
            }
            playerControllers.put(p.getUser_id(), new PlayerVillageController(x, y, this, p));
        }
        for (NPC npc : game.getMap().getVillage().getNpcs()) {
            npcControllers.put(npc.getName(), new NpcController(this, npc));
        }
        this.playerController = playerControllers.get(player.getUser_id());
        backgroundTexture = AssetManager.getImage("stardewvillageday");
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);
        popupStage = new Stage(new ScreenViewport());
        initializeEmoji();
        emojiShow(0.01f);
        playerController.getPlayer().emojiCounter = -1;
        float emojiSize = 64f;
        float spacing = 20f;
        int count = 4;
        float totalWidth = count * emojiSize + (count - 1) * spacing;
        float startX = (stage.getWidth() - totalWidth) / 2f + 400;
        float y = stage.getHeight() - emojiSize - 20f;

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

                playerController.getPlayer().currentEmoji = emojiTextures.get(i);
                playerController.getPlayer().emojiCounter = 0f;
                break;
            }
        }
    }
    private void initializeEmoji() {
        emojiTextures = new ArrayList<>();
        emojiTextures.add(AssetManager.getImage("happy"));
        emojiTextures.add(AssetManager.getImage("sad"));
        emojiTextures.add(AssetManager.getImage("anger"));
        emojiTextures.add(AssetManager.getImage("laugh"));
    }

    public void renderPlayers() {
        for (Player p : game.getPlayers()) {
            if (p.isInVillage())
                playerControllers.get(p.getUser_id()).render(batch);
        }
    }

    public void renderNpcs(float delta) {
        for (NPC npc : game.getMap().getVillage().getNpcs()) {
            npcControllers.get(npc.getName()).render(batch, delta);
        }
    }

    @Override
    public void socketMessage(String message) {
        HashMap<String, String> res = (HashMap<String, String>) gson.fromJson(message, HashMap.class);
        String type = res.get("type");

        if (type.equals("GAME_UPDATED")) {
            String gameJson = res.get("game");
            GameData game = gson.fromJson(gameJson, GameData.class);
            ClientApp.currentGameData = game;
            this.game = game;
            for (Map.Entry<String, PlayerVillageController> entry : playerControllers.entrySet()) {
                entry.getValue().updateGame();
            }
            for (Map.Entry<String, NpcController> entry : npcControllers.entrySet()) {
                entry.getValue().updateGame();
            }
        } else if (type.equals("PLAYER_UPDATED")) {
            String id = res.get("player_user_id");
            if (!id.equals(player.getUser_id())) {
                System.out.println(player.getUser().getUsername());
                PlayerVillageController controller = playerControllers.get(id);
                String playerJson = res.get("player");
                Player player1 = gson.fromJson(playerJson, Player.class);
                controller.updateGamePlayer(player1);
            }
        } else if (type.equals("MESSAGE_ADDED")) {
            String messageJson = res.get("message");
            System.out.println(messageJson);
            ChatMessage chatMsg = gson.fromJson(messageJson, ChatMessage.class);

            String prefix = "@" + ClientApp.loggedInUser.getUsername() + " ";

            if (chatMsg.message.startsWith(prefix)) {
                showPopUp(chatMsg.sender + ": " + chatMsg.message.substring(prefix.length()), "Chat Message");
            }
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

        }
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

    private synchronized void showPopUp(String message, String promptType) {
        temp = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(chatNotifStage);
        UIPopupHelper uiPopupHelper = new UIPopupHelper(chatNotifStage, AssetManager.getSkin());
        uiPopupHelper.showDialog(message, promptType, temp);
    }

    public void showGoToStorePopUp(String name) {
        if (!showingQuestion) {
            showingQuestion = true;
            VillageMenu menu = this;
            ConfirmAlert alert = new ConfirmAlert("question", "Do you want to go to " + name + " ?",
                AssetManager.getSkin()) {
                @Override
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    Gdx.input.setInputProcessor(stage);
                    showingQuestion = false;
                    if (result) {
                        gameMain.setScreen(new StoreInterface(gameMain, name, menu));
                    } else {
                        playerPosition.y -= 10;
                    }

                    remove();
                }
            };
            alert.show(popUpStage);
            Gdx.input.setInputProcessor(popUpStage);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
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
        } else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            System.out.println(playerPosition.x + " , " + playerPosition.y);

        } else {
            playerController.setState(PlayerState.IDLE);
            playerVelocity.x = 0;
            playerVelocity.y = 0;
        }
    }

    public void showGoToFarmPopUp() {
        if (!showingQuestion) {
            showingQuestion = true;
            ConfirmAlert alert = new ConfirmAlert("question", "Do you want to go to your farm ?",
                AssetManager.getSkin()) {
                @Override
                protected void result(Object object) {
                    boolean result = (boolean) object;
                    Gdx.input.setInputProcessor(stage);
                    showingQuestion = false;
                    if (result) {
                        var postResponse = HTTPUtil.post("/api/game/" + game.get_id() + "/movementGoToFarm", new JsonObject());
                        Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                        System.out.println(res.getMessage());
                        if (res.getStatus() == 200) {
                            player.setInVillage(false);
                            player.setCoordinate(new Coordinate(1200, 800));
                            gameMain.setScreen(farmMenu);
                            dispose();
                        } else {
                            System.out.println("Error");
                        }
                    } else {
                        playerPosition.y -= 10;
                    }

                    remove();
                }
            };
            alert.show(popUpStage);
            Gdx.input.setInputProcessor(popUpStage);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        playerController.updatePlayerPos(delta);
        playerController.update(delta);
        clearAndResetScreen();
        if (!showingQuestion)
            handleEvents();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderPlayers();
        renderNpcs(delta);
        batch.end();
        handleUI(delta);

    }

    public void handleUI(float delta) {
        stage.act(delta);
        stage.draw();
        popUpStage.act(delta);
        popUpStage.draw();
        popupStage.act(delta);
        popupStage.draw();
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
        popupStage.dispose();
        popUpStage.dispose();
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

    @Override
    public boolean keyDown(int i) {
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        NpcController nc = null;
        for (Map.Entry<String, NpcController> entry : npcControllers.entrySet()) {
            if (entry.getValue().isInPosition((int) touchPos.x, (int) touchPos.y)) {
                nc = entry.getValue();
            }
        }
        if (nc != null) {
            gameMain.setScreen(new NPCChatScreen(nc.getNpc(), this, gameMain));
        }
        for (Player player : ClientApp.currentGameData.getPlayers()) {
            if ((player != ClientApp.currentPlayer) && (player.getCoordinate().getX() <= touchPos.x + 80 && player.getCoordinate().getX() >= touchPos.x - 80) && (player.getCoordinate().getY() <= touchPos.y + 80 && player.getCoordinate().getY() >= touchPos.y - 80)) {
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
                hugBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        JsonObject req = new JsonObject();
                        req.addProperty("username", player.getUser().getUsername());
                        var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData + "/friendshipHug", req);
                        Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                        if (res.getStatus() == 200) {
                            String game = res.getBody().toString();
                            VillageMenu.this.playerController.updateGame(game);
                            Gdx.input.setInputProcessor(VillageMenu.this);
                            popup.remove();
                        } else {
                            String error = res.getBody().toString();
                            showPopUp(error, "Error");
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
                            String game = res.getBody().toString();
                            VillageMenu.this.playerController.updateGame(game);
                            Gdx.input.setInputProcessor(VillageMenu.this);
                            popup.remove();
                        } else {
                            String error = res.getBody().toString();
                            showPopUp(error, "Error");
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
                                String game = res.getBody().toString();
                                VillageMenu.this.playerController.updateGame(game);
                                Gdx.input.setInputProcessor(VillageMenu.this);
                                popup.remove();
                            } else {
                                String error = res.getBody().toString();
                                showPopUp(error, "Error");
                            }
                        }

                    }
                });
                exitBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Gdx.input.setInputProcessor(VillageMenu.this);
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
        emojiSelection(screenX, screenY);
        return false;
    }

    public boolean isFlower(String flowerName) {
        return flowerName.equals("Bouquet");
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

    public void setGame(GameData game) {
        this.game = game;
    }
}
