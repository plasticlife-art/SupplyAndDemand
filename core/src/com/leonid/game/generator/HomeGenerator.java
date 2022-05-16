package com.leonid.game.generator;

import com.leonid.game.domain.home.Home;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class HomeGenerator {

    @Autowired
    private PositionGenerator positionGenerator;

    public Home generate(float w, float h) {
        return new Home(positionGenerator.generate(w, h));
    }

}
