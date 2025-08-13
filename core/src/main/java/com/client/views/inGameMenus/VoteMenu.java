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
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.client.utils.UIPopupHelper;
import com.client.views.preGameMenus.MainMenu;
import com.common.GameGSON;
import com.common.models.GameData;
import com.common.models.Player;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.HashMap;

public class VoteMenu implements MyScreen, InputProcessor {
    private final Skin skin;
    private GameMain gameMain;
    private MyScreen farmScreen;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private Player player;
    private boolean goToFarmMenu = false;
    private boolean goToMainMenu = false;

    public VoteMenu(GameMain gameMain, MyScreen farmScreen, Player player) {
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
        this.player = player;
        float buttonWidth = 200f;
        float buttonHeight = 80f;
        float spacing = 40f;
        float totalWidth = buttonWidth * 2 + spacing;
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        float yButtons = Gdx.graphics.getHeight() - 200f;
        TextButton yesButton = new TextButton("Yes", skin);
        yesButton.setSize(buttonWidth, buttonHeight);
        yesButton.setPosition(startX, yButtons);
        TextButton noButton = new TextButton("No", skin);
        noButton.setSize(buttonWidth, buttonHeight);
        noButton.setPosition(startX + buttonWidth + spacing, yButtons);

        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JsonObject req = new JsonObject();
                req.addProperty("playerId", player.getUser_id());
                req.addProperty("vote", true);
                var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/worldVotePlayer", req);
                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                if (res.getStatus() == 200) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Waiting for others to vote ... ", "Success");
                } else {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog(res.getMessage(), "Error");
                }
//                ((FarmMenu) farmScreen).voteFlag = false;
//                ((FarmMenu) farmScreen).votedPlayer = null;
//                gameMain.setScreen(farmScreen);
//                dispose();
            }
        });

        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JsonObject req = new JsonObject();
                req.addProperty("playerId", player.getUser_id());
                req.addProperty("vote", false);
                var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/worldVotePlayer", req);
                Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                if (res.getStatus() == 200) {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Waiting for others to vote ... ", "Success");
                } else {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog(res.getMessage(), "Error");
                }
                ((FarmMenu) farmScreen).voteFlag = false;
//                ((FarmMenu) farmScreen).votedPlayer = null;
//                gameMain.setScreen(farmScreen);
//                dispose();
            }
        });
        String voteText = "Do you want to kick " + player.getUser().getUsername() + " ?";
        Label voteLabel = new Label(voteText, skin);
        voteLabel.setColor(Color.YELLOW);
        voteLabel.setFontScale(2.5f);
        voteLabel.setAlignment(1);

        float labelX = Gdx.graphics.getWidth() / 2f - voteLabel.getPrefWidth() / 2f + 150;
        float labelY = Gdx.graphics.getHeight() - 100f;

        voteLabel.setPosition(labelX, labelY);
        stage.addActor(voteLabel);

        stage.addActor(yesButton);
        stage.addActor(noButton);

        Gdx.input.setInputProcessor(stage);
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
        HashMap<String, String> res = (HashMap<String, String>) GameGSON.gson.fromJson(message, HashMap.class);
        String type = res.get("type");
        if (type.equals("PLAYER_VOTED")) {
            String playerUsername = res.get("player_username");
            String votedPlayerUsername = res.get("player_username_vote");
            String vote = res.get("vote");
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
            uiPopupHelper.showDialog(votedPlayerUsername + " voted " + vote + " for kick " + playerUsername, "Success");
        } else if (type.equals("PLAYER_KICK_OUT")) {
            String gameJson = res.get("game");
            ClientApp.currentGameData = GameGSON.gson.fromJson(gameJson, GameData.class);
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
            uiPopupHelper.showDialog("Player has been kicked out", "Success");
            goToFarmMenu = true;
            System.out.println("KIKCEDDDD");
        } else if (type.equals("PLAYER_NOT_KICK_OUT")) {
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
            uiPopupHelper.showDialog("Player hasn't been kicked out", "Success");
            goToFarmMenu = true;
            System.out.println("NOT KICKEDDD");
        } else if (type.equals("KICK_OUT")) {
            ClientApp.currentPlayer = null;
            ClientApp.loggedInUser.getGames().remove(ClientApp.currentGameData.get_id());
            ClientApp.currentGameData = null;
            goToMainMenu = true;
            System.out.println("SIKTIR");
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float v) {
        if (goToMainMenu) {
            dispose();
            gameMain.setScreen(farmScreen);
            return;
        }
        if (goToMainMenu) {
            dispose();
            farmScreen.dispose();
            gameMain.setScreen(new MainMenu(gameMain));
            return;
        }
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
