package com.leonid.game.domain.home;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.common.Position;

/**
 * @author Leonid Cheremshantsev
 */
public class Home implements HasPhysics {
    private final Position position;

    public Home(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getSize() {
        return 25;
    }

    @Override
    public Color getColor() {
        return Color.BROWN;
    }
}
