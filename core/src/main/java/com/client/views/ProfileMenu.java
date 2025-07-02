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

public class ProfileMenu implements Screen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;

    public ProfileMenu(GameMain gameMain) {
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

        Label label = new Label("Profile", skin);
        label.setColor(Color.RED);
        label.setFontScale(2f);

        TextButton usernameButton = new TextButton("Change Username", skin);
        usernameButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangeUsernameMenu(gameMain));
                dispose();
            }
        });

        TextButton passwordButton = new TextButton("Change Password", skin);
        passwordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangePasswordMenu(gameMain));
                dispose();
            }
        });

        TextButton nicknameButton = new TextButton("Change Nickname", skin);
        nicknameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangeNicknameMenu(gameMain));
                dispose();
            }
        });

        TextButton emailButton = new TextButton("Change Email", skin);
        emailButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangeEmailMenu(gameMain));
                dispose();
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new MainMenu(gameMain));
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.center();
        table.padTop(50);
        table.add(label).pad(10).row();
        table.add(usernameButton).width(500).height(60).pad(10).row();
        table.add(passwordButton).width(500).height(60).pad(10).row();
        table.add(nicknameButton).width(500).height(60).pad(10).row();
        table.add(emailButton).width(500).height(60).pad(10).row();
        table.add(backButton).width(500).height(60).pad(10).row();
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
