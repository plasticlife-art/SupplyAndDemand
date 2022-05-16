package com.leonid.game.domain.common;

/**
 * @author Leonid Cheremshantsev
 */
public interface Context<T extends HasPhysics> {

    T getMaster();

}
