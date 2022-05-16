package com.leonid.game.view.rander.impl;

import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.home.Home;
import com.leonid.game.view.rander.EntityRenderer;
import org.springframework.stereotype.Component;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class HomeRenderer extends EntityRenderer<Home> {
    @Override
    public void render(Home home) {
        renderer.setColor(home.getColor());
        renderer.rect(home.getPosition().getX(), home.getPosition().getY(), home.getSize(), home.getSize());
    }

    @Override
    public <T2 extends HasPhysics> boolean supportMasterClass(Class<T2> tClass) {
        return tClass.isAssignableFrom(Home.class);
    }

}
