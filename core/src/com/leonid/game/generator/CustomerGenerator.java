package com.leonid.game.generator;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.Position;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.home.Home;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Leonid Cheremshantsev
 */

@Component
public class CustomerGenerator {

    @Autowired
    private PositionGenerator positionGenerator;
    @Autowired
    private Config config;

    private final Random random = new Random();

    private final String[] names = new String[]{"Вася", "Петя", "Денис"};

    private final Color[] colors = new Color[]{Color.valueOf("052600"), Color.valueOf("1D730F"), Color.valueOf("96CE8D")};


    public Customer generate(Home home) {
        int speedMultiplier = generateSpeedMultiplier();
        Customer customer = new Customer(generateName(), getNewCustomerPosition(home), colors[random.nextInt(3)]);
        customer.setHome(home);
        customer.setSpeedMultiplier(speedMultiplier);
        return customer;
    }

    private int generateSpeedMultiplier() {
        int speedMultiplier = random.nextInt(config.getCustomerSpeedMultiplier()) + 1;

        speedMultiplier += getSpeedMultiplierSalt();

        return speedMultiplier;
    }

    private float getSpeedMultiplierSalt() {
        return (random.nextInt(11) - 5) * 0.1f;
    }

    private Position getNewCustomerPosition(Home home) {
        return new Position(home.getPosition().getX() + home.getSize() / 2, home.getPosition().getY() + home.getSize() / 2);
    }

    private String generateName() {
        return names[random.nextInt(names.length)];
    }
}
