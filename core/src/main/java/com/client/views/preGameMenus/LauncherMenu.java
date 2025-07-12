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
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.client.views.inGameMenus.FarmMenu;

public class LauncherMenu implements MyScreen {
    private final Skin skin;
    private final Texture background;
    private final GameMain gameMain;
    private Stage stage;

    public LauncherMenu(GameMain gameMain) {
        this.background = AssetManager.getTextures().get("launcherBackground");
        this.skin = AssetManager.getSkin();
        this.gameMain = gameMain;

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table table = new Table(skin);

        Label versionNumber = new Label("v1.0.0", skin);
        versionNumber.setFontScale(2f);
        versionNumber.setColor(Color.GOLD);
        versionNumber.setPosition(10, 10);
        stage.addActor(versionNumber);

        TextButton loginButton = new TextButton("Login", skin);
        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new FarmMenu(gameMain));
                dispose();
            }
        });

        TextButton registerButton = new TextButton("Register", skin);
        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new SignUpMenu(gameMain));
                dispose();
            }
        });

        table.padTop(50);
        table.add(loginButton).width(300).height(60).pad(10);
        table.pack();
        table.setPosition(stage.getWidth() / 2f - table.getWidth() / 2f, stage.getHeight() * 0.25f);
        table.row();
        table.add(registerButton).width(300).height(60).pad(10);

        stage.addActor(table);

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
