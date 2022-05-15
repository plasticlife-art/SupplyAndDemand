package com.leonid.game.domain.customer.state;

import com.badlogic.gdx.math.Vector2;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.kiosk.Kiosk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.leonid.game.domain.customer.CustomerStatus.TRANSITION;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class CustomerTransitionState implements State<CustomerContext> {

    @Autowired
    private ApplicationContext app;

    public CustomerTransitionState(CustomerContext customerContext) {
        customerContext.getMaster().setCustomerStatus(TRANSITION);
    }

    @Override
    public void tic(CustomerContext customerContext) {
        if (customerContext.getMaster().getKiosk() == null) return;

        Customer customer = customerContext.getMaster();
        Kiosk kiosk = customer.getKiosk();

        if (hasSamePosition(customer, kiosk)) {
            customerContext.setCustomerState(app.getBean(CustomerQueueState.class, customerContext));
            return;
        }

        move(customer, kiosk);
    }

    private void move(Customer customer, Kiosk kiosk) {
        Vector2 direction = getDirection(customer, kiosk);

        Vector2 position = new Vector2(customer.getPosition().getX(), customer.getPosition().getY());
        position.sub(direction);

        customer.getPosition().setX(position.x);
        customer.getPosition().setY(position.y);
    }

    private boolean hasSamePosition(Customer customer, Kiosk kiosk) {

        return Math.abs(kiosk.getPosition().getX() - customer.getPosition().getX()) < 1
                && Math.abs(kiosk.getPosition().getY() - customer.getPosition().getY()) < 1;
    }

    private static Vector2 getDirection(Customer customer, Kiosk kiosk) {
        return getDirection(customer.getPosition().getX(),
                customer.getPosition().getY(),
                kiosk.getPosition().getX(),
                kiosk.getPosition().getY());
    }

    private static Vector2 getDirection(Float x, Float y, Float x1, Float y1) {
        Vector2 recalcVector = new Vector2(x, y);
        Vector2 planetVector = new Vector2(x1, y1);
        recalcVector.sub(planetVector).nor();
        return recalcVector;
    }

}
