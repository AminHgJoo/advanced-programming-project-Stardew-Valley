package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.client.utils.UIPopupHelper;
import com.common.models.enums.types.QuestTypes;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestBoardMenu implements MyScreen {
    private final GameMain gameMain;
    private final FarmMenu farmMenu;
    private Stage stage;
    private Texture backgroundTexture;
    private final Skin skin;
    private final static ArrayList<QuestTypes> availableQuests = new ArrayList<>();

    static {
        availableQuests.addAll(Arrays.asList(QuestTypes.values()));
    }

    public QuestBoardMenu(GameMain gameMain, FarmMenu farmMenu) {
        this.gameMain = gameMain;
        this.farmMenu = farmMenu;

        skin = AssetManager.getSkin();

        backgroundTexture = AssetManager.getImage("profilebackground");

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Table scrollTable = new Table();
        scrollTable.setFillParent(true);

        for (QuestTypes questType : availableQuests) {
            Label questTypeLabel = new Label(questType.toString(), skin);

            scrollTable.add(questTypeLabel).pad(10);

            TextButton acceptQuestButton = new TextButton("Accept", skin);
            acceptQuestButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    availableQuests.remove(questType);
                    ClientApp.currentQuests.add(questType);

                    Gdx.app.postRunnable(() -> {
                        dispose();
                        initializeStage();
                        UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                        uiPopupHelper.showDialog("Quest Accepted", "Notification");
                    });
                }
            });
            scrollTable.add(acceptQuestButton).pad(10).row();
        }

        Table secondScrollTable = new Table();
        secondScrollTable.setFillParent(true);

        for (QuestTypes questType : ClientApp.currentQuests) {
            Label questTypeLabel = new Label(questType.toString(), skin);
            secondScrollTable.add(questTypeLabel).pad(10);

            TextButton turnInQuestButton = new TextButton("Turn In Quest", skin);
            turnInQuestButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (questType.attemptQuestFinish()) {
                        turnInQuest(questType);

                        ClientApp.currentQuests.remove(questType);
                        availableQuests.add(questType);
                        Gdx.app.postRunnable(() -> {
                           dispose();
                           initializeStage();

                           UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                           uiPopupHelper.showDialog("Quest Completed! Reward: " + questType.rewardDollars + "$", "Notification");
                        });
                    } else {
                        UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                        uiPopupHelper.showDialog("You don't have the required item in the quest.", "Error");
                    }
                }
            });
            secondScrollTable.add(turnInQuestButton).pad(10).row();
        }

        ScrollPane anotherScrollPane = new ScrollPane(secondScrollTable);
        anotherScrollPane.setFadeScrollBars(false);

        ScrollPane scrollPane = new ScrollPane(scrollTable);
        scrollPane.setFadeScrollBars(false);

        Table table = new Table();
        table.setFillParent(true);

        table.center();

        Label questsLabel = new Label("Quests", skin);
        table.add(questsLabel).pad(10).row();

        table.add(scrollPane).pad(10).row();

        Label currentQuestsLabel = new Label("Current Quests", skin);
        table.add(currentQuestsLabel).pad(10).row();

        table.add(anotherScrollPane).pad(10).row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                gameMain.setScreen(farmMenu);
            }
        });
        table.add(backButton).pad(10).row();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    private void turnInQuest(QuestTypes questType) {
        var req = new JsonObject();
        req.addProperty("command", "cheat add " + questType.rewardDollars + " dollars");

        var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id()
            + "/chatParseCheat", req);

        Response res = HTTPUtil.deserializeHttpResponse(postResponse);

        if (res.getStatus() == 200) {
            String type = res.getMessage();
            if (type.equals("player")) {
                String json = res.getBody().toString();
                farmMenu.getPlayerController().updatePlayerObject(json);
            } else {
                String json = res.getBody().toString();
                farmMenu.getPlayerController().updateGame(json);
            }
            System.out.println("Quest hulu boro too galoo");
        } else {
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, AssetManager.getSkin());
            uiPopupHelper.showDialog(res.getMessage(), "Error");
        }
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
