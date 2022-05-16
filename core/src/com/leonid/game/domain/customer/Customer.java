package com.leonid.game.domain.customer;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.common.Position;
import com.leonid.game.domain.home.Home;
import com.leonid.game.domain.kiosk.Kiosk;

/**
 * @author Leonid Cheremshantsev
 */
public class Customer implements HasPhysics {

    private String name;
    private Position position;
    private CustomerStatus customerStatus;
    private Home home;
    private Kiosk kiosk;

    public Customer(String name, Position position) {
        this.name = name;
        this.position = position;
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
        return Color.LIME;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Kiosk getKiosk() {
        return kiosk;
    }

    public void setKiosk(Kiosk kiosk) {
        this.kiosk = kiosk;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }
}
