package com.client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.ModelDecoder;
import com.client.utils.UIPopupHelper;
import com.common.models.networking.Lobby;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.server.utilities.Validation;

public class CreateLobby implements Screen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;
    private boolean isVisible;
    private boolean isPrivate;

    public CreateLobby(GameMain gameMain) {
        this.gameMain = gameMain;
        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getTextures().get("mainMenuBackground");
        isVisible = false;
        isPrivate = false;
        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Label label = new Label("Create Lobby", skin);
        label.setColor(Color.RED);
        label.setFontScale(2f);

        TextField nameField = new TextField("", skin);
        nameField.setMessageText("name");
        CheckBox isPrivateLobby = new CheckBox("Private Lobby", skin);
        isPrivateLobby.getLabel().setColor(Color.CYAN);
        isPrivateLobby.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPrivateLobby.setChecked(isPrivateLobby.isChecked());
                isPrivate = !isPrivate;
            }
        });
        isPrivateLobby.setChecked(false);

        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("password");

        CheckBox isVisibleLobby = new CheckBox("Visible Lobby", skin);
        isVisibleLobby.getLabel().setColor(Color.CYAN);
        isVisibleLobby.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isVisibleLobby.setChecked(isVisibleLobby.isChecked());
                isVisible = !isVisible;
            }
        });
        isVisibleLobby.setChecked(false);

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var req = new JsonObject();
                req.addProperty("name", nameField.getText() );
                req.addProperty("isVisible" , isVisibleLobby.isChecked());
                req.addProperty("isPublic", !isPrivateLobby.isChecked());
                req.addProperty("password", passwordField.getText());

                var postResponse = HTTPUtil.post("http://localhost:8080/api/lobby/", req);
                if (postResponse == null) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Connection to server failed.", "Error");
                    return;
                }
                var response = HTTPUtil.deserializeHttpResponse(postResponse);
                if (response.getStatus() == 400 || response.getStatus() == 404) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog(response.getMessage(), "Error");
                    return;
                }

                if (response.getStatus() == 200) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog(response.getMessage(), "Success");
                    LinkedTreeMap map = (LinkedTreeMap) response.getBody();
                    Gson gson = new Gson();
                    String json = gson.toJson(map);
                    Lobby lobby = ModelDecoder.decodeLobby(json);
                    gameMain.setScreen(new GameLobbyMenu(gameMain, lobby));

                }
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new GameLobbyMenu(gameMain, null));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.center();
        table.padTop(50);

        table.add(label).pad(10).row();
        table.add(nameField).width(500).height(60).padLeft(10).row();
        table.add(passwordField).width(500).height(60).pad(10).row();
        table.add(isPrivateLobby).pad(10).row();
        table.add(isVisibleLobby).pad(10).row();
        table.add(confirmButton).width(500).height(60).pad(10).row();
        table.add(backButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
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
