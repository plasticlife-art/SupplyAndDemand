package com.leonid.game.domain.customer;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.common.Position;
import com.leonid.game.domain.home.Home;
import com.leonid.game.domain.kiosk.Kiosk;

import java.util.Deque;
import java.util.LinkedList;

import static com.leonid.game.domain.customer.CustomerStatus.QUEUE;

/**
 * @author Leonid Cheremshantsev
 */
public class Customer implements HasPhysics {

    private String name;
    private Position position;
    private CustomerStatus status;
    private Home home;
    private final Deque<Kiosk> kiosksHistory;
    private float speedMultiplier;
    private Color color;
    private boolean visible;

    public Customer(String name, Position position, Color color) {
        this.name = name;
        this.position = position;
        this.color = color;

        kiosksHistory = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isVisible() {
        return status != QUEUE;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus customerStatus) {
        this.status = customerStatus;
    }

    public Kiosk getKiosk() {
        return kiosksHistory.peekLast();
    }

    public void setKiosk(Kiosk kiosk) {
        kiosksHistory.add(kiosk);
    }

    public Deque<Kiosk> getKiosksHistory() {
        return kiosksHistory;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
}
