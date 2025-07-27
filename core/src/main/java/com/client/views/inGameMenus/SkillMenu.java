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
import com.common.models.Player;
import com.common.models.skills.Skill;

import java.util.ArrayList;

public class SkillMenu implements MyScreen, InputProcessor {
    private final Skin skin;
    private GameMain gameMain;
    private MyScreen farmScreen;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private Player player;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private ArrayList<Skill> skills;
    private Texture skillTexture;
    private Texture levelTexture;
    private int farmingLevel;
    private int miningLevel;
    private int foragingLevel;
    private int fishingLevel;
    private Label tooltipLabel;

    public SkillMenu(GameMain gameMain, MyScreen farmScreen) {
        this.gameMain = gameMain;
        this.farmScreen = farmScreen;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getImage("profileBackground");
        this.skin = AssetManager.getSkin();
        stage = new Stage(new ScreenViewport());
        this.player = ClientApp.currentPlayer;
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);
        this.skillTexture = AssetManager.getImage("skill");
        Image skillImage = new Image(skillTexture);
        float centerX = (Gdx.graphics.getWidth() - skillTexture.getWidth()) / 2f;
        float centerY = (Gdx.graphics.getHeight() - skillTexture.getHeight()) / 2f;
        skillImage.setPosition(centerX, centerY);
        stage.addActor(skillImage);

        tooltipLabel = new Label("", skin);
        tooltipLabel.setColor(Color.YELLOW);
        tooltipLabel.setFontScale(2f);
        tooltipLabel.setVisible(false);
        stage.addActor(tooltipLabel);

        titleFont = AssetManager.getStardewFont();
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.WHITE);
        this.layout = new GlyphLayout();
        this.levelTexture = AssetManager.getImage("green");
        this.skills = player.getSkills();
        this.farmingLevel = skills.get(0).getLevel().levelNumber + 1;
        this.miningLevel = skills.get(1).getLevel().levelNumber + 1;
        this.foragingLevel = skills.get(2).getLevel().levelNumber + 1;
        this.fishingLevel = skills.get(3).getLevel().levelNumber + 1;
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            gameMain.setScreen(farmScreen);
            this.dispose();
        } else if (i == Input.Keys.RIGHT) {
            gameMain.setScreen(new SocialMenu(gameMain, farmScreen));
        } else if (i == Input.Keys.LEFT) {
            gameMain.setScreen(new InventoryMenu(gameMain, farmScreen));
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
    public boolean mouseMoved(int screenX, int screenY) {
        float invertedY = Gdx.graphics.getHeight() - screenY;
        float levelHeight = levelTexture.getHeight();
        float gapY = levelHeight / 2f + 121;
        float farmingX = 545;
        float farmingY = 140 + gapY * 3;
        float farmingWidth = 400;
        float farmingHeight = 150;

        if (screenX >= farmingX && screenX <= farmingX + farmingWidth &&
            invertedY >= farmingY && invertedY <= farmingY + farmingHeight) {
            tooltipLabel.setText("Farming is the skill associated with planting, growing, and harvesting crops on the farm.");
            tooltipLabel.setPosition(farmingX - 500, farmingY - 50);
            tooltipLabel.setVisible(true);
        } else if (screenX >= farmingX && screenX <= farmingX + farmingWidth &&
            invertedY >= farmingY - gapY && invertedY <= farmingY + farmingHeight - gapY) {
            tooltipLabel.setText("Mining Skill is increased by breaking rocks, panning, or by reading Mining Monthly.");
            tooltipLabel.setPosition(farmingX - 500, farmingY - gapY - 50);
            tooltipLabel.setVisible(true);
        } else if (screenX >= farmingX && screenX <= farmingX + farmingWidth &&
            invertedY >= farmingY - gapY * 2 && invertedY <= farmingY + farmingHeight - gapY * 2) {
            tooltipLabel.setText("Foraging is the skill associated with gathering wild resources found on the ground throughout Stardew Valley.");
            tooltipLabel.setPosition(farmingX - 500, farmingY - gapY * 2 - 50);
            tooltipLabel.setVisible(true);
        } else if (screenX >= farmingX && screenX <= farmingX + farmingWidth &&
            invertedY >= farmingY - gapY * 3 && invertedY <= farmingY + farmingHeight - gapY * 3) {
            tooltipLabel.setText("Fishing is a skill associated with catching fish with a fishing rod or by collecting items from crab pots.");
            tooltipLabel.setPosition(farmingX - 500, farmingY - gapY * 3 - 50);
            tooltipLabel.setVisible(true);
        } else {
            tooltipLabel.setVisible(false);
        }

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
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        String title = "Skills";
        layout.setText(titleFont, title);
        float xAsghar = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
        float yAsghar = Gdx.graphics.getHeight() - 70f;
        titleFont.draw(batch, layout, xAsghar, yAsghar);
        float levelWidth = levelTexture.getWidth();
        float levelHeight = levelTexture.getHeight();
        float gapX = levelWidth / 2f + 71;
        float gapY = levelHeight / 2f + 121;
        float startX = Gdx.graphics.getWidth() / 2f + 16;
        float startY = 120;

        for (int i = 0; i < fishingLevel; i++) {
            batch.draw(levelTexture, startX + i * gapX, startY);
        }
        for (int i = 0; i < foragingLevel; i++) {
            batch.draw(levelTexture, startX + i * gapX, startY + gapY);
        }
        for (int i = 0; i < miningLevel; i++) {
            batch.draw(levelTexture, startX + i * gapX, startY + gapY * 2);
        }
        for (int i = 0; i < farmingLevel; i++) {
            batch.draw(levelTexture, startX + i * gapX, startY + gapY * 3);
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
        stage.dispose();
        batch.dispose();
    }
}
