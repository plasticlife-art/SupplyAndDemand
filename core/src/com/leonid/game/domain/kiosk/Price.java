package com.leonid.game.domain.kiosk;

/**
 * @author Leonid Cheremshantsev
 */
public class Price {

    private Long price;

    public Price(Long price) {
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
