package com.leonid.game.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.leonid.game.EntitiesHolder;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.view.rander.Renderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Leonid Cheremshantsev
 */
@Configuration
@Component
public class RenderController {

    @SuppressWarnings("rawtypes")
    @Autowired
    private List<Renderer> renderers;
    @Autowired
    private EntitiesHolder holder;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void render(HasPhysics entity) {
        for (Renderer renderer : renderers) {
            if (renderer.supportMasterClass(entity.getClass())) {
                renderer.init(shapeRenderer, batch, font, holder);
                renderer.render(entity);
            }
        }
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }
}
