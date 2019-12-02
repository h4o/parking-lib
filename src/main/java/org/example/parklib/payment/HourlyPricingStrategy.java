package org.example.parklib.payment;

import java.math.BigDecimal;

/**
 * Pricing strategy computing invoices based on the time spent parking. The granularity is by the hour.
 */
public class HourlyPricingStrategy implements PricingStrategy {
    private BigDecimal baseAmount;
    private BigDecimal hourlyAmount;

    /**
     * Creates a new strategy with the given amount as constant for computing invoices
     * @param baseAmount the amount charged for every parking usage regardless of the duration
     * @param hourlyAmount the amount charged for every hour of parking
     */
    public HourlyPricingStrategy(BigDecimal baseAmount, BigDecimal hourlyAmount) {
        this.baseAmount = baseAmount;
        this.hourlyAmount = hourlyAmount;
    }

    /**
     * Generates an invoice using the given base and hourly amount
     * <p>
     *     The invoice value is computed using the formula baseAmount + hoursSpent * hourlyAmount.
     *     The hoursSpent is the rounded up duration of the parking in hours
     * </p>
     * @param receipt the receipt used for generating the invoice
     * @return the invoice computed
     */
    @Override
    public Invoice generateInvoice(Receipt receipt) {
        // we don't use Duration.toHours() because we want our value rounded up
        // eg. if the user spent 1h15 we want to bill for 2 hours and not for 1 hour
        BigDecimal timeSpent = new BigDecimal((int) Math.ceil(receipt.getDuration().toMinutes() / 60d));
        return new Invoice(baseAmount.add(hourlyAmount.multiply(timeSpent)));
    }
}
