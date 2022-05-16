package com.leonid.game.domain.home;

import com.leonid.game.domain.common.Context;
import com.leonid.game.domain.common.State;

/**
 * @author Leonid Cheremshantsev
 */
public class HomeContext implements Context<Home> {

    private final Home home;
    private State<HomeContext> state;

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
}
