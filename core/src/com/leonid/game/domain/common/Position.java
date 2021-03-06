package com.leonid.game.domain.common;

/**
 * @author Leonid Cheremshantsev
 */
public class Position {

    private Float x;
    private Float y;

    public Position(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }


    public static Position of(Position position) {
        return new Position(position);
    }

    public static Position of(HasPhysics entity) {
        return new Position(entity.getPosition());
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
