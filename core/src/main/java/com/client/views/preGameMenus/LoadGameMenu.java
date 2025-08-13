package com.client.views.preGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.*;
import com.client.views.inGameMenus.FarmMenu;
import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.Player;
import com.server.utilities.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadGameMenu implements MyScreen {
    private static final Logger log = LoggerFactory.getLogger(LoadGameMenu.class);
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;
    private boolean GAME_START = false;

    public LoadGameMenu(GameMain gameMain) {
        this.gameMain = gameMain;
        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getImage("mainMenuBackground");
        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.setFillParent(true);
        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Label label = new Label("Load Game", skin, "title");
        label.setColor(Color.RED);
        label.setFontScale(2f);
        SelectBox<String> selectBox = new SelectBox<>(skin);
        Array<String> arr = new Array<>();
        for (String id : ClientApp.loggedInUser.getGames()) {
            if (id != null) {
                arr.add(id);
            }
        }
        selectBox.setItems(arr);
        TextButton button = new TextButton("Load", skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var getResponse = HTTPUtil.get("/api/game/loadGame/" + selectBox.getSelected());
                Response res = HTTPUtil.deserializeHttpResponse(getResponse);
                if (res.getStatus() == 200) {
                    System.out.println("Khoda Ro Shokr II");
                } else {
                }
            }
        });
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                gameMain.setScreen(new MainMenu(gameMain));
            }
        });

        table.add(label).row();
        table.add(selectBox).pad(10);
        table.row();
        table.add(button).pad(10);
        table.row();
        table.add(back).pad(10);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    public void showMessage(String message, String type) {
        UIPopupHelper popupHelper = new UIPopupHelper(stage, skin);
        popupHelper.showDialog(message, type);
    }

    @Override
    public void socketMessage(String message) {
        HashMap<String, String> res = (HashMap<String, String>) GameGSON.gson.fromJson(message, HashMap.class);
        String type = res.get("type");
        if (type.equals("PLAYER_ONLINE")) {
            if (!res.get("player_username").equals(ClientApp.currentPlayer.getUser().getUsername()))
                showMessage(res.get("player_username") + " joined the game", "Success");
        } else if (type.equals("GAME_START")) {
            String gameJson = res.get("game");
            GameData game = GameGSON.gson.fromJson(gameJson, GameData.class);
            ClientApp.currentGameData = game;
            for (Player p : game.getPlayers()) {
                if (p.getUser_id().equals(ClientApp.loggedInUser.get_id())) {
                    ClientApp.currentPlayer = p;
                }
            }
            GAME_START = true;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (GAME_START) {
            this.gameMain.setScreen(new FarmMenu(gameMain));
            this.dispose();
        }
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
