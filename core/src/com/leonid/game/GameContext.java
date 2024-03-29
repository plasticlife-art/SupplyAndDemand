
package com.leonid.game;

import com.leonid.game.calc.Calculator;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.domain.kiosk.state.KioskWaitingState;
import com.leonid.game.event.CustomerComeHome;
import com.leonid.game.event.KioskDeadEvent;
import com.leonid.game.generator.CustomerGenerator;
import com.leonid.game.generator.HomeGenerator;
import com.leonid.game.generator.KioskGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Random;

import static com.leonid.game.Game.HEIGHT;
import static com.leonid.game.Game.WIDTH;
import static com.leonid.game.domain.kiosk.KioskStatus.DEAD;

/**
 * @author Leonid Cheremshantsev
 */

@Component
public class GameContext {

    private final Logger log = LoggerFactory.getLogger(GameContext.class);

    private final EntityHolder holder;
    private final KioskGenerator kioskGenerator;
    private final CustomerGenerator customerGenerator;
    private final HomeGenerator homeGenerator;
    private final Calculator calculator;
    private final Random random = new Random();
    private int kiosksGenerationCount;

    @Autowired
    private ApplicationContext app;
    @Autowired
    private Config config;
    private LocalTime autoReInitAt;
    private StopWatch stopWatch;
    private LocalTime printAt = LocalTime.now().plusMinutes(1);


    public GameContext(EntityHolder holder,
                       KioskGenerator kioskGenerator,
                       CustomerGenerator customerGenerator,
                       HomeGenerator homeGenerator,
                       Calculator calculator) {
        this.holder = holder;
        this.kioskGenerator = kioskGenerator;
        this.customerGenerator = customerGenerator;
        this.homeGenerator = homeGenerator;
        this.calculator = calculator;

        stopWatch = new StopWatch("tic");
    }

    @PostConstruct
    public void init() {
        generateKiosks();

        generateHomes();
    }

    private void generateHomes() {
        for (int i = 0; i < config.getGenerationHomeCount(); i++) {
            holder.addEntity(homeGenerator.generate(WIDTH, HEIGHT));
        }
    }

    private void generateKiosks() {
        kiosksGenerationCount = getKiosksGenerationCount();
        for (int i = 0; i < kiosksGenerationCount; i++) {
            generateKiosk();
        }
    }

    private int getKiosksGenerationCount() {
        int configKiosksCount = config.getKiosksGenerationCount();

        if (configKiosksCount == -1) {
            return random.nextInt(9) + 5;
        } else {
            return configKiosksCount;
        }
    }

    private void generateKiosk() {
        Kiosk kiosk = kioskGenerator.generate(WIDTH, HEIGHT);
        holder.addEntity(kiosk);

        KioskContext kioskContext = new KioskContext(kiosk);
        kioskContext.setKioskMaxLevel(config.getKioskMaxLevel());
        kioskContext.setState(app.getBean(KioskWaitingState.class, kioskContext));
        kioskContext.setLevelUpThreshold(config.getKioskLevelUpThreshold());
        holder.addEntity(kioskContext);
    }

    public int getMaxKioskLevel() {
        return (int) holder.getMaxKioskLevel();
    }

    public void reinit() {
        clear();
        init();
    }

    private void clear() {
        holder.clear();
        autoReInitAt = null;
    }

    public void tic() {
        stopWatch.start();

        holder.ticContexts();
        checkAutoReInit();
        stopWatch.stop();
        printPerformanceStatisticIfNeeded();
    }

    private void printPerformanceStatisticIfNeeded() {
        if (LocalTime.now().isAfter(printAt)) {
            printAt = LocalTime.now().plusMinutes(1);
            log.info("Tic time avg: {} ns", formatNanos(Math.round(1.0f * stopWatch.getTotalTimeNanos() / stopWatch.getTaskCount())));
            log.info("Last tic: {} ns. Total objects: {}. Customers: {}, Homes: {}, Kiosks: {}.",
                    formatNanos(stopWatch.getLastTaskTimeNanos()),
                    holder.getCustomersCount() + holder.getHomeCount() + holder.getKiosksCount(),
                    holder.getCustomersCount(),
                    holder.getHomeCount(),
                    holder.getKiosksCount());
        }
    }

    private String formatNanos(long nanos) {
        String s = String.valueOf(nanos);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = s.length() - 1; i >= 0; i--) {
            if ((s.length() - 1 - i) % 3 == 0 && i != s.length() - 1) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(s.charAt(i));
        }

        return stringBuilder.reverse().toString();
    }
    public CustomerContext getContext(Customer customer) {
        return holder.getContext(customer);
    }

    public KioskContext getContext(Kiosk customer) {
        return holder.getContext(customer);
    }

    private void checkAutoReInit() {
        if (autoReInitAt != null && LocalTime.now().isAfter(autoReInitAt)) {
            autoReInitAt = null;
            reinit();
        }
    }

    @EventListener
    public void onApplicationEvent(CustomerComeHome event) {
        Customer customer = event.getCustomer();

        holder.remove(customer);
    }

    @EventListener
    public void onApplicationEvent(KioskDeadEvent event) {
        if (isEveryKioskDead() && autoReInitAt == null) {
            autoReInitAt = LocalTime.now().plusMinutes(1);
            log.info("Everybody dead. Restart in 1 minute.");
        }
    }

    private boolean isEveryKioskDead() {
        return deadKioskCount() == holder.getEntities(Kiosk.class).size();
    }

    public int deadKioskCount() {
        int count = 0;
        Iterator<HasPhysics> entities = holder.getEntities();
        while (entities.hasNext()) {
            HasPhysics entity = entities.next();

            if (entity instanceof Kiosk) {
                Kiosk kiosk = (Kiosk) entity;
                if (kiosk.getStatus() == DEAD) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getKioskCount() {
        return kiosksGenerationCount;
    }

    public int getCustomerCount() {
        return holder.getCustomersCount();
    }
}
