package com.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LightningHelper {
    public boolean flashing = false;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private float alpha = 0f;

    public void trigger() {
        alpha = 1f;
        flashing = true;
    }

    public void render(Camera camera, float delta) {
        if (!flashing) return;

        alpha -= delta; // fade out quickly
        if (alpha <= 0f) {
            flashing = false;
            return;
        }

        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 1f, 1f, alpha); // white with alpha
        shapeRenderer.rect(camera.position.x - camera.viewportWidth / 2,
            camera.position.y - camera.viewportHeight / 2,
            camera.viewportWidth,
            camera.viewportHeight);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
