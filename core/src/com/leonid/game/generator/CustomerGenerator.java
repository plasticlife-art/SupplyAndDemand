package com.leonid.game.generator;

import com.leonid.game.domain.customer.Customer;
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
        return new Customer(names[random.nextInt(names.length)], positionGenerator.generate(2 * w, 2 * h));
    }
}
