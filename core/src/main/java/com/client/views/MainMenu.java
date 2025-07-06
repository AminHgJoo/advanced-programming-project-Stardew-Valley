package com.client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.FileUtil;

public class MainMenu implements Screen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;

    public MainMenu(GameMain gameMain) {
        this.gameMain = gameMain;
        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getTextures().get("mainMenuBackground");
        ClientApp.init();
        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Label label = new Label("Main Menu", skin, "title");
        label.setColor(Color.RED);
        label.setFontScale(2f);

        TextButton profileMenuButtonButton = new TextButton("Profile", skin);
        profileMenuButtonButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ProfileMenu(gameMain));
                dispose();
            }
        });

        TextButton createGameButton = new TextButton("Lobby Menu", skin);
        createGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new GameLobbyMenu(gameMain));
                dispose();
            }
        });

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        style.font = skin.getFont("font");
        style.fontColor = Color.WHITE;

        TextButton logoutButton = new TextButton("Logout", style);
        logoutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ClientApp.token = "";
                ClientApp.loggedInUser = null;
                FileUtil.write("../core/src/main/java/com/client/env/env.prod", "");
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
        table.add(profileMenuButtonButton).width(500).height(60).pad(10).row();
        table.add(createGameButton).width(500).height(60).pad(10).row();
        table.add(logoutButton).width(500).height(60).pad(10).row();
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
