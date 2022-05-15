package com.leonid.game.generator;

import com.leonid.game.domain.common.Position;
import org.springframework.stereotype.Component;

import java.util.Random;

import static java.lang.Math.round;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class PositionGenerator {

    private final Random random = new Random();


    public Position generate(float w, float h) {
        return new Position((float) random.nextInt(round(w)), (float) random.nextInt(round(h)));
    }
}
