package com.leonid.game.event;

import com.leonid.game.domain.customer.Customer;
import org.springframework.context.ApplicationEvent;

/**
 * @author Leonid Cheremshantsev
 */
public class CustomerComeHome extends ApplicationEvent {

    private final Customer customer;

    public CustomerComeHome(Object source, Customer customer) {
        super(source);
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
