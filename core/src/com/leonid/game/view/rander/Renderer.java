package com.leonid.game.view.rander;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.leonid.game.EntityHolder;
import com.leonid.game.domain.common.HasPhysics;

/**
 * @author Leonid Cheremshantsev
 */
public interface Renderer<T> {

    void render(T context);

    <T2 extends HasPhysics> boolean supportMasterClass(Class<T2> tClass);

    void init(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font, EntityHolder holder);
}
