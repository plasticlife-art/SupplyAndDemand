package com.leonid.game.view.rander.impl;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.calc.Calculator;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.view.rander.EntityRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.graphics.Color.*;
import static com.leonid.game.domain.kiosk.KioskStatus.DEAD;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class KioskRenderer extends EntityRenderer<Kiosk> {

    @Autowired
    private Calculator calculator;

    @Override
    public void render(Kiosk kiosk) {

        Float x = kiosk.getPosition().getX();
        Float y = kiosk.getPosition().getY();
        int size = kiosk.getSize();

        batch.begin();

        drawKiosk(kiosk);

        KioskContext context = holder.getContext(kiosk);

        drawCustomerInQueueCount(context, x, y, size);
        drawProcessedCustomerCount(context, x, y, size);
        drawKioskLevel(context, x, y, size);

        batch.end();
    }

    @Override
    public <T2 extends HasPhysics> boolean supportMasterClass(Class<T2> tClass) {
        return tClass.isAssignableFrom(Kiosk.class);
    }

    private void drawKioskLevel(KioskContext kioskContext, Float x, Float y, int size) {
        font.setColor(BLACK);
        font.draw(batch, String.valueOf(kioskContext.getMaster().getLevel()), x + size, y + size);
    }

    private void drawProcessedCustomerCount(KioskContext kioskContext, Float x, Float y, int size) {
        font.setColor(GREEN);
        font.draw(batch, String.valueOf(kioskContext.getProcessedCustomers()), x + size, y - size);
    }

    private void drawCustomerInQueueCount(KioskContext kioskContext, Float x, Float y, int size) {
        font.setColor(Color.valueOf("FFD700"));
        font.draw(batch, kioskContext.getCustomersCount() + "/" + calculator.getKioskMaxQueue(kioskContext.getMaster()), x - size, y - size);
    }

    private void drawKiosk(Kiosk kiosk) {
        drawBuilding(kiosk);
        drawStatus(kiosk);
    }

    private void drawStatus(Kiosk kiosk) {
        Float x = kiosk.getPosition().getX();
        Float y = kiosk.getPosition().getY();

        Color color = YELLOW;

        switch (kiosk.getStatus()) {
            case WAITING:
                color = YELLOW;
                break;
            case PROCESSING:
                color = GREEN;
                break;
        }

        renderer.setColor(color);
        renderer.circle(x - kiosk.getSize() / 2f, y + kiosk.getSize() / 2f, kiosk.getSize() / 7f);
    }

    private void drawBuilding(Kiosk kiosk) {
        Float x = kiosk.getPosition().getX();
        Float y = kiosk.getPosition().getY();

        Color color = DARK_GRAY;

        if (kiosk.getStatus() == DEAD) {
            color = RED;
        }

        renderer.setColor(color);
        renderer.circle(x, y, kiosk.getSize());
    }
}
