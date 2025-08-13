package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.client.utils.UIPopupHelper;
import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.NPCModels.NPC;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NPCChatScreen implements MyScreen {
    private final ExecutorService networkThreadPool = Executors.newFixedThreadPool(2);
    private Stage stage;
    private Skin skin;
    private VillageMenu villageMenu;
    private GameMain gameMain;
    private String npcName;
    private NPC npc;
    private Texture npcPortrait;

    private Table mainTable;
    private Table sidebar;
    private Table chatArea;
    private ScrollPane chatScrollPane;
    private Table messageContainer;
    private TextField messageInput;
    private TextField giftItemName;
    private TextButton sendButton;
    private TextButton giftButton;
    private Label friendshipLabel;

    public NPCChatScreen(NPC npc, VillageMenu villageMenu, GameMain gameMain) {
        this.npc = npc;
        this.npcName = this.npc.getName();
        this.npcPortrait = new Texture(Gdx.files.internal("images/npc/" + npcName + "/portrait-1.png"));
        this.villageMenu = villageMenu;
        this.gameMain = gameMain;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = AssetManager.getSkin();

        createUI();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    private void createUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        createSidebar();

        createChatArea();

        mainTable.add(chatArea).expand().fill();
        mainTable.add(sidebar).width(200).fillY();
    }

    private void createSidebar() {
        sidebar = new Table();
        sidebar.defaults().pad(10);
        sidebar.setBackground(new TextureRegionDrawable(createColoredTexture(Color.BROWN)));

        Image portraitImage = new Image(npcPortrait);
        Table portraitContainer = new Table();
        portraitContainer.setBackground(new TextureRegionDrawable(createColoredTexture(Color.WHITE)));
        portraitContainer.add(portraitImage).width(150).height(150);
        sidebar.add(portraitContainer).width(160).height(160).padBottom(20).row();

        Label nameLabel = new Label(npcName, skin, "title");
        sidebar.add(nameLabel).row();
        giftItemName = new TextField("Item name", skin);
        sidebar.add(giftItemName).width(150).height(40).row();

        giftButton = new TextButton("Send Gift", skin);
        sidebar.add(giftButton).width(150).height(60).row();
        TextButton backButton = new TextButton("Back", skin);
        sidebar.add(backButton).width(150).height(60).row();
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                gameMain.setScreen(villageMenu);
            }
        });
        friendshipLabel = new Label("Friend Level: " + fetchFriendshipLevel(npcName) + "/10", skin);
        sidebar.add(friendshipLabel).width(150).height(60).row();

        sidebar.add().expandY();

    }

    private TextureRegion createColoredTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(texture);
    }


    private void createChatArea() {
        chatArea = new Table();
        chatArea.defaults().expandX().fillX();
        chatArea.setBackground(new TextureRegionDrawable(createColoredTexture(Color.GRAY)));

        messageContainer = new Table();
        messageContainer.defaults().pad(5);

        chatScrollPane = new ScrollPane(messageContainer, skin);
        chatScrollPane.setFadeScrollBars(false);
        chatScrollPane.setScrollingDisabled(true, false);

        Table inputTable = new Table();
        messageInput = new TextField("", skin);
        sendButton = new TextButton("Send", skin);

        inputTable.add(messageInput).expandX().fillX();
        inputTable.add(sendButton).width(80);

        chatArea.add(chatScrollPane).expand().fill().row();
        chatArea.add(inputTable).fillX().pad(5);

        setupListeners();
    }

    private void setupListeners() {
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendMessage();
            }
        });

        messageInput.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n') {
                    sendMessage();
                }
            }
        });

        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                networkThreadPool.execute(()->{
                    JsonObject req = new JsonObject();
                    req.addProperty("item", giftItemName.getText());
                    req.addProperty("npcName", npcName);

                    var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/npcGift" , req);

                    Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                    Gdx.app.postRunnable(()->{
                        if(res.getStatus() == 200){
                            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage , skin);
                            uiPopupHelper.showDialog("Gift was sent successfully" , "Success");
                            String gameJson = res.getBody().toString();
                            System.out.println(gameJson);
                            GameData gameData = GameGSON.gson.fromJson(gameJson, GameData.class);
                            ClientApp.currentGameData = gameData;
                        }else {
                            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage , skin);
                            uiPopupHelper.showDialog(res.getMessage() , "Error");
                        }
                    });
                });
            }
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            addMessage(message, true);
            messageInput.setText("");
            networkThreadPool.execute(() -> {
                JsonObject req = new JsonObject();
                req.addProperty("npcName", npcName);
                req.addProperty("message", message);

                var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/npcTalk", req);
                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                Gdx.app.postRunnable(() -> {
                    if (res.getStatus() == 200) {
                        String msg = res.getMessage().toString();
                        addMessage(msg, false);
                    } else {
                        System.out.println(res.getMessage());
                    }
                });
            });
        }
    }

    private void addMessage(String text, boolean isUser) {
        Table messageTable = new Table();
        Label messageLabel = new Label(text, skin);
        messageLabel.setWrap(true);

        if (isUser) {
            messageTable.add(messageLabel).width(chatArea.getWidth() * 0.5f)
                .left().pad(5);
            messageTable.setBackground(new TextureRegionDrawable(createColoredTexture(Color.SKY)));

        } else {
            messageTable.add(messageLabel).width(chatArea.getWidth() * 0.5f)
                .right().pad(5);
            messageTable.setBackground(new TextureRegionDrawable(createColoredTexture(Color.GOLD)));

        }

        messageContainer.add(messageTable).expandX().fillX()
            .pad(5).row();

        messageContainer.layout();
        chatScrollPane.scrollTo(0, 0, 0, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
    }

    @Override
    public void socketMessage(String message) {

    }

    private String fetchFriendshipLevel(String npcName) {

        return "0";
    }
}
