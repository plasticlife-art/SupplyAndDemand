package com.leonid.game.view.rander.impl;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.view.rander.EntityRenderer;
import org.springframework.stereotype.Component;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class CustomerRenderer extends EntityRenderer<Customer> {

    private final Color[] colors = new Color[]{Color.valueOf("052600"), Color.valueOf("1D730F"), Color.valueOf("96CE8D")};

    @Override
    public void render(Customer customer) {
        renderer.setColor(getColor(customer));

        renderer.circle(customer.getPosition().getX(), customer.getPosition().getY(), customer.getSize());
    }

    private Color getColor(Customer customer) {
        return colors[Math.round(customer.getSpeedMultiplier()) - 1];
    }

    @Override
    public <T2 extends HasPhysics> boolean supportMasterClass(Class<T2> tClass) {
        return tClass.isAssignableFrom(Customer.class);
    }

}
