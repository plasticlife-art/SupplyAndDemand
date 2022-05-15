package com.leonid.game.domain.kiosk;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.event.CustomerProcessedEvent;
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

    public static final long NANOS_PER_SECOND = 1000_000_000L;
    @Autowired
    private ApplicationContext app;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private Config config;

    private final KioskContext context;
    private final LocalTime startTime;
    private final CustomerContext processingCustomer;

    public KioskProcessingState(KioskContext context) {
        this.startTime = LocalTime.now();
        this.context = context;
        context.setState(this);
        context.getKiosk().setStatus(PROCESSING);
        context.getKiosk().setColor(Color.DARK_GRAY);
        processingCustomer = context.getProcessingCustomer();
    }

    @Override
    public void tic(KioskContext context) {
        if (inProcessingFor(getProcessingTime(context))) {
            CustomerContext customerContext = context.processCustomer();

            if (customerContext == null) return; //todo

            eventPublisher.publishEvent(new CustomerProcessedEvent(this, customerContext.getCustomer()));

            CustomerContext nextCustomer = context.getProcessingCustomer();
            if (nextCustomer != null) {
                app.getBean(KioskProcessingState.class, context);
            } else {
                app.getBean(KioskWaitingState.class, context);
            }
        }

        if (context.getCustomersCount() >= 50) {
            app.getBean(KioskDeadState.class, context);
            eventPublisher.publishEvent(new KioskDeadEvent(this));
        }
    }

    private long getProcessingTime(KioskContext context) {
        return NANOS_PER_SECOND * config.getProcessingSeconds() / context.getKiosk().getLevel();
    }

    private boolean inProcessingFor(long nanos) {
        return LocalTime.now().minusNanos(nanos).isAfter(startTime);
    }
}
