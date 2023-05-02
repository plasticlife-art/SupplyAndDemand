package com.leonid.game.domain.home;

import com.leonid.game.domain.common.Context;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.kiosk.Kiosk;

/**
 * @author Leonid Cheremshantsev
 */
public class HomeContext implements Context<Home> {

    private final Home home;
    private State<HomeContext> state;

    private Kiosk bestKiosk;

    public HomeContext(Home home) {
        this.home = home;
    }

    @Override
    public Home getMaster() {
        return home;
    }

    @Override
    public void tic() {
        state.tic(this);
    }

    public State<HomeContext> getState() {
        return state;
    }

    public void setState(State<HomeContext> state) {
        this.state = state;
    }

    public Kiosk getBestKiosk() {
        return bestKiosk;
    }

    public HomeContext setBestKiosk(Kiosk bestKiosk) {
        this.bestKiosk = bestKiosk;
        return this;
    }
}
