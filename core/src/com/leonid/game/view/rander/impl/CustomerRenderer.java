package com.leonid.game.view.rander.impl;

import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.view.rander.EntityRenderer;
import org.springframework.stereotype.Component;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class CustomerRenderer extends EntityRenderer<CustomerContext> {
    @Override
    public void render(CustomerContext context) {
        Customer customer = context.getMaster();

        renderer.setColor(customer.getColor());
        renderer.circle(customer.getPosition().getX(), customer.getPosition().getY(), customer.getSize());
    }

    @Override
    public Class<CustomerContext> support() {
        return CustomerContext.class;
    }

}
