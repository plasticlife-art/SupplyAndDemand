package com.leonid.game.generator;

import com.leonid.game.domain.common.Position;
import org.springframework.stereotype.Component;

import java.util.Random;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class PositionGenerator {

    private final Random random = new Random();


    public Position generate(float w, float h) {
        float x;
        float y;

        do {
            x = random.nextInt(round(w) * 2) - round(w);
            y = random.nextInt(round(h) * 2) - round(h);
        } while (sqrt(x * x + y * y) > h);

        return new Position(x, y);
    }
}
