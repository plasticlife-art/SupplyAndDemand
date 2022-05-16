package com.leonid.game.domain.kiosk.state;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.calc.GameCalculator;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.customer.state.CustomerTransitionHomeState;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.domain.kiosk.KioskDeadState;
import com.leonid.game.event.KioskDeadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static com.leonid.game.domain.kiosk.KioskStatus.PROCESSING;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KioskProcessingState implements State<KioskContext> {

    @Autowired
    private ApplicationContext app;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private Config config;
    @Autowired
    private GameCalculator calculator;

    private final LocalTime startTime;

    public KioskProcessingState(KioskContext context) {
        this.startTime = LocalTime.now();
        context.setState(this);
        context.getMaster().setStatus(PROCESSING);
        context.getMaster().setColor(Color.DARK_GRAY);
    }

    @Override
    public void tic(KioskContext context) {
        if (inProcessingFor(calculator.getKioskProcessingTime(context.getMaster()))) {
            CustomerContext customerContext = context.processCustomer();

            if (customerContext == null) return; //todo

            customerContext.setCustomerState(app.getBean(CustomerTransitionHomeState.class, customerContext, customerContext.getMaster().getHome()));

            CustomerContext nextCustomer = context.getProcessingCustomer();
            if (nextCustomer != null) {
                app.getBean(KioskProcessingState.class, context);
            } else {
                app.getBean(KioskWaitingState.class, context);
            }
        }

        if (isOverloaded(context)) {
            app.getBean(KioskDeadState.class, context);
            eventPublisher.publishEvent(new KioskDeadEvent(this));
        }
    }

    private boolean isOverloaded(KioskContext context) {
        return context.getCustomersCount() >= calculator.getKioskMaxQueue(context.getMaster());
    }

    private boolean inProcessingFor(long nanos) {
        return LocalTime.now().minusNanos(nanos).isAfter(startTime);
    }
}
