
package com.leonid.game;

import com.leonid.game.calc.GameCalculator;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.domain.kiosk.state.KioskProcessingState;
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

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Random;

import static com.leonid.game.Game.HEIGHT;
import static com.leonid.game.Game.WIDTH;
import static com.leonid.game.domain.customer.CustomerStatus.QUEUE;
import static com.leonid.game.domain.kiosk.KioskStatus.DEAD;
import static com.leonid.game.domain.kiosk.KioskStatus.WAITING;

/**
 * @author Leonid Cheremshantsev
 */

@Component
public class GameContext {

    private final Logger log = LoggerFactory.getLogger(GameContext.class);

    private final EntitiesHolder holder;
    private final KioskGenerator kioskGenerator;
    private final CustomerGenerator customerGenerator;
    private final HomeGenerator homeGenerator;
    private final GameCalculator gameCalculator;
    private final Random random = new Random();
    private int kiosksGenerationCount;
    private int customersGenerationCount;

    @Autowired
    private ApplicationContext app;
    @Autowired
    private Config config;
    private LocalTime autoReInitAt;


    public GameContext(EntitiesHolder holder,
                       KioskGenerator kioskGenerator,
                       CustomerGenerator customerGenerator,
                       HomeGenerator homeGenerator,
                       GameCalculator gameCalculator) {
        this.holder = holder;
        this.kioskGenerator = kioskGenerator;
        this.customerGenerator = customerGenerator;
        this.homeGenerator = homeGenerator;
        this.gameCalculator = gameCalculator;
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
        kioskContext.setState(app.getBean(KioskWaitingState.class, kioskContext));
        holder.addEntity(kioskContext);
    }

    private void generateCustomers() {
        customersGenerationCount = getCustomerGenerationCount();
        generateCustomers(getCustomerGenerationCount());
    }

    private int getCustomerGenerationCount() {
        if (config.getCustomerGenerationCount() == -1) {
            return Math.max(kiosksGenerationCount, randomCustomerCount());
        } else {
            return config.getCustomerGenerationCount();
        }
    }

    private int randomCustomerCount() {
        return random.nextInt(Math.round(randomCustomerBorder()));
    }

    private float randomCustomerBorder() {
        return config.getCustomerGenerationBorder() + config.getCustomerGenerationKioskLevelCoef() * getMaxKioskLevel();
    }

    public int getMaxKioskLevel() {
        return (int) holder.getMaxKioskLevel();
    }

    private void generateCustomers(int customersCount) {
        for (int i = 0; i < customersCount; i++) {
            generateCustomer(WIDTH, HEIGHT);
        }
    }

    private void generateCustomer(float w, float h) {
        Customer customer = customerGenerator.generate(w, h);
        customer.setKiosk(gameCalculator.getBestKiosk(customer));
        holder.addEntity(customer);
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
        holder.ticContexts();

        recalcCustomerCount();

        checkAutoReInit();

//        generateCustomersRandomly();
    }

    private void generateCustomersRandomly() {
        if (random.nextInt(config.getProbabilityBound()) < 1 && kiosksGenerationCount > 0) {
            generateCustomers(getCustomerGenerationCount());
        }
    }

    private void recalcCustomerCount() {
        holder.getEntities().forEachRemaining(entity -> {
            if (entity instanceof Customer) {
                Customer customer = (Customer) entity;
                KioskContext kioskContext = getContext(customer.getKiosk());
                if (kioskContext == null || kioskContext.getMaster() == null) {
                    return;
                }
                CustomerContext customerContext = getContext(customer);

                if (customer.getCustomerStatus() == QUEUE && !kioskContext.isInQueue(customerContext)) {
                    kioskContext.putCustomer(customerContext);
                }

                if (kioskContext.getMaster().getStatus() == WAITING && kioskContext.getProcessingCustomer() != null) {
                    kioskContext.setState(app.getBean(KioskProcessingState.class, kioskContext));
                }
            }
        });
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

        log.info("Kiosk dead [{}/{}]", deadKioskCount(), kiosksGenerationCount);

        if (isEveryKioskDead() && autoReInitAt == null) {
            autoReInitAt = LocalTime.now().plusMinutes(1);
            log.info("Everybody dead. Restart in 1 minute.");
        }
    }

    private boolean isEveryKioskDead() {
        return deadKioskCount() == kiosksGenerationCount;
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
