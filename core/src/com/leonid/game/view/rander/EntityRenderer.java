package com.leonid.game.view.rander;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.leonid.game.EntityHolder;

/**
 * @author Leonid Cheremshantsev
 */
public abstract class EntityRenderer<T> implements Renderer<T> {

    protected ShapeRenderer renderer;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected EntityHolder holder;

    @Override
    public void init(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font, EntityHolder holder) {
        this.renderer = shapeRenderer;
        this.batch = batch;
        this.font = font;
        this.holder = holder;
    }
}
