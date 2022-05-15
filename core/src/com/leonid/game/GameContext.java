
package com.leonid.game;

import com.leonid.game.calc.GameCalculator;
import com.leonid.game.config.Config;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.customer.CustomerTransitionState;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.domain.kiosk.KioskProcessingState;
import com.leonid.game.domain.kiosk.KioskWaitingState;
import com.leonid.game.event.CustomerProcessedEvent;
import com.leonid.game.event.KioskDeadEvent;
import com.leonid.game.generator.CustomerGenerator;
import com.leonid.game.generator.KioskGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
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

    private final EntitiesHolder holder;
    private final KioskGenerator kioskGenerator;
    private final CustomerGenerator customerGenerator;
    private final GameCalculator gameCalculator;

    private final Random random = new Random();

    private final Map<Customer, CustomerContext> customerContexts = new HashMap<>();
    private final Map<Kiosk, KioskContext> kioskContexts = new HashMap<>();
    private int kiosksCount;
    private int customersCount;

    @Autowired
    private ApplicationContext app;
    @Autowired
    private Config config;
    private LocalTime autoReinitAt;


    public GameContext(EntitiesHolder holder,
                       KioskGenerator kioskGenerator,
                       CustomerGenerator customerGenerator,
                       GameCalculator gameCalculator) {
        this.holder = holder;
        this.kioskGenerator = kioskGenerator;
        this.customerGenerator = customerGenerator;
        this.gameCalculator = gameCalculator;
    }

    @PostConstruct
    public void init() {
        generateKiosks();
        generateCustomers();
    }

    private void generateKiosks() {
        int configKiosksCount = config.getKiosksCount();

        if (configKiosksCount == -1) {
            kiosksCount = random.nextInt(9) + 5;
        } else {
            kiosksCount = configKiosksCount;
        }
        for (int i = 0; i < kiosksCount; i++) {
            generateKiosk();
        }
    }

    private void generateKiosk() {
        Kiosk kiosk = kioskGenerator.generate(WIDTH, HEIGHT);
        holder.addEntity(kiosk);

        KioskContext kioskContext = new KioskContext(kiosk);
        kioskContext.setState(app.getBean(KioskWaitingState.class, kioskContext));
        kioskContexts.put(kiosk, kioskContext);
    }

    private void generateCustomers() {
        customersCount = Math.max(kiosksCount, random.nextInt(kiosksCount * 5));
        generateCustomers(customersCount);
    }

    private void generateCustomers(int customersCount) {
        for (int i = 0; i < customersCount; i++) {
            generateCustomer();
        }
    }

    private void generateCustomer() {
        Customer customer = customerGenerator.generate(WIDTH, HEIGHT);
        customer.setKiosk(gameCalculator.getBestKiosk(customer));
        holder.addEntity(customer);

        CustomerContext customerContext = new CustomerContext(customer);

        customerContext.setCustomerState(app.getBean(CustomerTransitionState.class, customerContext));

        customerContexts.put(customer, customerContext);
    }

    public void reinit() {
        clear();
        init();
    }

    private void clear() {
        holder.clear();
        customerContexts.clear();
        kioskContexts.clear();
        autoReinitAt = null;
    }

    public void tic() {
        customerContexts.values().forEach(CustomerContext::tic);
        kioskContexts.values().forEach(KioskContext::tic);

        recalcCustomerCount();

        checkAutoReinit();

        generateCustomersRandomly();
    }

    private void generateCustomersRandomly() {
        if (random.nextInt(config.getProbabilityBound()) < 1 && kiosksCount > 0) {
            generateCustomers(random.nextInt(kiosksCount * (random.nextInt(5) + 1)));
        }
    }

    private void recalcCustomerCount() {
        holder.getEntities().forEachRemaining(entity -> {
            if (entity instanceof Customer) {
                Customer customer = (Customer) entity;
                KioskContext kioskContext = getContext(customer.getKiosk());
                if (kioskContext == null || kioskContext.getKiosk() == null) {
                    return;
                }
                CustomerContext customerContext = getContext(customer);

                if (customer.getCustomerStatus() == QUEUE && !kioskContext.isInQueue(customerContext)) {
                    kioskContext.putCustomer(customerContext);
                }

                if (kioskContext.getKiosk().getStatus() == WAITING && kioskContext.getProcessingCustomer() != null) {
                    kioskContext.setState(app.getBean(KioskProcessingState.class, kioskContext));
                }
            }
        });
    }

    private CustomerContext getContext(Customer customer) {
        return customerContexts.get(customer);
    }

    private KioskContext getContext(Kiosk customer) {
        return kioskContexts.get(customer);
    }

    private void checkAutoReinit() {
        if (autoReinitAt != null && LocalTime.now().isAfter(autoReinitAt)) {
            autoReinitAt = null;
            reinit();
        }
    }

    public KioskContext getKioskContext(Kiosk kiosk) {
        return getContext(kiosk);
    }

    @EventListener
    public void onApplicationEvent(CustomerProcessedEvent event) {
        Customer customer = event.getCustomer();

        holder.remove(customer);
        customerContexts.remove(customer);
    }


    @EventListener
    public void onApplicationEvent(KioskDeadEvent event) {

        if (isEveryKioskDead() && autoReinitAt == null) {
            autoReinitAt = LocalTime.now().plusMinutes(1);
        }
    }

    private boolean isEveryKioskDead() {
        return kioskContexts.keySet().stream().anyMatch(kiosk -> kiosk.getStatus() != DEAD);
    }
}
