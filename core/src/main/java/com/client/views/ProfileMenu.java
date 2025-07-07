package com.client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.User;

public class ProfileMenu implements MyScreen {
    private final GameMain gameMain;
    private final Skin skin;
    private final Texture background;
    private Stage stage;
    private String nickname;
    private String username;
    private String email;
    private String password;
    private String gender;
    private User user;


    public ProfileMenu(GameMain gameMain) {
        this.gameMain = gameMain;
        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getTextures().get("profileBackground");
        user = ClientApp.loggedInUser;
        nickname = user.getNickname();
        username = user.getUsername();
        email = user.getEmail();
        gender = user.getGender();
        password = user.getHashedPassword();
        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Label label = new Label("Profile", skin);
        label.setColor(Color.RED);
        label.setFontScale(2f);


        Label usernameLabel = new Label("Username: " + username, skin);
        Label passwordLabel = new Label("Password: " + password, skin);
        Label nicknameLabel = new Label("Nickname: " + nickname, skin);
        Label emailLabel = new Label("Email: " + email, skin);
        Label genderLabel = new Label("Gender: " + gender, skin);


        TextButton usernameButton = new TextButton("Change Username", skin);
        usernameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangeUsernameMenu(gameMain));
                dispose();
            }
        });

        TextButton passwordButton = new TextButton("Change Password", skin);
        passwordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangePasswordMenu(gameMain));
                dispose();
            }
        });

        TextButton nicknameButton = new TextButton("Change Nickname", skin);
        nicknameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangeNicknameMenu(gameMain));
                dispose();
            }
        });

        TextButton emailButton = new TextButton("Change Email", skin);
        emailButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new ChangeEmailMenu(gameMain));
                dispose();
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMain.setScreen(new MainMenu(gameMain));
                dispose();
            }
        });


        Table detailsTable = new Table(skin);
        detailsTable.top().left().pad(20);
        detailsTable.add(new Label("Personal Info", skin, "title")).left().padBottom(20).row();
        detailsTable.add(usernameLabel).left().padBottom(10).row();
        detailsTable.add(passwordLabel).left().padBottom(10).row();
        detailsTable.add(nicknameLabel).left().padBottom(10).row();
        detailsTable.add(emailLabel).left().padBottom(10).row();
        detailsTable.add(genderLabel).left().padBottom(10).row();


        Table menuTable = new Table();
        menuTable.top().padTop(50);
        menuTable.add(label).pad(10).row();
        menuTable.add(usernameButton).width(500).height(60).pad(10).row();
        menuTable.add(passwordButton).width(500).height(60).pad(10).row();
        menuTable.add(nicknameButton).width(500).height(60).pad(10).row();
        menuTable.add(emailButton).width(500).height(60).pad(10).row();
        menuTable.add(backButton).width(500).height(60).pad(10).row();


        Table root = new Table();
        root.setFillParent(true);
        root.add(detailsTable).expand().left().top().padLeft(30);
        root.add(menuTable).expand().top(); // Center/Right side
        stage.addActor(root);

        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
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

    @Override
    public void socketMessage(String message) {

    }
}
