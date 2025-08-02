package com.client.views.inGameMenus;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Player;

import java.util.ArrayList;

public class ChoosePlayerMenu implements MyScreen, InputProcessor {
    private final Skin skin;
    private GameMain gameMain;
    private MyScreen farmScreen;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private ArrayList<Player> players;

    public ChoosePlayerMenu(GameMain gameMain, MyScreen farmScreen) {
        this.gameMain = gameMain;
        this.farmScreen = farmScreen;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getImage("profileBackground");
        this.skin = AssetManager.getSkin();
        stage = new Stage(new ScreenViewport());
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);
        String voteText = "Choose a player to kick out";
        Label voteLabel = new Label(voteText, skin);
        voteLabel.setColor(Color.YELLOW);
        voteLabel.setFontScale(2.5f);
        voteLabel.setAlignment(1);

        float labelX = Gdx.graphics.getWidth() / 2f - voteLabel.getPrefWidth() / 2f + 150;
        float labelY = Gdx.graphics.getHeight() - 100f;

        voteLabel.setPosition(labelX, labelY);
        stage.addActor(voteLabel);
        titleFont = AssetManager.getStardewFont();
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.WHITE);
        this.layout = new GlyphLayout();
        this.players = ClientApp.currentGameData.getPlayers();

        float startY = Gdx.graphics.getHeight() - 200f;
        float spacing = 80f;

        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            String username = player.getUser().getUsername();

            TextButton button = new TextButton(username, skin);
            float buttonWidth = 300f;
            float buttonHeight = 60f;
            float xCenter = (Gdx.graphics.getWidth() - buttonWidth) / 2f;
            float yPos = startY - (i * spacing);

            button.setSize(buttonWidth, buttonHeight);
            button.setPosition(xCenter, yPos);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    siktir(player);
                }
            });

            stage.addActor(button);
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void siktir(Player player) {
        //TODO server : All players should be informed to immediately enter the vote menu
        ((FarmMenu) farmScreen).voteFlag = true;
        ((FarmMenu) farmScreen).votedPlayer = player;
        gameMain.setScreen(farmScreen);
        dispose();
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE || i == Input.Keys.F) {
            gameMain.setScreen(farmScreen);
            this.dispose();
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


    @Override
    public void resize(int i, int i1) {

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
        batch.dispose();
        stage.dispose();
    }
}
