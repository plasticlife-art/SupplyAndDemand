package com.leonid.game.domain.customer.state;

import com.badlogic.gdx.math.Vector2;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static com.leonid.game.domain.customer.CustomerStatus.TRANSITION;

/**
 * @author Leonid Cheremshantsev
 */

public abstract class CustomerTransitionState implements State<CustomerContext> {

    @Autowired
    protected ApplicationContext app;

    protected HasPhysics goal;

    public CustomerTransitionState(CustomerContext customerContext, HasPhysics goal) {
        customerContext.setCustomerState(this);
        customerContext.getMaster().setCustomerStatus(TRANSITION);

        this.goal = goal;
    }

    @Override
    public abstract void tic(CustomerContext customerContext);

    protected void move(Customer customer, HasPhysics entity) {
        Vector2 direction = getDirection(customer, entity);

        Vector2 position = new Vector2(customer.getPosition().getX(), customer.getPosition().getY());
        position.sub(direction);

        customer.getPosition().setX(position.x);
        customer.getPosition().setY(position.y);
    }

    protected boolean hasSamePosition(Customer customer, HasPhysics entity) {

        return Math.abs(entity.getPosition().getX() - customer.getPosition().getX()) < 1
                && Math.abs(entity.getPosition().getY() - customer.getPosition().getY()) < 1;
    }

    private static Vector2 getDirection(Customer customer, HasPhysics entity) {
        return getDirection(customer.getPosition().getX(),
                customer.getPosition().getY(),
                entity.getPosition().getX(),
                entity.getPosition().getY());
    }

    private static Vector2 getDirection(Float x, Float y, Float x1, Float y1) {
        Vector2 recalcVector = new Vector2(x, y);
        Vector2 planetVector = new Vector2(x1, y1);
        recalcVector.sub(planetVector).nor();
        return recalcVector;
    }

}
