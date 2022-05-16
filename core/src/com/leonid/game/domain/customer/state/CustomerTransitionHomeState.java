package com.leonid.game.domain.customer.state;

import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.event.CustomerComeHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class CustomerTransitionHomeState extends CustomerTransitionState {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public CustomerTransitionHomeState(CustomerContext customerContext, HasPhysics goal) {
        super(customerContext, goal);
    }

    @Override
    public void tic(CustomerContext customerContext) {
        if (goal == null) return; //todo

        Customer customer = customerContext.getMaster();

        if (hasSamePosition(customer, goal)) {
            eventPublisher.publishEvent(new CustomerComeHome(this, customerContext.getMaster()));
            return;
        }

        move(customer, goal);
    }

    @Override
    protected Float getGoalX(HasPhysics entity) {
        return super.getGoalX(entity) + entity.getSize() / 2;
    }


    @Override
    protected Float getGoalY(HasPhysics entity) {
        return super.getGoalY(entity) + entity.getSize() / 2;
    }
}
