package com.leonid.game.generator;

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

    private final Random random = new Random();

    private final String[] names = new String[]{"Вася", "Петя", "Денис"};

    public Customer generate(float w, float h) {
        return new Customer(generateName(), positionGenerator.generate(2 * w, 2 * h));
    }


    public Customer generate(Home home) {
        Customer customer = new Customer(generateName(), Position.of(home));
        customer.setHome(home);
        return customer;
    }

    private String generateName() {
        return names[random.nextInt(names.length)];
    }
}
