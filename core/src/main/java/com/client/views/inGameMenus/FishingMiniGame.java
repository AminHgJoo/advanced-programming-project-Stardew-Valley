package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.controllers.FishingGameController;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.FishType;

public class FishingMiniGame implements MyScreen, InputProcessor {
    private final float BASE_SPEED_FACTOR = 3;
    private final float MAX_PROGRESS = 10;
    private final float MIN_PROGRESS = 0;
    private final float MAX_BOBBER_POS = 325;
    private final float MIN_BOBBER_POS = 0;

    private boolean isGameOngoing = true;
    private boolean isVictorious;
    private Stage stage;
    private Texture backgroundTexture;

    private Image waterLane;

    private Rectangle fishHitbox;
    private Image fishImage;

    private float fishPosition = 0;
    private float fishVelocity = 0;
    private float fishAcceleration = 0;

    private Rectangle bobberHitbox;
    private Image bobberImage;
    private float bobberPosition = 0;
    private float bobberVelocity = 0;
    private float bobberAcceleration = 0;

    private float catchingProgress = 0.01f;
    private boolean isCatchPerfect = true;
    private ProgressBar catchingProgressBar;

    private final GameMain gameMain;
    private final FarmMenu farmMenu;

    private float BOBBER_BASE_X;
    public float BOBBER_BASE_Y;

    //TODO: TEST ONLY.
    private FishType caughtFishType = FishType.GLACIER_FISH;
    private int caughtFishQuantity = 2;
    private Quality caughtFishQuality = Quality.COPPER;
    private int xpGained = 5;

    private final Quality poleQuality;

    public FishingMiniGame(GameMain gameMain, FarmMenu farmMenu, boolean doesUserHaveSonar, Quality poleQuality) {
        this.gameMain = gameMain;
        this.farmMenu = farmMenu;
        this.poleQuality = poleQuality;

        getFishingResults();

        initializeStage();

        if (doesUserHaveSonar) {
            showFishType();
        }

        FishingGameController.determineFishMotionType(caughtFishType.isLegendary);
    }

    private void getFishingResults() {
        if (ClientApp.currentGameData == null) {
            return;
        }

        var query = FishingGameController.queryFishingResult(ClientApp.currentGameData, poleQuality);
        caughtFishQuality = (Quality) query.get("quality");
        caughtFishType = (FishType) query.get("type");
        caughtFishQuantity = (Integer) query.get("quantity");
        xpGained = (Integer) query.get("xp");
    }

    private void showFishType() {
        Image caughtFishImage = new Image(AssetManager.getImage(caughtFishType.getTextureName()));
        stage.addActor(caughtFishImage);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = AssetManager.getStardewFont();
        labelStyle.fontColor = Color.GOLD;

        Label label = new Label("Predicted Catch: ", labelStyle);
        stage.addActor(label);

        label.setPosition(stage.getWidth() / 2 - (caughtFishImage.getWidth() + 10 + label.getWidth()) / 2, waterLane.getY() - label.getHeight());

        caughtFishImage.setPosition(label.getX() + label.getWidth() + 10, waterLane.getY() - caughtFishImage.getHeight());
    }

    private void initializeStage() {

        stage = new Stage(new ScreenViewport());

        backgroundTexture = AssetManager.getImage("profilebackground");

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        waterLane = new Image(AssetManager.getImage("fishinggamewaterlane"));
        stage.addActor(waterLane);
        waterLane.setPosition(stage.getWidth() / 2 - waterLane.getWidth() / 2, stage.getHeight() / 2 - waterLane.getHeight() / 2);

        bobberImage = new Image(AssetManager.getImage("fishinggamesafezone"));
        stage.addActor(bobberImage);

        BOBBER_BASE_X = stage.getWidth() / 2 - waterLane.getWidth() / 2 + 85;
        BOBBER_BASE_Y = stage.getHeight() / 2 - waterLane.getHeight() / 2 + 20;
        bobberImage.setPosition(BOBBER_BASE_X, BOBBER_BASE_Y);

        bobberHitbox = new Rectangle(BOBBER_BASE_X, BOBBER_BASE_Y, bobberImage.getWidth(), bobberImage.getHeight());

        fishImage = new Image(caughtFishType.isLegendary ?
            AssetManager.getImage("fishinggamelegendaryfishicon")
            : AssetManager.getImage("fishinggamefishicon"));

        stage.addActor(fishImage);
        fishImage.setPosition(BOBBER_BASE_X, BOBBER_BASE_Y);

        fishHitbox = new Rectangle(BOBBER_BASE_X, BOBBER_BASE_Y, fishImage.getWidth(), fishImage.getHeight());

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.GOLD;
        labelStyle.font = AssetManager.getStardewFont();

        Label label = new Label("Use the up/down arrow keys to reel the fish!", labelStyle);
        stage.addActor(label);
        label.setPosition(stage.getWidth() / 2 - label.getWidth() / 2, stage.getHeight() / 2 + waterLane.getHeight() / 2);

        catchingProgressBar = new ProgressBar(MIN_PROGRESS, MAX_PROGRESS, 0.01f, true, AssetManager.getPixthulhu());
        stage.addActor(catchingProgressBar);
        catchingProgressBar.setPosition(waterLane.getX() + waterLane.getWidth(), waterLane.getY());
        catchingProgressBar.setHeight(waterLane.getHeight());
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
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
    public void render(float delta) {
        if (isGameOngoing) {
            handlePlayerInput();
            handleBobberKinematics(delta);
            FishingGameController.handleFishAI(this, delta);
            incrementProgress(delta);
            checkForWinOrLoss();
        }

        stage.act(delta);
        stage.draw();
    }

    private void checkForWinOrLoss() {
        if (catchingProgress == MAX_PROGRESS) {
            isGameOngoing = false;
            isVictorious = true;
        } else if (catchingProgress == MIN_PROGRESS) {
            isGameOngoing = false;
            isVictorious = false;
        }

        if (!isGameOngoing) {
            stage.dispose();

            if (isVictorious) {

                if (isCatchPerfect) {

                    if (caughtFishQuality.ordinal() >= 2) {
                        caughtFishQuality = Quality.values()[Math.min(caughtFishQuality.ordinal() + 1, Quality.values().length - 1)];
                    }

                    xpGained = (int) (xpGained * 2.4);
                }

                notifyServerOfVictory(xpGained, caughtFishType, caughtFishQuality, caughtFishQuantity);
            }
            constructEndStage();
        }
    }

    private void constructEndStage() {

        stage = new Stage(new ScreenViewport());
        Image image = new Image(backgroundTexture);
        stage.addActor(image);
        image.setFillParent(true);

        Table table = new Table();
        table.setFillParent(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.GOLD;
        labelStyle.font = AssetManager.getStardewFont();

        Label label = new Label((isVictorious ? "You Won!" : "You Lost!")
            + (isVictorious ? " XP Gained: " + xpGained
            + " Caught x(" + caughtFishQuantity + ") "
            + caughtFishType.name + " of "
            + caughtFishQuality.toString() + " quality." : ""), labelStyle);

        Label label1 = new Label(isCatchPerfect ? "Perfect Catch!" : "Normal Catch", labelStyle);

        Skin skin = AssetManager.getSkin();
        BitmapFont customFont = AssetManager.getStardewFont();

        TextButton.TextButtonStyle baseStyle = skin.get(TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle();
        customStyle.up = baseStyle.up;
        customStyle.down = baseStyle.down;
        customStyle.checked = baseStyle.checked;
        customStyle.disabled = baseStyle.disabled;
        customStyle.font = customFont;
        customStyle.fontColor = baseStyle.fontColor;
        customStyle.pressedOffsetX = baseStyle.pressedOffsetX;
        customStyle.pressedOffsetY = baseStyle.pressedOffsetY;

        TextButton backButton = new TextButton("Exit", customStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                gameMain.setScreen(farmMenu);
            }
        });

        table.center();
        table.add(label);
        if (isVictorious) {
            table.row();
            table.add(label1);
        }
        table.row();
        table.add(backButton);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    //TODO:
    private void notifyServerOfVictory(int xpGained, FishType caughtFishType
        , Quality caughtFishQuality, int caughtFishQuantity) {

    }

    private void incrementProgress(float delta) {
        if (fishHitbox.overlaps(bobberHitbox)) {
            catchingProgress += delta;
        } else {
            catchingProgress -= delta * 0.5f;
            isCatchPerfect = false;
        }
        catchingProgress = MathUtils.clamp(catchingProgress, MIN_PROGRESS, MAX_PROGRESS);
        catchingProgressBar.setValue(catchingProgress);
    }

    private void handlePlayerInput() {
        final float ALPHA = 10;
        final float BETA = 0.15f;

        // dv/dt = α - β * v;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bobberAcceleration = ALPHA - BETA * bobberVelocity;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bobberAcceleration = -ALPHA - BETA * bobberVelocity;
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            //TODO: Exit mini-game.
            dispose();
            gameMain.setScreen(farmMenu);
        } else {
            bobberAcceleration = -BETA * bobberVelocity;
        }
    }

    private void handleBobberKinematics(float delta) {
        // a = dv/dt , v = dx/dt;
        final float DELTA_X = 0.5f;

        bobberPosition += delta * bobberVelocity * BASE_SPEED_FACTOR;
        bobberPosition = MathUtils.clamp(bobberPosition, MIN_BOBBER_POS, MAX_BOBBER_POS);

        if (bobberPosition == MAX_BOBBER_POS
            || bobberPosition == MIN_BOBBER_POS) {
            bobberVelocity = -bobberVelocity * 0.8f;

            if (bobberPosition == MIN_BOBBER_POS) {
                bobberPosition = DELTA_X + MIN_BOBBER_POS;
            } else {
                bobberPosition = -DELTA_X + MAX_BOBBER_POS;
            }
        }

        bobberVelocity += delta * bobberAcceleration * BASE_SPEED_FACTOR;

        bobberImage.setPosition(BOBBER_BASE_X, BOBBER_BASE_Y + bobberPosition);
        bobberHitbox.setPosition(BOBBER_BASE_X, BOBBER_BASE_Y + bobberPosition);
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

    public float getFishAcceleration() {
        return fishAcceleration;
    }

    public void setFishAcceleration(float fishAcceleration) {
        this.fishAcceleration = fishAcceleration;
    }

    public void incrementFishAcceleration(float amount) {
        fishAcceleration += amount;
    }

    public float getFishVelocity() {
        return fishVelocity;
    }

    public void setFishVelocity(float fishVelocity) {
        this.fishVelocity = fishVelocity;
    }

    public void incrementFishVelocity(float amount) {
        fishVelocity += amount;
    }

    public float getFishPosition() {
        return fishPosition;
    }

    public void setFishPosition(float fishPosition) {
        this.fishPosition = fishPosition;
    }

    public void incrementFishPosition(float amount) {
        fishPosition += amount;
    }

    public Image getFishImage() {
        return fishImage;
    }

    public Rectangle getFishHitbox() {
        return fishHitbox;
    }
}
