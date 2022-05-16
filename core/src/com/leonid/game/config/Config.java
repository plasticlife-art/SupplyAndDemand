package com.leonid.game.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Leonid Cheremshantsev
 */

@Configuration
@ConfigurationProperties(prefix = "game")
@PropertySource("classpath:app.properties")
public class Config {

    private int kiosksGenerationCount;
    private int kioskMaxLevel;
    private float kioskProcessingTimeDefaultSeconds;
    private float kioskProcessingTimeToLevelMultiplier;
    private float kioskMaxQueueToLevelMultiplier;
    private int kioskDefaultMaxQueue;

    private int generationHomeCount;
    private float generationCustomerPerHomePerTime;
    private Long generationCustomerTime;
    private int customerToGoToAnotherKioskPercent;
    private int customerWaitingTime;
    private long kioskWaitingTime;
    private int customerSpeedMultiplier;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(getGenerationHomeCount());
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    public int getKiosksGenerationCount() {
        return kiosksGenerationCount;
    }

    public void setKiosksGenerationCount(int kiosksGenerationCount) {
        this.kiosksGenerationCount = kiosksGenerationCount;
    }

    public float getKioskProcessingTimeDefaultSeconds() {
        return kioskProcessingTimeDefaultSeconds;
    }

    public void setKioskProcessingTimeDefaultSeconds(float kioskProcessingTimeDefaultSeconds) {
        this.kioskProcessingTimeDefaultSeconds = kioskProcessingTimeDefaultSeconds;
    }

    public int getGenerationHomeCount() {
        return generationHomeCount;
    }

    public void setGenerationHomeCount(int generationHomeCount) {
        this.generationHomeCount = generationHomeCount;
    }

    public float getGenerationCustomerPerHomePerTime() {
        return generationCustomerPerHomePerTime;
    }

    public void setGenerationCustomerPerHomePerTime(float generationCustomerPerHomePerTime) {
        this.generationCustomerPerHomePerTime = generationCustomerPerHomePerTime;
    }

    public Long getGenerationCustomerTime() {
        return generationCustomerTime;
    }

    public void setGenerationCustomerTime(Long generationCustomerTime) {
        this.generationCustomerTime = generationCustomerTime;
    }

    public float getKioskMaxQueueToLevelMultiplier() {
        return kioskMaxQueueToLevelMultiplier;
    }

    public void setKioskMaxQueueToLevelMultiplier(float kioskMaxQueueToLevelMultiplier) {
        this.kioskMaxQueueToLevelMultiplier = kioskMaxQueueToLevelMultiplier;
    }

    public int getKioskDefaultMaxQueue() {
        return kioskDefaultMaxQueue;
    }

    public void setKioskDefaultMaxQueue(int kioskDefaultMaxQueue) {
        this.kioskDefaultMaxQueue = kioskDefaultMaxQueue;
    }

    public float getKioskProcessingTimeToLevelMultiplier() {
        return kioskProcessingTimeToLevelMultiplier;
    }

    public void setKioskProcessingTimeToLevelMultiplier(float kioskProcessingTimeToLevelMultiplier) {
        this.kioskProcessingTimeToLevelMultiplier = kioskProcessingTimeToLevelMultiplier;
    }

    public int getKioskMaxLevel() {
        return kioskMaxLevel;
    }

    public void setKioskMaxLevel(int kioskMaxLevel) {
        this.kioskMaxLevel = kioskMaxLevel;
    }

    public int getCustomerToGoToAnotherKioskPercent() {
        return customerToGoToAnotherKioskPercent;
    }

    public void setCustomerToGoToAnotherKioskPercent(int customerToGoToAnotherKioskPercent) {
        this.customerToGoToAnotherKioskPercent = customerToGoToAnotherKioskPercent;
    }

    public int getCustomerWaitingTime() {
        return customerWaitingTime;
    }

    public void setCustomerWaitingTime(int customerWaitingTime) {
        this.customerWaitingTime = customerWaitingTime;
    }

    public long getKioskWaitingTime() {
        return kioskWaitingTime;
    }

    public void setKioskWaitingTime(long kioskWaitingTime) {
        this.kioskWaitingTime = kioskWaitingTime;
    }

    public int getCustomerSpeedMultiplier() {
        return customerSpeedMultiplier;
    }

    public void setCustomerSpeedMultiplier(int customerSpeedMultiplier) {
        this.customerSpeedMultiplier = customerSpeedMultiplier;
    }
}
