package com.leonid.game.domain.kiosk.state;

import com.leonid.game.config.Config;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.domain.kiosk.KioskDeadState;
import com.leonid.game.event.KioskDeadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalTime;

import static com.leonid.game.domain.kiosk.KioskStatus.WAITING;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class KioskWaitingState implements State<KioskContext> {

    @Autowired
    private Config config;
    @Autowired
    private ApplicationContext app;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private LocalTime waitUntil;

    public KioskWaitingState(KioskContext context) {
        context.setState(this);
        context.getMaster().setStatus(WAITING);
    }

    @PostConstruct
    private void initWaitUntil() {
        this.waitUntil = LocalTime.now().plusSeconds(config.getKioskWaitingTime());
    }

    @Override
    public void tic(KioskContext context) {
        if (LocalTime.now().isAfter(waitUntil)) {
            context.getMaster().levelDown();

            if (context.getMaster().getLevel() == 0) {
                app.getBean(KioskDeadState.class, context);
                eventPublisher.publishEvent(new KioskDeadEvent(this));
            }

            initWaitUntil();
        }
    }
}
