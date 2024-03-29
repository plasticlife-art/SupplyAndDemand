package com.leonid.game.domain.customer.state;

import com.leonid.game.EntityHolder;
import com.leonid.game.calc.Calculator;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.domain.kiosk.state.KioskProcessingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Random;

import static com.leonid.game.domain.customer.CustomerStatus.QUEUE;
import static com.leonid.game.domain.kiosk.KioskStatus.WAITING;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerQueueState implements State<CustomerContext> {

    @Autowired
    private ApplicationContext app;
    @Autowired
    private Config config;
    @Autowired
    private Calculator calculator;
    @Autowired
    private EntityHolder holder;

    private final Random random = new Random();

    private final LocalTime inQueue;

    public CustomerQueueState(CustomerContext customerContext) {
        this.inQueue = LocalTime.now();
        customerContext.getMaster().setStatus(QUEUE);
        customerContext.setCustomerState(this);
    }

    @Override
    public void tic(CustomerContext customerContext) {
        if (customerContext.getMaster() == null) {
            return;
        }

        KioskContext kioskContext = holder.getContext(customerContext.getMaster().getKiosk());
        if (kioskContext == null || kioskContext.getMaster() == null) {
            return;
        }

        if (customerContext.getMaster().getStatus() == QUEUE && !kioskContext.isInQueue(customerContext)) {
            kioskContext.putCustomer(customerContext);
        }

        CustomerContext processingCustomer = kioskContext.getProcessingCustomer();
        if (kioskContext.getMaster().getStatus() == WAITING && processingCustomer != null) {
            app.getBean(KioskProcessingState.class, kioskContext);
            app.getBean(CustomerProcessingState.class, processingCustomer);
        }
        if (shouldGo()) {
            goToAnotherKiosk(customerContext);
        }
    }

    private boolean shouldGo() {
        return isWaitingFor(config.getCustomerWaitingTime())
                && random.nextInt(100) < config.getCustomerToGoToAnotherKioskPercent();
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
        Kiosk kiosk = calculator.calcBestKioskExceptHistory(customer);

        if (kiosk.getId() == -1L && customer.getKiosk() != null) {
            return customer.getKiosk();
        }

        return kiosk;
    }

    private CustomerTransitionKioskState getTransitionState(CustomerContext customerContext) {
        return app.getBean(CustomerTransitionKioskState.class, customerContext, customerContext.getMaster().getKiosk());
    }
}
