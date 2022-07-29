package com.leonid.game.generator;

import com.leonid.game.EntityHolder;
import com.leonid.game.Game;
import com.leonid.game.calc.Calculator;
import com.leonid.game.domain.common.Position;
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
    @Autowired
    private EntityHolder entityHolder;
    @Autowired
    private Calculator calculator;

    private final Random random = new Random();


    public Kiosk generate(float w, float h) {
        Kiosk newKiosk = null;

        while (newKiosk == null) {
            Kiosk generated = getNewKiosk(w, h);
            if (checkAround(generated, Game.WIDTH / 10)) {
                newKiosk = generated;
            }
        }

        return newKiosk;
    }

    private boolean checkAround(Kiosk generated, int radius) {
        return entityHolder.getEntities(Kiosk.class).stream().allMatch(kiosk -> calculator.getDistance(kiosk, generated) > radius);
    }

    private Kiosk getNewKiosk(float w, float h) {
        return new Kiosk(getId(), getPosition(w, h), getPrice());
    }

    private Price getPrice() {
        return new Price(100L);
    }

    private long getId() {
        return random.nextLong();
    }

    private Position getPosition(float w, float h) {
        return positionGenerator.generate(w, h);
    }


}
