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
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.UIPopupHelper;
import com.google.gson.JsonObject;

public class ChangePasswordMenu implements Screen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;

    public ChangePasswordMenu(GameMain gameMain) {
        this.gameMain = gameMain;
        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getTextures().get("profileBackground");

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Label label = new Label("Change Password", skin);
        label.setColor(Color.RED);
        label.setFontScale(2f);

        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("New Password");

        TextField oldPasswordField = new TextField("", skin);
        oldPasswordField.setMessageText("Old Password");


        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var req = new JsonObject();
                req.addProperty("newPassword", passwordField.getText());
                req.addProperty("oldPassword", oldPasswordField.getText());
                var postResponse = HTTPUtil.post("http://localhost:8080/api/user/changePassword", req);
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
                }
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ProfileMenu(gameMain));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.center();
        table.padTop(50);
        table.add(label).pad(10).row();
        table.add(oldPasswordField).width(500).height(60).pad(10).row();
        table.add(passwordField).width(500).height(60).pad(10).row();
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
