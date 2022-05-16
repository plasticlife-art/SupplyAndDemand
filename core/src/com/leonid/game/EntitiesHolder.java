package com.leonid.game;

import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.customer.CustomerContext;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class EntitiesHolder {

    private final Set<HasPhysics> entities = new LinkedHashSet<>();

    private final Map<Customer, CustomerContext> customerContexts = new HashMap<>();
    private final Map<Kiosk, KioskContext> kioskContexts = new HashMap<>();

    public void addEntity(Collection<HasPhysics> kiosks) {
        this.entities.addAll(kiosks);
    }

    public void addEntity(HasPhysics kiosk) {
        entities.add(kiosk);
    }

    public Iterator<HasPhysics> getEntities() {
        return entities.iterator();
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
    }

    public void addEntity(KioskContext kioskContext) {
        kioskContexts.put(kioskContext.getMaster(), kioskContext);
    }

    public void ticContexts() {
        customerContexts.values().forEach(CustomerContext::tic);
        kioskContexts.values().forEach(KioskContext::tic);
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
}
