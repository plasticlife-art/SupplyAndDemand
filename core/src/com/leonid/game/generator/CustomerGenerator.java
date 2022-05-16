package com.leonid.game.generator;

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

    public Customer generate(float w, float h) {
        return new Customer(generateName(), positionGenerator.generate(2 * w, 2 * h));
    }


    public Customer generate(Home home) {
        Customer customer = new Customer(generateName(), getNewCustomerPosition(home));
        customer.setHome(home);
        customer.setSpeedMultiplier(random.nextInt(config.getCustomerSpeedMultiplier()) + 1);
        return customer;
    }

    private Position getNewCustomerPosition(Home home) {
        return new Position(home.getPosition().getX() + home.getSize() / 2, home.getPosition().getY() + home.getSize() / 2);
    }

    private String generateName() {
        return names[random.nextInt(names.length)];
    }
}
