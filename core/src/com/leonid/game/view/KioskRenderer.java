package com.leonid.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;

/**
 * @author Leonid Cheremshantsev
 */
public class KioskRenderer {

    private final ShapeRenderer renderer;
    private final SpriteBatch batch;
    private final BitmapFont font;

    public KioskRenderer(ShapeRenderer renderer, SpriteBatch batch, BitmapFont font) {
        this.renderer = renderer;
        this.batch = batch;

        this.font = font;
    }


    public void render(KioskContext kioskContext) {
        Kiosk kiosk = kioskContext.getKiosk();

        renderKiosk(kiosk);

        Float x = kiosk.getPosition().getX();
        Float y = kiosk.getPosition().getY();
        int size = kiosk.getSize();

        batch.begin();
        drawCustomerInQueueCount(kioskContext, x, y, size);
        drawProcessedCustomerCount(kioskContext, x, y, size);
        drawKioskLevel(kioskContext, x, y, size);
        batch.end();
    }

    private void drawKioskLevel(KioskContext kioskContext, Float x, Float y, int size) {
        font.setColor(Color.BLACK);
        font.draw(batch, String.valueOf(kioskContext.getKiosk().getLevel()), x + size, y + size);
    }

    private void drawProcessedCustomerCount(KioskContext kioskContext, Float x, Float y, int size) {
        font.setColor(Color.GREEN);
        font.draw(batch, String.valueOf(kioskContext.getProcessedCustomers()), x + size, y - size);
    }

    private void drawCustomerInQueueCount(KioskContext kioskContext, Float x, Float y, int size) {
        font.setColor(Color.valueOf("FFD700"));
        font.draw(batch, String.valueOf(kioskContext.getCustomersCount()), x - size, y - size);
    }

    private void renderKiosk(Kiosk kiosk) {
        Float x = kiosk.getPosition().getX();
        Float y = kiosk.getPosition().getY();

        renderer.setColor(kiosk.getColor());
        renderer.circle(x, y, kiosk.getSize());
    }

}
