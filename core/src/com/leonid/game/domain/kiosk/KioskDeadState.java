package com.leonid.game.domain.kiosk;

import com.leonid.game.domain.common.State;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.leonid.game.domain.kiosk.KioskStatus.DEAD;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class KioskDeadState implements State<KioskContext> {

    public KioskDeadState(KioskContext context) {
        context.setState(this);
        context.getKiosk().setColor(RED);
        context.getKiosk().setStatus(DEAD);
    }

    @Override
    public void tic(KioskContext context) {

    }
}
