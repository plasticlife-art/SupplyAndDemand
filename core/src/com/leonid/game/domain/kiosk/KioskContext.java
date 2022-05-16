package com.leonid.game.domain.kiosk;

import com.leonid.game.domain.common.Context;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.CustomerContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.util.stream.Collectors.toList;

/**
 * @author Leonid Cheremshantsev
 */
public class KioskContext implements Context<Kiosk> {

    private final Kiosk kiosk;
    private Queue<CustomerContext> customerQueue = new LinkedList<>();

    private State<KioskContext> state;
    private long processed = 0;
    private int kioskMaxLevel;

    public KioskContext(Kiosk kiosk) {
        this.kiosk = kiosk;
    }

    public Kiosk getMaster() {
        return kiosk;
    }

    public void putCustomer(CustomerContext customerContext) {
        customerQueue.add(customerContext);
    }

    public boolean isInQueue(CustomerContext customerContext) {
        return customerQueue.contains(customerContext);
    }

    public int getCustomersCount() {
        return customerQueue.size();
    }

    public CustomerContext getProcessingCustomer() {
        return customerQueue.peek();
    }

    public CustomerContext processCustomer() {
        CustomerContext processedCustomer = customerQueue.poll();

        if (processedCustomer != null) {
            processed++;
        }

        if (processed % 50 == 0 && kiosk.getLevel() < getKioskMaxLevel()) {
            kiosk.levelUp();
        }

        return processedCustomer;
    }

    public void setState(State<KioskContext> state) {
        this.state = state;
    }

    public long getProcessedCustomers() {
        return processed;
    }

    public void tic() {
        cleanQueue();
        state.tic(this);
    }

    private void cleanQueue() {
        List<CustomerContext> toRemove = customerQueue.stream()
                .filter(customerContext -> !customerContext.getMaster().getKiosk().equals(kiosk))
                .collect(toList());

        customerQueue.removeAll(toRemove);
    }

    public int getKioskMaxLevel() {
        return kioskMaxLevel;
    }

    public void setKioskMaxLevel(int kioskMaxLevel) {
        this.kioskMaxLevel = kioskMaxLevel;
    }
}
