package com.example.graphics;

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
import com.example.GameMain;
import com.example.models.enums.SecurityQuestion;

public class SignUpMenu implements Screen {
    private final GameMain gameMain;
    private Stage stage;
    private final Skin skin;
    private final Texture background;

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
