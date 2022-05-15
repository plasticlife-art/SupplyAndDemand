package com.leonid.game.domain.common;

/**
 * @author Leonid Cheremshantsev
 */
public interface State<T> {

    void tic(T context);

}
