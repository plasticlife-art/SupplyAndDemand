package com.leonid.game.domain.customer;

import com.leonid.game.domain.common.Context;
import com.leonid.game.domain.common.State;

/**
 * @author Leonid Cheremshantsev
 */
public class CustomerContext implements Context<Customer> {

    private final Customer customer;
    private State<CustomerContext> state;

    public CustomerContext(Customer customer) {
        this.customer = customer;
    }

    public void setCustomerState(State state) {
        this.state = state;
    }

    public void tic() {
        state.tic(this);
    }

    @Override
    public Customer getMaster() {
        return customer;
    }
}
