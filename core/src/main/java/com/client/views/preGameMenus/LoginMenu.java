package com.client.views.preGameMenus;

import com.badlogic.gdx.Gdx;
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
import com.client.utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

public class LoginMenu implements MyScreen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;

    public LoginMenu(GameMain gameMain) {
        this.gameMain = gameMain;
        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getTextures().get("launcherBackground");

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Label label = new Label("Login", skin);
        label.setColor(Color.RED);
        label.setFontScale(2f);

        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");

        CheckBox stayLoggedInCheckBox = new CheckBox("Stay Logged in", skin);
        stayLoggedInCheckBox.setChecked(true);

        TextButton loginButton = new TextButton("Login", skin);
        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                var req = new JsonObject();
                req.addProperty("username", usernameField.getText());
                req.addProperty("password", passwordField.getText());

                var postResponse = HTTPUtil.post("http://localhost:8080/api/user/login", req);

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
                    var token = postResponse.getHeaders().get("Authorization").get(0);
                    ClientApp.token = token;

                    if (stayLoggedInCheckBox.isChecked()) {
                        System.out.println(System.getProperty("user.dir"));
                        FileUtil.write(System.getenv("address"), "TOKEN=" + ClientApp.token);
                    }

                    try {
                        System.out.println(response.getBody());
                        LinkedTreeMap map = (LinkedTreeMap) response.getBody();
                        Gson gson = new Gson();
                        String json = gson.toJson(map);
                        System.out.println(json);
                        ClientApp.loggedInUser = ModelDecoder.decodeUser(json);
                        gameMain.setScreen(new MainMenu(gameMain));
                    } catch (Exception e) {
                        e.printStackTrace();
                        UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                        uiPopupHelper.showDialog("A server error occurred.", "Error");
                    }
                }
            }
        });

        Label label2 = new Label("Account Recovery", skin);
        label2.setColor(Color.RED);
        label2.setFontScale(2f);

        TextField oldUsernameField = new TextField("", skin);
        oldUsernameField.setMessageText("Username");

        TextField newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("New Password");

        TextField securityAnswerField = new TextField("", skin);
        securityAnswerField.setMessageText("Security Answer");

        TextButton recoverAccountButton = new TextButton("Recover Account", skin);
        recoverAccountButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                var req = new JsonObject();
                req.addProperty("username", oldUsernameField.getText());
                req.addProperty("newPassword", newPasswordField.getText());
                req.addProperty("securityAnswer", securityAnswerField.getText());

                var postResponse = HTTPUtil.post("http://localhost:8080/api/user/forgetPassword", req);

                if (postResponse == null) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Connection failed.", "Error");
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
                gameMain.setScreen(new LauncherMenu(gameMain));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.center();
        table.padTop(50);
        table.add(label).pad(10).row();
        table.add(usernameField).width(500).height(60).pad(10).row();
        table.add(passwordField).width(500).height(60).pad(10).row();
        table.add(stayLoggedInCheckBox).width(500).height(60).pad(10).row();
        table.add(loginButton).width(500).height(60).pad(10).row();
        table.add(label2).pad(10).row();
        table.add(oldUsernameField).width(500).height(60).pad(10).row();
        table.add(newPasswordField).width(500).height(60).pad(10).row();
        table.add(securityAnswerField).width(500).height(60).pad(10).row();
        table.add(recoverAccountButton).width(500).height(60).pad(10).row();
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

    @Override
    public void socketMessage(String message) {

    }
}
