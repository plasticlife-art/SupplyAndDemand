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

    private int kiosksCount;
    private int probabilityBound;
    private int processingSeconds;
    private int levelThreshold;

    public int getKiosksCount() {
        return kiosksCount;
    }

    public void setKiosksCount(int kiosksCount) {
        this.kiosksCount = kiosksCount;
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
}
