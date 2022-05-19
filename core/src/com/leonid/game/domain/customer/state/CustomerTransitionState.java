package com.leonid.game.domain.customer.state;

import com.badlogic.gdx.math.Vector2;
import com.leonid.game.config.Config;
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

    private final CustomerContext context;
    @Autowired
    protected ApplicationContext app;
    @Autowired
    private Config config;

    protected HasPhysics goal;

    public CustomerTransitionState(CustomerContext context, HasPhysics goal) {
        this.context = context;
        this.context.setCustomerState(this);
        context.getMaster().setStatus(TRANSITION);

        this.goal = goal;
    }

    @Override
    public abstract void tic(CustomerContext customerContext);

    protected void move(Customer customer, HasPhysics entity) {
        Vector2 direction = getDirection(customer, entity);
        direction.scl(getSpeedMultiplier());

        Vector2 position = new Vector2(customer.getPosition().getX(), customer.getPosition().getY());
        position.sub(direction);

        customer.getPosition().setX(position.x);
        customer.getPosition().setY(position.y);
    }

    protected boolean hasSamePosition(HasPhysics e1, HasPhysics e2) {

        return Math.abs(getGoalX(e2) - e1.getPosition().getX()) < getSpeedMultiplier()
                && Math.abs(getGoalY(e2) - e1.getPosition().getY()) < getSpeedMultiplier();
    }

    private float getSpeedMultiplier() {
        return context.getMaster().getSpeedMultiplier();
    }

    private Vector2 getDirection(Customer customer, HasPhysics entity) {
        return getDirection(customer.getPosition().getX(),
                customer.getPosition().getY(),
                getGoalX(entity),
                getGoalY(entity));
    }

    protected Float getGoalY(HasPhysics entity) {
        return entity.getPosition().getY();
    }

    protected Float getGoalX(HasPhysics entity) {
        return entity.getPosition().getX();
    }

    private Vector2 getDirection(Float x, Float y, Float x1, Float y1) {
        Vector2 recalcVector = new Vector2(x, y);
        Vector2 planetVector = new Vector2(x1, y1);
        recalcVector.sub(planetVector).nor();
        return recalcVector;
    }

}
