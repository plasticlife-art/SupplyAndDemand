package com.leonid.game.domain.common;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Leonid Cheremshantsev
 */
public interface HasPhysics {

    Position getPosition();

    int getSize();

    Color getColor();

    boolean isVisible();
}
