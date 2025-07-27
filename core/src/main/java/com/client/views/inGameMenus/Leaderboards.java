package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboards implements MyScreen {
    private Texture background;
    private Stage stage;
    private Skin skin;
    private final Label.LabelStyle labelStyle;
    private final TextButton.TextButtonStyle textButtonStyle;
    private final GameMain gameMain;
    private final FarmMenu farmMenu;

    private boolean refreshFlag;
    private ArrayList<Player> players;
    private final ArrayList<SortOption> sortOptions;
    private SortOption sortedByCurrently;

    private enum SortOption {
        SORT_BY_NAME("Sorted by Name"),
        SORT_BY_MONEY("Sorted by Money"),
        SORT_BY_LEVEL("Sorted by Level"),
        SORT_BY_QUESTS("Sorted by Quests");

        private final String name;

        SortOption(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public Leaderboards(GameMain gameMain, FarmMenu farmMenu) {
        this.gameMain = gameMain;
        this.farmMenu = farmMenu;

        this.background = AssetManager.getImage("profilebackground");
        this.skin = AssetManager.getSkin();

        this.labelStyle = new Label.LabelStyle();
        this.labelStyle.font = AssetManager.getStardewFont();
        this.labelStyle.fontColor = Color.GOLD;

        BitmapFont customFont = AssetManager.getStardewFont();

        TextButton.TextButtonStyle baseStyle = skin.get(TextButton.TextButtonStyle.class);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = baseStyle.up;
        textButtonStyle.down = baseStyle.down;
        textButtonStyle.checked = baseStyle.checked;
        textButtonStyle.disabled = baseStyle.disabled;
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = baseStyle.fontColor;
        textButtonStyle.pressedOffsetX = baseStyle.pressedOffsetX;
        textButtonStyle.pressedOffsetY = baseStyle.pressedOffsetY;

        this.players = new ArrayList<>();
        this.players.addAll(ClientApp.currentGameData.getPlayers());
        this.players.sort(Comparator.comparingInt((Player player) -> player.getMoney(ClientApp.currentGameData)).reversed());

        this.sortedByCurrently = SortOption.SORT_BY_NAME;

        this.sortOptions = new ArrayList<>();
        sortOptions.add(SortOption.SORT_BY_MONEY);
        sortOptions.add(SortOption.SORT_BY_LEVEL);
        sortOptions.add(SortOption.SORT_BY_QUESTS);
        sortOptions.add(SortOption.SORT_BY_NAME);

        this.refreshFlag = false;

        initializeStage();
    }

    private void iterateSortOptionsList() {
        SortOption currentSortOption = sortOptions.get(0);

        if (currentSortOption == SortOption.SORT_BY_NAME) {
            sortedByCurrently = SortOption.SORT_BY_NAME;
        } else if (currentSortOption == SortOption.SORT_BY_LEVEL) {
            sortedByCurrently = SortOption.SORT_BY_LEVEL;
        } else if (currentSortOption == SortOption.SORT_BY_MONEY) {
            sortedByCurrently = SortOption.SORT_BY_MONEY;
        } else if (currentSortOption == SortOption.SORT_BY_QUESTS) {
            sortedByCurrently = SortOption.SORT_BY_QUESTS;
        }

        Collections.rotate(sortOptions, 1);
    }

    private void updateLeaderboards() {

    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Table contentTable = new Table();
        contentTable.center();

        Label rankLabel = new Label("Rank", labelStyle);
        rankLabel.setFontScale(1.3f);
        rankLabel.setColor(Color.GOLD);
        contentTable.add(rankLabel).pad(10);

        Label usernameLabel = new Label("Username", labelStyle);
        usernameLabel.setFontScale(1.3f);
        usernameLabel.setColor(Color.GOLD);
        contentTable.add(usernameLabel).pad(10);

        Label moneyLabel = new Label("Money", labelStyle);
        moneyLabel.setFontScale(1.3f);
        moneyLabel.setColor(Color.GOLD);
        contentTable.add(moneyLabel).pad(10);

        Label maxSkillLevel = new Label("Max Skill Level", labelStyle);
        maxSkillLevel.setFontScale(1.3f);
        maxSkillLevel.setColor(Color.GOLD);
        contentTable.add(maxSkillLevel).pad(10);

        Label questsCompleted = new Label("Quests Completed", labelStyle);
        questsCompleted.setFontScale(1.3f);
        questsCompleted.setColor(Color.GOLD);
        contentTable.add(questsCompleted).pad(10);

        contentTable.row();

        int index = 1;

        for (Player player : this.players) {
            Color color = (player == ClientApp.currentPlayer) ? Color.RED : Color.GOLD;

            if (index <= 3 && color == Color.GOLD) {
                color = Color.MAGENTA;
            }

            Label rank = new Label(String.valueOf(index), labelStyle);
            rank.setFontScale(1.3f);
            rank.setColor(color);
            contentTable.add(rank).pad(10);

            Label username = new Label(player.getUser().getUsername(), labelStyle);
            username.setFontScale(1.3f);
            username.setColor(color);
            contentTable.add(username).pad(10);

            Label score = new Label(String.valueOf(player.getMoney(ClientApp.currentGameData)), labelStyle);
            score.setFontScale(1.3f);
            score.setColor(color);
            contentTable.add(score).pad(10);

            //TODO: Farming Skill Level :(
            Label kills = new Label("0", labelStyle);
            kills.setFontScale(1.3f);
            kills.setColor(color);
            contentTable.add(kills).pad(10);

            Label survivalTime = new Label("0", labelStyle);
            survivalTime.setFontScale(1.3f);
            survivalTime.setColor(color);
            contentTable.add(survivalTime).pad(10);
            contentTable.row();

            index++;
        }

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setScrollingDisabled(true, false);

        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(farmMenu);
                dispose();
            }
        });

        TextButton sortButton = new TextButton(sortOptions.get(0).toString(), textButtonStyle);
        sortButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                refreshFlag = true;

                if (sortOptions.get(0).equals(SortOption.SORT_BY_NAME)) {
                    players.sort(Comparator.comparing((Player player) -> player.getUser().getUsername()));
                } else if (sortOptions.get(0).equals(SortOption.SORT_BY_MONEY)) {
                    players.sort(Comparator.comparingInt((Player player) -> player.getMoney(ClientApp.currentGameData)).reversed());
                } else if (sortOptions.get(0).equals(SortOption.SORT_BY_LEVEL)) {
                    players.sort(Comparator.comparingInt((Player user) -> user.getFarmingSkill().getLevel().levelNumber).reversed());
                } else if (sortOptions.get(0).equals(SortOption.SORT_BY_QUESTS)) {
                }

                iterateSortOptionsList();
            }
        });

        Label label = new Label(sortedByCurrently.toString(), labelStyle);
        label.setFontScale(1.3f);
        label.setColor(Color.GREEN);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        mainTable.add(scrollPane).width(1200).height(700).row();
        mainTable.center();
        mainTable.add(backButton).height(50).pad(10).row();
        mainTable.add(sortButton).height(50).pad(10).row();
        mainTable.add(label).pad(10).row();

        stage.addActor(mainTable);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (refreshFlag) {
            stage.dispose();
            initializeStage();
            refreshFlag = false;
        }
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

    @Override
    public void socketMessage(String message) {

    }
}
