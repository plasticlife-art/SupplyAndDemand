package com.leonid.game.domain.customer.state;

import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class CustomerTransitionKioskState extends CustomerTransitionState {

    public CustomerTransitionKioskState(CustomerContext customerContext, HasPhysics goal) {
        super(customerContext, goal);
    }

    @Override
    public void tic(CustomerContext customerContext) {
        if (goal == null) return; //todo

        Customer customer = customerContext.getMaster();

        if (hasSamePosition(customer, goal)) {
            customerContext.setCustomerState(app.getBean(CustomerQueueState.class, customerContext));
            return;
        }

        move(customer, goal);
    }
}
