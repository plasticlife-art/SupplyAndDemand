package com.leonid.game.view.rander.impl;

import com.badlogic.gdx.graphics.Color;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.view.rander.EntityRenderer;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.GREEN;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class KioskRenderer extends EntityRenderer<KioskContext> {


    @Override
    public void render(KioskContext kioskContext) {
        Kiosk kiosk = kioskContext.getMaster();

        Float x = kiosk.getPosition().getX();
        Float y = kiosk.getPosition().getY();
        int size = kiosk.getSize();

        batch.begin();

        drawKiosk(kiosk);
        drawCustomerInQueueCount(kioskContext, x, y, size);
        drawProcessedCustomerCount(kioskContext, x, y, size);
        drawKioskLevel(kioskContext, x, y, size);

        batch.end();
    }

    @Override
    public Class<KioskContext> support() {
        return KioskContext.class;
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
        font.draw(batch, String.valueOf(kioskContext.getCustomersCount()), x - size, y - size);
    }

    private void drawKiosk(Kiosk kiosk) {
        Float x = kiosk.getPosition().getX();
        Float y = kiosk.getPosition().getY();

        renderer.setColor(kiosk.getColor());
        renderer.circle(x, y, kiosk.getSize());
    }
}
