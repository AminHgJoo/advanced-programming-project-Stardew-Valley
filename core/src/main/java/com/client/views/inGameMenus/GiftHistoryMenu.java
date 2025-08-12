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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.common.models.Friendship;
import com.common.models.Gift;
import com.common.models.Player;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.common.GameGSON.gson;

public class GiftHistoryMenu implements MyScreen, InputProcessor {
    private final Skin skin;
    private GameMain gameMain;
    private FarmMenu farmScreen;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private Player player;
    private ArrayList<Gift> gifts;
    private ArrayList<Gift> receivedGifts = new ArrayList<>();
    private ArrayList<Gift> sentGifts = new ArrayList<>();
    private float buttonWidth;
    private TextField rateField;
    private TextButton exitButton;

    public GiftHistoryMenu(GameMain gameMain, FarmMenu farmScreen) {
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
        gifts = ClientApp.currentGameData.getGifts();
        for (Gift g : gifts) {
            if(g.getFrom().equals(player.getUser().getUsername())) {
                sentGifts.add(g);
            }
            else if(g.getTo().equals(player.getUser().getUsername())) {
                receivedGifts.add(g);
            }
        }


        float fieldWidth = 200f;
        float fieldHeight = 50f;
        float xField = 100f;
        float yField = Gdx.graphics.getHeight() - 180f;

        buttonWidth = 300;
        float xButton = xField + fieldWidth + 40;
        float yButtonStart = Gdx.graphics.getHeight() - 180f;
        float buttonHeight = 50f;
        float buttonSpacing = 30f;
        int index = 0;
        for (Gift g : receivedGifts) {
            TextButton rateButton = new TextButton("Rate", skin);
            rateButton.setSize(buttonWidth, buttonHeight);
            rateButton.setPosition(xButton, yButtonStart);
            rateField = new TextField("", skin);
            rateField.setMessageText("Enter count...");
            rateField.setTextFieldFilter(new TextField.TextFieldFilter() {
                @Override
                public boolean acceptChar(TextField textField, char c) {
                    return Character.isDigit(c);
                }
            });
            rateField.setSize(fieldWidth, fieldHeight);
            rateField.setPosition(xField, yField);
            stage.addActor(rateField);

            int finalIndex = index;
            rateButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int rate = Integer.parseInt(rateField.getText());
                    JsonObject req = new JsonObject();
                    req.addProperty("giftNumber", finalIndex);
                    req.addProperty("rate", rate);
                    var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/friendshipGiftRate", req);
                    Response res = HTTPUtil.deserializeHttpResponse(postResponse);
                    if (res.getStatus() == 200) {
                        String game = res.getBody().toString();
                        ((FarmMenu) farmScreen).getPlayerController().updateGame(game);
                    }
                }
            });

            exitButton = new TextButton("Exit", skin);
            exitButton.setSize(fieldWidth, fieldHeight);
            exitButton.setPosition(xField, yField - 2* fieldHeight - 20f);

            exitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameMain.setScreen(farmScreen);
                    GiftHistoryMenu.this.dispose();
                }
            });

            stage.addActor(rateButton);
            stage.addActor(exitButton);
            yButtonStart -= buttonSpacing;
            yField -= buttonSpacing;
            index++;
        }
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage , this);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }


    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            gameMain.setScreen(new InteractionsMenu(gameMain, farmScreen));
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
        HashMap<String, String> res = (HashMap<String, String>) gson.fromJson(message, HashMap.class);
        String type = res.get("type");

        if (type.equals("PLAYER_UPDATED")) {
            String id = res.get("player_user_id");
            if (!Objects.equals(id, ((FarmMenu)farmScreen).getPlayerController().getPlayer().getUser_id())) {
                String player = res.get("player");
                ((FarmMenu)farmScreen).getPlayerController().updateAnotherPlayerObject(player);
            }
        } else if (type.equals("GAME_UPDATED")) {
            String game = res.get("game");
            ((FarmMenu)farmScreen).getPlayerController().updateGame(game);
        }
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
        String title = "Gifts History";
        layout.setText(titleFont, title);
        float xTitle = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
        float yTitle = Gdx.graphics.getHeight() - 50f;
        titleFont.draw(batch, layout, xTitle, yTitle);

        float xInfo = 590f;
        float yInfo = Gdx.graphics.getHeight() - 160f;
        BitmapFont font = AssetManager.getStardewFont();
        font.getData().setScale(1);
        font.setColor(Color.WHITE);

        font.draw(batch, "Received Gifts:", xInfo, yInfo);
        yInfo -= 40;

        for (Gift gift : receivedGifts) {
            font.draw(batch, "From: " + gift.getFrom() + " | Item: " + gift.getItem().getName() + " | Rate: " + gift.getRate(), xInfo, yInfo);
            yInfo -= 30;
        }

        font.draw(batch, "Sent Gifts:", xInfo, yInfo);
        yInfo -= 40;
        for (Gift gift : sentGifts) {
            font.draw(batch, "From: " + gift.getFrom() + " | Item: " + gift.getItem().getName() + " | Rate: " + gift.getRate(), xInfo, yInfo);
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
