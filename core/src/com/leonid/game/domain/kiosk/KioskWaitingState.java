package com.leonid.game.domain.kiosk;

import com.leonid.game.domain.common.State;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.graphics.Color.YELLOW;
import static com.leonid.game.domain.kiosk.KioskStatus.WAITING;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class KioskWaitingState implements State<KioskContext> {

    public KioskWaitingState(KioskContext context) {
        context.setState(this);
        context.getKiosk().setColor(YELLOW);
        context.getKiosk().setStatus(WAITING);
    }

    @Override
    public void tic(KioskContext context) {

    }
}
