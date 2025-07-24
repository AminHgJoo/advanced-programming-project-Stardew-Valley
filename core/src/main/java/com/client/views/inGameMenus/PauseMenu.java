package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.client.GameMain;

public class PauseMenu {
    private Window window;
    private TextButton resumeButton;
    private TextButton leaveButton;
    private boolean isVisible = false;
    private GameMain gameMain;
    // TODO idk why this shit is not working
    public PauseMenu(Skin skin, Stage stage, GameMain gameMain) {
        this.gameMain = gameMain;
        window = new Window("Paused", skin);
        window.setModal(true);

        resumeButton = new TextButton("Resume", skin);
        leaveButton = new TextButton("Leave", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        leaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Leave button clicked");
            }
        });

        window.add(resumeButton).pad(10).row();
        window.add(leaveButton).pad(10);

        window.pack();
        window.setSize(400, 300);
        window.setColor(Color.RED);
        window.setPosition(
            stage.getWidth() / 2 - window.getWidth() / 2,
            stage.getHeight() / 2 - window.getHeight() / 2
        );

        window.setVisible(false);
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            this.toggle();
        }
        stage.addActor(window);
    }

    public void show() {
        window.setVisible(true);
        isVisible = true;
    }

    public void hide() {
        window.setVisible(false);
        isVisible = false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void toggle() {
        if (isVisible) hide();
        else show();
    }
}
