package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
import com.common.models.Friendship;
import com.common.models.NPCModels.NPCFriendship;
import com.common.models.Player;

import java.util.ArrayList;

public class InteractionsMenu implements MyScreen, InputProcessor {
    private final Skin skin;
    private GameMain gameMain;
    private FarmMenu farmScreen;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private Player player;
    private ArrayList<Friendship> friendships;
    private ArrayList<NPCFriendship> npcs;
    private float buttonWidth;

    public InteractionsMenu(GameMain gameMain, FarmMenu farmScreen) {
        this.gameMain = gameMain;
        this.farmScreen = farmScreen;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getImage("profileBackground");
        this.skin = AssetManager.getSkin();
        stage = new Stage(new ScreenViewport());
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);
        titleFont = AssetManager.getStardewFont();
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.WHITE);
        this.layout = new GlyphLayout();
        this.player = ClientApp.currentPlayer;
        npcs = player.getNpcs();
        friendships = player.getFriendships();
        buttonWidth = 300;
        float xButton = 100f;
        float yButtonStart = Gdx.graphics.getHeight() - 180f;
        float buttonHeight = 40f;
        float buttonSpacing = 30f;

        for (Friendship f : friendships) {
            TextButton giftButton = new TextButton("Gift", skin);
            giftButton.setSize(buttonWidth, buttonHeight);
            giftButton.setPosition(xButton, yButtonStart);


            String playerString = f.getPlayer();
            Player targetPlayer = ClientApp.currentPlayer;

            for (Player player1 : ClientApp.currentGameData.getPlayers()) {
                if (player1.getUser().getUsername().equals(playerString)) {
                    targetPlayer = player1;
                }
            }
            Player finalTargetPlayer = targetPlayer;
            giftButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gift(finalTargetPlayer);
                }
            });

            stage.addActor(giftButton);
            yButtonStart -= buttonSpacing;
        }

        TextButton historyButton = new TextButton("Gift History", skin);
        historyButton.setSize(200, 50);
        historyButton.setPosition(20f, Gdx.graphics.getHeight() - 70f);
        historyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMain.setScreen(new GiftHistoryMenu(gameMain, farmScreen));
                InteractionsMenu.this.dispose();
            }
        });
        stage.addActor(historyButton);

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage , this);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    private void gift(Player targetPlayer) {
        gameMain.setScreen(new GiftMenu(gameMain, farmScreen, targetPlayer));
        this.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
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

        stage.act(v);
        stage.draw();

        batch.begin();

        String title = "Social";
        layout.setText(titleFont, title);
        float xTitle = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
        float yTitle = Gdx.graphics.getHeight() - 50f;
        titleFont.draw(batch, layout, xTitle, yTitle);

        float xInfo = 500f;
        float yInfo = Gdx.graphics.getHeight() - 140f;
        BitmapFont font = AssetManager.getStardewFont();
        font.getData().setScale(1);
        font.setColor(Color.WHITE);

        font.draw(batch, "Player Friendships:", xInfo, yInfo);
        yInfo -= 40;

        for (Friendship f : friendships) {
            font.draw(batch, "Name: " + f.getPlayer() + " | Level: " + f.getLevel() + " | XP: " + f.getXp(), xInfo, yInfo);
            yInfo -= 70;
        }

        batch.end();
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
        titleFont.getData().setScale(1);
        batch.dispose();
        stage.dispose();
    }
}
