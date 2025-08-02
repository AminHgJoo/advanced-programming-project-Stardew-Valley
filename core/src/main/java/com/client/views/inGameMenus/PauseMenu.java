package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.HTTPUtil;
import com.client.views.preGameMenus.MainMenu;
import com.server.utilities.Response;

public class PauseMenu {
    private Stage stage;
    private Table table;
    private Skin skin;
    private boolean isVisible = false;
    private FarmMenu farmMenu;
    private GameMain gameMain;

    public PauseMenu(Skin skin, FarmMenu farmMenu, GameMain gameMain) {
        this.farmMenu = farmMenu;
        this.skin = skin;
        this.gameMain = gameMain;
        stage = new Stage();
        // Create the table that will hold our buttons
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.6f, 0.4f, 0.2f, 1)); // Brown wood-like color
        pixmap.fill();

        Texture woodTexture = new Texture(pixmap);
        pixmap.dispose();

        TextureRegionDrawable woodBackground = new TextureRegionDrawable(new TextureRegion(woodTexture));
        table = new Table();
        table.setBackground(woodBackground);

        table.setSize(600, 400);
        table.setPosition(Gdx.graphics.getWidth() / 2 - 150 * 2, Gdx.graphics.getHeight() / 2 - 100 * 2);

        Label title = new Label("Pause Menus", skin, "title");
        title.setFontScale(0.5f);
        table.add(title).padTop(20).padBottom(30).colspan(2).row();

        // Create buttons
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton voteButton = new TextButton("Vote", skin);
        TextButton leaveButton = new TextButton("Leave", skin);

        // Add listeners
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        voteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                gameMain.setScreen(new ChoosePlayerMenu(gameMain, farmMenu));
            }
        });

        leaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    var getResponse = HTTPUtil.get("/api/game/leave/" +
                        ClientApp.currentGameData.get_id());
                    Response res = HTTPUtil.deserializeHttpResponse(getResponse);
                    System.out.println(res.getMessage());
                    if (res.getStatus() == 200) {
                        System.out.println("hiiiiii");
                        ClientApp.currentPlayer = null;
                        ClientApp.currentGameData = null;
                        ClientApp.loggedInUser.setCurrentGameId(null);
                        farmMenu.getGameMain().setScreen(new MainMenu(farmMenu.getGameMain()));
                        farmMenu.dispose();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Add buttons to table
        table.add(resumeButton).padBottom(20).row();
        table.add(voteButton).padBottom(20).row();
        table.add(leaveButton);

        stage.addActor(table);
    }

    public void show() {
        isVisible = true;
        Gdx.input.setInputProcessor(stage); // Take over input processing
    }

    public void hide() {
        isVisible = false;
        // Return input processing to your main screen
    }

    public void render() {
        if (isVisible) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.setPosition(width / 2 - 150, height / 2 - 100);
    }

    public void togglePauseMenu() {
        if (isVisible) {
            hide();
        } else
            show();
    }

    public void dispose() {
        stage.dispose();
    }

    public boolean isVisible() {
        return isVisible;
    }
}
