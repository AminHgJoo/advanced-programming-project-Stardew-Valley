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
import com.common.models.enums.SecurityQuestion;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

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
                //TODO: Send server request.
                JsonObject req = new JsonObject();
                req.addProperty("username", usernameField.getText());
                req.addProperty("password", passwordField.getText());
                req.addProperty("nickname", nameField.getText());
                req.addProperty("email", emailField.getText());
                req.addProperty("gender", genderField.getText());
                req.addProperty("securityQuestion", securityQuestionField.getSelected());
                req.addProperty("securityAnswer", securityAnswerField.getText());

                HttpResponse<JsonNode> postResponse = Unirest.post("http://localhost:8080/api/user/register")
                    .header("Content-Type", "application/json")
                    .body(req.toString())
                    .asJson();
                if (postResponse.getStatus() == 200) {
                    System.out.println(postResponse.getBody().toString());
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
