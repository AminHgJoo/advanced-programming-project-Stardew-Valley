package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;

public class ChatScreen implements MyScreen {
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;

    private final GameMain gameMain;
    private final FarmMenu farmMenu;

    private Table rootTable;
    private Table messagesTable;
    private ScrollPane scrollPane;
    private TextField inputField;

    public ChatScreen(FarmMenu farmMenu, GameMain gameMain) {
        this.farmMenu = farmMenu;
        this.gameMain = gameMain;

        this.skin = AssetManager.getSkin();
        this.backgroundTexture = AssetManager.getImage("profileBackground");

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top();
        stage.addActor(rootTable);

        messagesTable = new Table();
        messagesTable.top().left();

        scrollPane = new ScrollPane(messagesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        inputField = new TextField("", skin);
        inputField.setMessageText("Type your message...");
        inputField.setTextFieldListener((textField, c) -> {
            if (c == '\n' || c == '\r') {
                String msg = textField.getText().trim();
                if (!msg.isEmpty()) {
                    addMessage("Me: " + msg);
                    textField.setText("");
                }
            }
        });

        rootTable.add(scrollPane)
            .expand().fill().pad(10)
            .row();
        rootTable.add(inputField)
            .expandX().fillX().pad(10).height(30);

        Gdx.input.setInputProcessor(stage);
    }

    private void addMessage(String text) {
        Label msgLabel = new Label(text, skin);
        msgLabel.setWrap(true);
        messagesTable.add(msgLabel)
            .expandX().fillX().pad(2)
            .row();

        Gdx.app.postRunnable(() -> {
            scrollPane.layout();
            scrollPane.setScrollPercentY(1f);
        });
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.12f, 1);
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
}
