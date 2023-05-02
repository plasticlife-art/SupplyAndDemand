package com.leonid.game.calc;

import com.leonid.game.EntityHolder;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.common.Position;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;

import static com.leonid.game.domain.kiosk.KioskStatus.DEAD;
import static java.lang.Math.sqrt;

/**
 * @author Leonid Cheremshantsev
 */

@Component
public class Calculator {


    public static final long NANOS_PER_SECOND = 1000_000_000L;

    @Autowired
    private EntityHolder holder;
    @Autowired
    private Config config;

    public HasPhysics getEntityByClick(float x, float y) {
        Iterator<HasPhysics> entities = holder.getEntities();

        while (entities.hasNext()) {
            HasPhysics entity = entities.next();
            if (entity instanceof Kiosk) {
                if (getDistanceToCenter(x, y, entity.getPosition().getX(), entity.getPosition().getY()) <= entity.getSize()) {
                    return entity;
                }
            }
        }

        return null;
    }

    public int getKioskMaxQueue(Kiosk kiosk) {
        return Math.round(config.getKioskDefaultMaxQueue() * (1 + (kiosk.getLevel() * kiosk.getLevel() - 1) * config.getKioskMaxQueueToLevelMultiplier()));
    }


    public long getKioskProcessingTime(Kiosk kiosk) {
        return Math.round(NANOS_PER_SECOND * config.getKioskProcessingTimeDefaultSeconds() / (1 + (kiosk.getLevel() * kiosk.getLevel() - 1) * config.getKioskProcessingTimeToLevelMultiplier()));
    }

    public Kiosk calcBestKioskExcept(Customer customer, Kiosk exception) {
        ArrayList<Kiosk> kiosks = new ArrayList<>(getLiveKiosks());
        kiosks.remove(exception);
        return getBestKiosk(customer, kiosks);
    }

    public Kiosk calcBestKioskExceptHistory(Customer customer) {
        ArrayList<Kiosk> kiosks = new ArrayList<>(getLiveKiosks());
        kiosks.removeAll(customer.getKiosksHistory());
        return getBestKiosk(customer, kiosks);
    }

    public Kiosk getBestKiosk(Customer customer) {
        return getBestKiosk(customer, getLiveKiosks());
    }

    private Kiosk getBestKiosk(Customer customer, ArrayList<Kiosk> kiosks) {
        if (kiosks.size() == 0) {
            return new Kiosk(-1L, new Position(-1f, -1f), new Price(100L));
        }

        Kiosk bestKiosk;
        float bestScore;

        bestKiosk = kiosks.get(0);
        bestScore = getScore(customer, bestKiosk);

        for (Kiosk kiosk : kiosks) {
            float score = getScore(customer, kiosk);
            if (score < bestScore) {
                bestScore = score;
                bestKiosk = kiosk;
            }
        }

        return bestKiosk;
    }

    private float getScore(Customer customer, Kiosk kiosk) {

        return getDistance(customer, kiosk);
    }

    public float getDistance(HasPhysics customer, HasPhysics kiosk) {
        Float customerX = customer.getPosition().getX();
        Float customerY = customer.getPosition().getY();

        Float kioskX = kiosk.getPosition().getX();
        Float kioskY = kiosk.getPosition().getY();

        return getDistanceToCenter(customerX, customerY, kioskX, kioskY) - kiosk.getSize();
    }

    private float getDistanceToCenter(Float customerX, Float customerY, Float kioskX, Float kioskY) {
        return (float) sqrt((customerX - kioskX) * (customerX - kioskX) + (customerY - kioskY) * (customerY - kioskY));
    }

    private ArrayList<Kiosk> getLiveKiosks() {
        ArrayList<Kiosk> kiosks = new ArrayList<>();

        holder.getEntities().forEachRemaining(entity -> {
            if (isAliveKiosk(entity)) {
                kiosks.add((Kiosk) entity);
            }
        });

        return kiosks;
    }

    private boolean isAliveKiosk(HasPhysics entity) {
        return entity instanceof Kiosk && ((Kiosk) entity).getStatus() != DEAD;
    }

}
