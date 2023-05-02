package com.leonid.game;

import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.customer.state.CustomerTransitionKioskState;
import com.leonid.game.domain.home.Home;
import com.leonid.game.domain.home.HomeContext;
import com.leonid.game.domain.home.state.HomeCustomerGenerationState;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class EntityHolder {


    @Autowired
    private ApplicationContext app;

    private final Map<HasPhysics, HasPhysics> entities = new ConcurrentHashMap<>();

    private final Map<Customer, CustomerContext> customerContexts = new ConcurrentHashMap<>();
    private final Map<Kiosk, KioskContext> kioskContexts = new ConcurrentHashMap<>();
    private final Map<Home, HomeContext> homeContexts = new ConcurrentHashMap<>();

    public void addEntity(Collection<HasPhysics> kiosks) {
        kiosks.forEach(e -> this.entities.put(e, e));
    }

    public void addEntity(HasPhysics entity) {
        entities.put(entity, entity);
        if (entity instanceof Customer) {
            CustomerContext customerContext = new CustomerContext((Customer) entity);
            app.getBean(CustomerTransitionKioskState.class, customerContext, customerContext.getMaster().getKiosk());
            addEntity(customerContext);
        } else if (entity instanceof Home) {
            HomeContext homeContext = new HomeContext((Home) entity);
            app.getBean(HomeCustomerGenerationState.class, homeContext);
            addEntity(homeContext);
        }

    }

    private void addEntity(HomeContext homeContext) {
        homeContexts.put(homeContext.getMaster(), homeContext);
    }

    public <T extends HasPhysics> List<T> getEntities(Class<T> tClass) {
        ArrayList<T> result = new ArrayList<>();
        for (HasPhysics entity : entities.values()) {
            if (tClass.isAssignableFrom(entity.getClass())) {
                result.add((T) entity);
            }
        }
        return result;
    }

    public Iterator<HasPhysics> getEntities() {
        return entities.values().iterator();
    }

    public void remove(HasPhysics entity) {
        entities.remove(entity);

        if (entity instanceof Customer) {
            customerContexts.remove(entity);
        } else if (entity instanceof Kiosk) {
            kioskContexts.remove(entity);
        }
    }

    public void clear() {
        entities.clear();
        customerContexts.clear();
        kioskContexts.clear();
        homeContexts.clear();
    }

    public void addEntity(KioskContext kioskContext) {
        kioskContexts.put(kioskContext.getMaster(), kioskContext);
    }

    private final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private final Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);
    private final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);


    public void ticContexts() {
        customerContexts.values().forEach(CustomerContext::tic);
        kioskContexts.values().forEach(KioskContext::tic);
        homeContexts.values().forEach(HomeContext::tic);
    }

    public void addEntity(CustomerContext customerContext) {
        customerContexts.put(customerContext.getMaster(), customerContext);
    }

    public CustomerContext getContext(Customer customer) {
        return customerContexts.get(customer);
    }

    public KioskContext getContext(Kiosk kiosk) {
        return kioskContexts.get(kiosk);
    }

    public float getMaxKioskLevel() {
        return kioskContexts.keySet().stream()
                .mapToInt(Kiosk::getLevel)
                .max()
                .orElse(1);
    }

    public int getCustomersCount() {
        return customerContexts.size();
    }

    public int getKiosksCount() {
        return kioskContexts.size();
    }

    public int getHomeCount() {
        return homeContexts.size();
    }
}
