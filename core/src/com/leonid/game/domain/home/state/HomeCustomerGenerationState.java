package com.leonid.game.domain.home.state;

import com.leonid.game.EntityHolder;
import com.leonid.game.calc.Calculator;
import com.leonid.game.config.Config;
import com.leonid.game.domain.common.State;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.home.HomeContext;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.generator.CustomerGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Leonid Cheremshantsev
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class HomeCustomerGenerationState implements State<HomeContext> {

    private final HomeContext homeContext;
    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private CustomerGenerator customerGenerator;
    @Autowired
    private Calculator calculator;
    @Autowired
    private EntityHolder holder;
    @Autowired
    private Config config;

    private final Random random = new Random();
    private LocalTime nextGenerationAt;
    private boolean generation = false;

    public HomeCustomerGenerationState(HomeContext homeContext) {
        this.homeContext = homeContext;
        homeContext.setState(this);
    }

    @Override
    public void tic(HomeContext context) {
        if (!generation && (nextGenerationAt == null || LocalTime.now().isAfter(nextGenerationAt))) {
            startGenerationTask();
        }

    }

    private void startGenerationTask() {
        generation = true;
        taskExecutor.execute(() -> {
            generateCustomers();
            nextGenerationAt = LocalTime.now().plusSeconds(config.getGenerationCustomerTime());
            generation = false;
        });
    }

    private void generateCustomers() {
        try {
            for (int i = 0; i < config.getGenerationCustomerPerHomePerTime(); i++) {
                if (random.nextBoolean()) {
                    generateCustomer(homeContext);
                    TimeUnit.MILLISECONDS.sleep(250);
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    private void generateCustomer(HomeContext homeContext) {
        Customer customer = customerGenerator.generate(homeContext.getMaster());
        customer.setKiosk(getBestKiosk(homeContext, customer));
        holder.addEntity(customer);
    }

    private Kiosk getBestKiosk(HomeContext homeContext, Customer customer) {
        if (homeContext.getBestKiosk() == null) {
            homeContext.setBestKiosk(calculator.getBestKiosk(customer));
        }
        return homeContext.getBestKiosk();
    }
}
