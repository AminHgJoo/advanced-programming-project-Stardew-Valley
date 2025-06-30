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

public class LoginMenu implements Screen {
    private final GameMain gameMain;
    private Stage stage;
    private final Skin skin;
    private final Texture background;

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
                //TODO : add server login request.
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
                //TODO: add server request
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
}
