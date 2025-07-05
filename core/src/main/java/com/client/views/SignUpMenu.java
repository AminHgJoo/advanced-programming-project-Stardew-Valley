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
import com.client.utils.*;
import com.common.models.enums.SecurityQuestion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

public class SignUpMenu implements Screen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;

    public SignUpMenu(GameMain gameMain) {
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

        Label label = new Label("Enter your information.", skin);
        label.setColor(Color.CORAL);
        label.setFontScale(2f);

        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");

        TextField passwordConfirmButton = new TextField("", skin);
        passwordConfirmButton.setMessageText("Confirm Password");

        TextField nameField = new TextField("", skin);
        nameField.setMessageText("Nickname");

        TextField emailField = new TextField("", skin);
        emailField.setMessageText("Email");

        TextField genderField = new TextField("", skin);
        genderField.setMessageText("Gender");

        Label securityQuestionLabel = new Label("Security Question", skin);
        securityQuestionLabel.setColor(Color.CORAL);
        securityQuestionLabel.setFontScale(2f);

        SelectBox<String> securityQuestionField = new SelectBox<>(skin);
        securityQuestionField.setItems(SecurityQuestion.listAllQuestions());

        TextField securityAnswerField = new TextField("", skin);
        securityAnswerField.setMessageText("Security Answer");

        TextField securityAnswerConfirmField = new TextField("", skin);
        securityAnswerConfirmField.setMessageText("Confirm Security Answer");

        TextButton confirmButton = new TextButton("Confirm", skin);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!passwordConfirmButton.getText().equals(passwordField.getText())) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Passwords does not match!", "Error");
                    return;
                }
                if(!securityAnswerField.getText().equals(securityAnswerConfirmField.getText())) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Security answers does not match!", "Error");
                    return;
                }
                var req = new JsonObject();
                req.addProperty("username", usernameField.getText());
                req.addProperty("password", passwordField.getText());
                req.addProperty("nickname", nameField.getText());
                req.addProperty("email", emailField.getText());
                req.addProperty("gender", genderField.getText());
                req.addProperty("securityQuestion", securityQuestionField.getSelected());
                req.addProperty("securityAnswer", securityAnswerField.getText());

                var postResponse = HTTPUtil.post("http://localhost:8080/api/user/register", req);

                if (postResponse == null) {
                    UIPopupHelper popupHelper = new UIPopupHelper(stage, skin);
                    popupHelper.showDialog("Connection to server failed.", "Error");
                    return;
                }

                var response = HTTPUtil.deserializeHttpResponse(postResponse);

                if (response.getStatus() == 400) {
                    UIPopupHelper popupHelper = new UIPopupHelper(stage, skin);
                    popupHelper.showDialog(response.getMessage(), "Error");
                    return;
                }

                if (response.getStatus() == 200) {
                    usernameField.setText("");
                    passwordField.setText("");
                    passwordConfirmButton.setText("");
                    nameField.setText("");
                    emailField.setText("");
                    genderField.setText("");
                    securityQuestionField.setSelectedIndex(0);
                    securityAnswerField.setText("");
                    securityAnswerConfirmField.setText("");
                    var token = postResponse.getHeaders().get("Authorization").get(0);
                    ClientApp.token = token;
//
//                    if (stayLoggedInCheckBox.isChecked()) {
//                        System.out.println(System.getProperty("user.dir"));
//                        FileUtil.write("../core/src/main/java/com/client/env/env.prod", "TOKEN=" + ClientApp.token);
//                    }

                    try {
                        LinkedTreeMap map = (LinkedTreeMap) response.getBody();
                        Gson gson = new Gson();
                        String json = gson.toJson(map);
                        System.out.println(json);
                        ClientApp.loggedInUser = UserDecoder.decode(json);
                        gameMain.setScreen(new MainMenu(gameMain));
                    } catch (Exception e) {
                        e.printStackTrace();
                        UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                        uiPopupHelper.showDialog("A server error occurred.", "Error");
                    }
                    UIPopupHelper popupHelper = new UIPopupHelper(stage, skin);
                    popupHelper.showDialog(response.getMessage(), "Success");
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
        table.add(label).pad(10);
        table.row();
        table.add(usernameField).width(500).height(50).pad(10);
        table.row();
        table.add(passwordField).width(500).height(50).pad(10);
        table.row();
        table.add(passwordConfirmButton).width(500).height(50).pad(10);
        table.row();
        table.add(nameField).width(500).height(50).pad(10);
        table.row();
        table.add(emailField).width(500).height(50).pad(10);
        table.row();
        table.add(genderField).width(500).height(50).pad(10);
        table.row();
        table.add(securityQuestionLabel).height(50).pad(10);
        table.row();
        table.add(securityQuestionField).width(500).height(50).pad(10);
        table.row();
        table.add(securityAnswerField).width(500).height(50).pad(10);
        table.row();
        table.add(securityAnswerConfirmField).width(500).height(50).pad(10);
        table.row();
        table.add(confirmButton).width(400).height(50).pad(10);
        table.row();
        table.add(backButton).width(400).height(50).pad(10);

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
