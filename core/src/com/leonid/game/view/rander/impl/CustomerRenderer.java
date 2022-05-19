package com.leonid.game.view.rander.impl;

import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.view.rander.EntityRenderer;
import org.springframework.stereotype.Component;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class CustomerRenderer extends EntityRenderer<Customer> {

    @Override
    public void render(Customer customer) {
        renderer.setColor(customer.getColor());

        renderer.circle(customer.getPosition().getX(), customer.getPosition().getY(), customer.getSize());
    }

    @Override
    public <T2 extends HasPhysics> boolean supportMasterClass(Class<T2> tClass) {
        return tClass.isAssignableFrom(Customer.class);
    }

}
