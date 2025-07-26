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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Friendship;
import com.common.models.NPCModels.NPCFriendship;
import com.common.models.Player;

import java.util.ArrayList;

public class SocialMenu implements MyScreen, InputProcessor {
    private final Skin skin;
    private GameMain gameMain;
    private MyScreen farmScreen;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private Player player;
    private ArrayList<Friendship> friendships;
    private ArrayList<NPCFriendship> npcs;

    public SocialMenu(GameMain gameMain, MyScreen farmScreen) {
        this.gameMain = gameMain;
        this.farmScreen = farmScreen;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getImage("profileBackground");
        this.skin = AssetManager.getSkin();
        stage = new Stage(new ScreenViewport());
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);
        Label label = new Label("Social", skin);
        label.setColor(Color.RED);
        label.setFontScale(4f);
        stage.addActor(label);
        titleFont = AssetManager.getStardewFont();
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.WHITE);
        this.layout = new GlyphLayout();
        this.player = ClientApp.currentPlayer;
        npcs = player.getNpcs();
        friendships = player.getFriendships();
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            gameMain.setScreen(farmScreen);
            this.dispose();
        } else if (i == Input.Keys.RIGHT) {
            gameMain.setScreen(new MapMenu(gameMain, farmScreen));
        } else if (i == Input.Keys.LEFT) {
            gameMain.setScreen(new SkillMenu(gameMain, farmScreen));
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
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0);

        String title = "Social";
        layout.setText(titleFont, title);
        float xTitle = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
        float yTitle = Gdx.graphics.getHeight() - 50f;
        titleFont.draw(batch, layout, xTitle, yTitle);

        float xInfo = 100f;
        float yInfo = Gdx.graphics.getHeight() - 150f;
        BitmapFont font = AssetManager.getStardewFont();
        font.getData().setScale(1);
        font.setColor(Color.WHITE);

        font.draw(batch, "Player Friendships:", xInfo, yInfo);
        yInfo -= 40;

        for (Friendship f : friendships) {
            font.draw(batch, "Name: " + f.getPlayer() + " | Level: " + f.getLevel() + " | XP: " + f.getXp(), xInfo, yInfo);
            yInfo -= 30;
        }

        yInfo -= 40;
        font.draw(batch, "NPC Friendships:", xInfo, yInfo);
        yInfo -= 40;

        for (NPCFriendship npc : npcs) {
            font.draw(batch, "Name: " + npc.getNpc() + " | Level: " + npc.getLevel() + " | XP: " + npc.getXp(), xInfo, yInfo);
            yInfo -= 30;
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
