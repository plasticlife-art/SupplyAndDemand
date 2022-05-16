package com.leonid.game.domain.kiosk;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.common.Position;

/**
 * @author Leonid Cheremshantsev
 */
public class Kiosk implements HasPhysics {

    private Long id;
    private Position position;
    private Price price;
    private Color color;
    private KioskStatus status;
    private int level;
    private int size = 25;

    public Kiosk(Long id, Position position, Price price) {
        this.id = id;
        this.position = position;
        this.price = price;
        this.color = Color.YELLOW;
        this.status = KioskStatus.WAITING;
        this.level = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setStatus(KioskStatus status) {
        this.status = status;
    }

    public KioskStatus getStatus() {
        return status;
    }

    public void levelUp() {
        if (level >= 10) {
            return;
        }

        level++;
        this.size *= 1.2;
    }

    public int getLevel() {
        return level;
    }
}
