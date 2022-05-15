package com.leonid.game.view.rander;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Leonid Cheremshantsev
 */
public interface Renderer<T> {

    void render(T context);

    Class<T> support();

    void init(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font);
}
