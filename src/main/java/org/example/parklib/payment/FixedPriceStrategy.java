package org.example.parklib.payment;

import java.math.BigDecimal;

/**
 * Pricing strategy returning a fixed price for each invoce
 * @see PricingStrategy
 */
public class FixedPriceStrategy implements PricingStrategy {

    private BigDecimal price;

    /**
     * Creates a new strategy charging no money (fixed price of 0)
     */
    public FixedPriceStrategy() {
        price = BigDecimal.ZERO;
    }

    /**
     * Creates a new strategy using the given price as the fee
     * @param price the price to use for all invoices
     */
    public FixedPriceStrategy(BigDecimal price) {
        this.price = price;
    }

    @Override
    public Invoice generateInvoice(Receipt receipt) {
        return new Invoice(price);
    }
}
