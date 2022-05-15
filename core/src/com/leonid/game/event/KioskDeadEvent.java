package com.leonid.game.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Leonid Cheremshantsev
 */
public class KioskDeadEvent extends ApplicationEvent {

    public KioskDeadEvent(Object source) {
        super(source);
    }
}
