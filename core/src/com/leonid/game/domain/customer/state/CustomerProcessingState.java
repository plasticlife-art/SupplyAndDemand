package com.leonid.game.domain.customer.state;

import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.CustomerContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.leonid.game.domain.customer.CustomerStatus.PROCESSING;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class CustomerProcessingState implements State<CustomerContext> {

    public CustomerProcessingState(CustomerContext customerContext) {
        customerContext.getMaster().setStatus(PROCESSING);
        customerContext.setCustomerState(this);
    }

    @Override
    public void tic(CustomerContext context) {

    }
}
