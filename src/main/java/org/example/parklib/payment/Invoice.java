package org.example.parklib.payment;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Fixed currency immutable invoice (using EUR).
 */
public class Invoice {
    private BigDecimal value;

    public Invoice(BigDecimal value) {

        this.value = value;
    }

    /**
     * @return the currency given
     */
    public Currency getCurrency() {
        return Currency.getInstance("EUR");
    }

    /**
     * @return the invoice value
     */
    public BigDecimal getValue() {
        return value;
    }
}
