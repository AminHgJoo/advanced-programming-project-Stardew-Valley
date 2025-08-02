package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.NPCModels.NPC;

public class NPCChatScreen implements MyScreen {
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
    private TextButton sendButton;
    private TextButton giftButton;

    public NPCChatScreen(NPC npc , VillageMenu villageMenu , GameMain gameMain) {
        this.npc = npc;
        this.npcName = this.npc.getName();
        this.npcPortrait = new Texture(Gdx.files.internal("images/npc/" + npcName + "/portrait-1.png"));
        this.villageMenu = villageMenu;
        this.gameMain = gameMain;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = AssetManager.getSkin();

        createUI();
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

        Image portraitImage = new Image(npcPortrait);
        sidebar.add(portraitImage).width(150).height(200).row();

        Label nameLabel = new Label(npcName, skin, "title");
        sidebar.add(nameLabel).row();

        giftButton = new TextButton("Send Gift", skin);
        sidebar.add(giftButton).width(150).height(60).row();

        sidebar.add().expandY();
    }

    private void createChatArea() {
        chatArea = new Table();
        chatArea.defaults().expandX().fillX();

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
                System.out.println("Gift button clicked");
            }
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            addMessage(message, true);
            messageInput.setText("");

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    addMessage("This is an NPC response to: " + message, false);
                }
            }, 1);
        }
    }

    private void addMessage(String text, boolean isUser) {
        Table messageTable = new Table();
        Label messageLabel = new Label(text, skin);
        messageLabel.setWrap(true);

        if (isUser) {
            messageTable.add(messageLabel).width(chatArea.getWidth() * 0.6f)
                .left().pad(5);
        } else {
            messageTable.add(messageLabel).width(chatArea.getWidth() * 0.6f)
                .right().pad(5);
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
        skin.dispose();
    }

    @Override
    public void socketMessage(String message) {

    }
}
