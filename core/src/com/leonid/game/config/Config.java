package com.leonid.game.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Leonid Cheremshantsev
 */

@Configuration
@ConfigurationProperties(prefix = "game")
@PropertySource("classpath:app.properties")
public class Config {

    private int kiosksGenerationCount;
    private int customerGenerationCount;
    private int probabilityBound;
    private int processingSeconds;
    private int levelThreshold;
    private float customerGenerationKioskCoef;

    public int getKiosksGenerationCount() {
        return kiosksGenerationCount;
    }

    public void setKiosksGenerationCount(int kiosksGenerationCount) {
        this.kiosksGenerationCount = kiosksGenerationCount;
    }

    public int getProbabilityBound() {
        return probabilityBound;
    }

    public void setProbabilityBound(int probabilityBound) {
        this.probabilityBound = probabilityBound;
    }

    public int getProcessingSeconds() {
        return processingSeconds;
    }

    public void setProcessingSeconds(int processingSeconds) {
        this.processingSeconds = processingSeconds;
    }

    public int getLevelThreshold() {
        return levelThreshold;
    }

    public void setLevelThreshold(int levelThreshold) {
        this.levelThreshold = levelThreshold;
    }

    public int getCustomerGenerationCount() {
        return customerGenerationCount;
    }

    public void setCustomerGenerationCount(int customerGenerationCount) {
        this.customerGenerationCount = customerGenerationCount;
    }

    public float getCustomerGenerationKioskCoef() {
        return customerGenerationKioskCoef;
    }

    public void setCustomerGenerationKioskCoef(float customerGenerationKioskCoef) {
        this.customerGenerationKioskCoef = customerGenerationKioskCoef;
    }
}
