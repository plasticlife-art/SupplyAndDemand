package com.leonid.game.domain.customer.state;

import com.leonid.game.calc.GameCalculator;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.kiosk.Kiosk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Random;

import static com.leonid.game.domain.customer.CustomerStatus.QUEUE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerQueueState implements State<CustomerContext> {

    @Autowired
    private ApplicationContext app;
    @Autowired
    private GameCalculator gameCalculator;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final Random random = new Random();

    private final LocalTime inQueue;

    public CustomerQueueState(CustomerContext customerContext) {
        this.inQueue = LocalTime.now();
        customerContext.getMaster().setCustomerStatus(QUEUE);
    }

    @Override
    public void tic(CustomerContext customerContext) {
        if (isWaitingFor(10) && random.nextBoolean()) {
            goToAnotherKiosk(customerContext);
        }
    }

    private void goToAnotherKiosk(CustomerContext customerContext) {
        Customer customer = customerContext.getMaster();

        customer.setKiosk(calcNewBestKiosk(customer));

        customerContext.setCustomerState(getTransitionState(customerContext));
    }

    private boolean isWaitingFor(int seconds) {
        return LocalTime.now().minusSeconds(seconds).isAfter(inQueue);
    }

    private Kiosk calcNewBestKiosk(Customer customer) {
        Kiosk kiosk = gameCalculator.calcBestKioskExcept(customer, customer.getKiosk());

        if (kiosk.getId() == -1L && customer.getKiosk() != null) {
            return customer.getKiosk();
        }

        return kiosk;
    }

    private CustomerTransitionKioskState getTransitionState(CustomerContext customerContext) {
        return app.getBean(CustomerTransitionKioskState.class, customerContext, customerContext.getMaster().getKiosk());
    }
}
