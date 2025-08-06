package com.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A helper for onscreen pop-ups.
 *
 * @author AminHg
 */
public class UIPopupHelper {

    private final Stage stage;
    private final Skin skin;

    public UIPopupHelper(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    public void showDialog(String message, String promptType) {

        Dialog dialog = new Dialog(promptType, skin) {
            @Override
            protected void result(Object object) {
            }
        };

        dialog.text(message);
        dialog.button("Confirm");

        dialog.show(stage);
    }

    public void showDialog(String message, String promptType, Runnable onConfirm) {

        Dialog dialog = new Dialog(promptType, skin) {
            @Override
            protected void result(Object object) {
                onConfirm.run();
            }
        };

        dialog.text(message);
        dialog.button("Confirm");

        dialog.show(stage);
    }

    public void showDialog(String message, String promptType, InputProcessor inputProcessor) {

        Dialog dialog = new Dialog(promptType, skin) {
            @Override
            protected void result(Object object) {
                Gdx.input.setInputProcessor(inputProcessor);
            }
        };

        dialog.text(message);
        dialog.button("Confirm");

        dialog.show(stage);
    }
}
