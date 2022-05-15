package com.leonid.game.generator;

import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class KioskGenerator {

    @Autowired
    private PositionGenerator positionGenerator;

    private final Random random = new Random();


    public Kiosk generate(float w, float h) {
        return new Kiosk(random.nextLong(), positionGenerator.generate(w, h), new Price(100L));
    }


}
