package com.client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.UIPopupHelper;
import com.common.models.networking.Lobby;

import java.util.ArrayList;

public class GameLobbyMenu implements Screen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;
    private Skin skin2;

    /// Dev note: labels, every type of text label if changed needs updating, set this flag to true if that need happens.
    private boolean doesUINeedRefresh = false;

    private Lobby currLobby = null;

    //TODO: Add server notifs here!!!
    private final ArrayList<String> messagesFromServer = new ArrayList<>();
    //TODO: get this from the server.
    private final ArrayList<Lobby> visibleLobbies = new ArrayList<>();

    public GameLobbyMenu(GameMain gameMain, Lobby currLobby) {
        this.gameMain = gameMain;
        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getTextures().get("mainMenuBackground");
        this.currLobby = currLobby;
        skin2 = AssetManager.getSkin2();
        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        initializeMiddleMenu();

        initializeSlidingMenu();

        Gdx.input.setInputProcessor(stage);
    }

    private void initializeMiddleMenu() {
        Table table = new Table(skin);
        table.setFillParent(true);

        Label currentLobbyName = new Label("Current Lobby: " + giveLobbyName(), skin);
        currentLobbyName.setColor(Color.CYAN);
        currentLobbyName.setFontScale(2f);
        table.add(currentLobbyName).colspan(2).pad(10).row();

        Label currentLobbyId = new Label("Lobby ID: " + giveLobbyID(), skin);
        currentLobbyId.setColor(Color.CYAN);
        currentLobbyId.setFontScale(2f);
        table.add(currentLobbyId).colspan(2).pad(10).row();

        if(currLobby != null && currLobby.getOwnerUsername().equals(ClientApp.loggedInUser.getUsername())) {
            TextButton startGameButton = new TextButton("Start Game (Host)", skin);
            startGameButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //TODO:
                }
            });
            table.add(startGameButton).colspan(2).pad(10).row();
        }
        else{
            TextButton startGameButton = new TextButton("Start Game (Host)", skin2);
            table.add(startGameButton)
                .colspan(2)
                .pad(10)
                .width(150)
                .height(50)
                .row();
        }

        Label farmChoiceLabel = new Label("Farm Choice", skin);
        farmChoiceLabel.setColor(Color.CYAN);
        farmChoiceLabel.setFontScale(2f);
        table.add(farmChoiceLabel).colspan(2).pad(10).row();

        SelectBox<String> farmSelectBox = new SelectBox<>(skin);
        farmSelectBox.setItems("Farm 1", "Farm 2");
        farmSelectBox.setSelectedIndex(0);
        table.add(farmSelectBox).colspan(2).pad(10).row();

        if(currLobby != null) {
            TextButton readyUp = new TextButton("Confirm & Ready Up", skin);
            readyUp.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //TODO:
                }
            });
            table.add(readyUp).colspan(2).pad(10).row();
        }
        else{
            TextButton readyUp = new TextButton("Confirm & Ready Up", skin2);
            table.add(readyUp)
                .colspan(2)
                .pad(10)
                .width(300)
                .height(60)
                .row();
        }

        Label invisibleLobbyName = new Label("Search invisible lobbies", skin);
        invisibleLobbyName.setColor(Color.CYAN);
        invisibleLobbyName.setFontScale(2f);
        table.add(invisibleLobbyName).colspan(2).pad(10).row();

        TextField searchInvisibleLobby = new TextField("", skin);
        searchInvisibleLobby.setMessageText("Enter Lobby ID");
        table.add(searchInvisibleLobby).pad(10);

        TextField passwordInvisibleLobby = new TextField("", skin);
        passwordInvisibleLobby.setMessageText("Enter Password");
        table.add(passwordInvisibleLobby).pad(10).row();

        TextButton searchButton = new TextButton("Search", skin);
        searchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO: Search for invisible lobby.
            }
        });
        table.add(searchButton).colspan(2).pad(10).row();

        TextButton refreshButton = new TextButton("Refresh", skin);
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO:
            }
        });
        table.add(refreshButton).colspan(2).pad(10).row();


        Table root = new Table();
        root.setFillParent(true);
        root.setColor(Color.CYAN);
        root.top().right();
        stage.addActor(root);

        Table listTable = new Table(skin);
        listTable.align(Align.top);

        for (Lobby lobby : visibleLobbies) {
            Label label = new Label(lobby.toString(), skin);
            label.setColor(Color.CYAN);
            TextButton btn = new TextButton("Join", skin);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //TODO: Try to join this lobby.
                }
            });

            if(lobby.isPublic()) {
                TextField passwordField = new TextField("", skin2);
                passwordField.setMessageText("Password");

                listTable.add(label).pad(4).colspan(2).left().row();

                listTable.add(passwordField).width(100).pad(4);
            }
            else{
                TextField passwordField = new TextField("", skin);
                passwordField.setMessageText("Password");

                listTable.add(label).pad(4).colspan(2).left().row();

                listTable.add(passwordField).width(100).pad(4);
            }
            listTable.add(btn).width(100).pad(4).row();
        }

        ScrollPane scrollPane = new ScrollPane(listTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        root.add(scrollPane)
            .width(500)
            .height(stage.getViewport().getWorldHeight())
            .pad(16);

        stage.addActor(table);
    }

    private void initializeSlidingMenu() {
        Table slidingMenu = new Table(skin);
        if(currLobby != null) {
            Label asghar = new Label("Players in lobby:", skin);
            asghar.setColor(Color.DARK_GRAY);
            slidingMenu.add(asghar).pad(10).row();
            for(String use : currLobby.getUsers()){
                Label label = new Label(use, skin);
                label.setColor(Color.DARK_GRAY);
                slidingMenu.add(label).pad(10).row();
            }
        }
        slidingMenu.pad(10);

        Label label = new Label("Lobby Options", skin);
        label.setColor(Color.DARK_GRAY);
        slidingMenu.add(label).pad(10).row();


        TextButton createLobby = new TextButton("Create Lobby", skin);
        createLobby.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new CreateLobby(gameMain));
            }
        });
        slidingMenu.add(createLobby).pad(10).row();
        if(currLobby != null) {
            TextButton leaveLobby = new TextButton("Leave Lobby", skin);
            leaveLobby.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //TODO:
                }
            });
            slidingMenu.add(leaveLobby).pad(10).row();
        }
        else{
            TextButton leaveLobby = new TextButton("Leave Lobby", skin2);
            slidingMenu.add(leaveLobby)
                .colspan(2)
                .pad(10)
                .width(150)
                .height(50)
                .row();
        }

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new MainMenu(gameMain));
                dispose();
            }
        });
        slidingMenu.add(mainMenuButton).pad(10).row();

        if(currLobby != null && currLobby.getOwnerUsername().equals(ClientApp.loggedInUser.getUsername())) {
            Label hostOptions = new Label("Host Options", skin);
            hostOptions.setColor(Color.DARK_GRAY);
            slidingMenu.add(hostOptions).pad(10).row();

            CheckBox isPrivateLobby = new CheckBox("Private Lobby", skin);
            isPrivateLobby.getLabel().setColor(Color.DARK_GRAY);
            isPrivateLobby.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //TODO:
                }
            });
            isPrivateLobby.setChecked(false);
            slidingMenu.add(isPrivateLobby).pad(10).row();

            CheckBox isInvisibleLobby = new CheckBox("Invisible Lobby", skin);
            isInvisibleLobby.getLabel().setColor(Color.DARK_GRAY);
            isInvisibleLobby.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //TODO:
                }
            });
            isInvisibleLobby.setChecked(false);
            slidingMenu.add(isInvisibleLobby).pad(10).row();
        }

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CYAN);
        pixmap.fill();

        Texture cyanTex = new Texture(pixmap);
        pixmap.dispose();
        skin.add("cyanBg", cyanTex);

        slidingMenu.setBackground(skin.getDrawable("cyanBg"));

        //Not hard coded, can be changed if needed.
        float menuW = 250, menuH = stage.getViewport().getWorldHeight();
        slidingMenu.setSize(menuW, menuH);

        // start off‐screen to the left
        slidingMenu.setPosition(-menuW, (stage.getViewport().getWorldHeight() - menuH) / 2);
        stage.addActor(slidingMenu);

        TextButton toggle = new TextButton("Menu", skin);
        toggle.setPosition(10, 10);
        stage.addActor(toggle);

        // Do not change this, because of anon-class quirks.
        final boolean[] isOpen = {false};
        toggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isOpen[0]) {
                    // slide out
                    slidingMenu.addAction(Actions.moveTo(-menuW, slidingMenu.getY(), 0.4f, Interpolation.fade));
                } else {
                    // slide in
                    slidingMenu.addAction(Actions.moveTo(0, slidingMenu.getY(), 0.4f, Interpolation.swingOut));
                }
                isOpen[0] = !isOpen[0];
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (doesUINeedRefresh) {
            stage.dispose();
            initializeStage();
            doesUINeedRefresh = false;
        }

        if (!messagesFromServer.isEmpty()) {
            String message = messagesFromServer.remove(0);
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
            uiPopupHelper.showDialog(message, "Message");
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    private String giveLobbyName() {
        if (currLobby == null) {
            return "Not in lobby";
        }

        return currLobby.getName();
    }

    private String giveLobbyID() {
        if (currLobby == null) {
            return "Not in lobby";
        }
        return currLobby.get_id();
    }
}
