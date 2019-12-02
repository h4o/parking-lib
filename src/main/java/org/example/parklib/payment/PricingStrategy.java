package org.example.parklib.payment;

/**
 * Interface used to implement different strategies to price the parking usage
 */
public interface PricingStrategy {
    /**
     * Generates an invoice
     * @param receipt the receipt used for generating the invoice
     * @return an invoice containing the price
     */
    Invoice generateInvoice(Receipt receipt);
}
