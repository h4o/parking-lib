package org.example.parklib.payment;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class FixedPriceStrategyTest {
    @Test
    void invoicesGivenPrice() {
        FixedPriceStrategy fixedPriceStrategy = new FixedPriceStrategy(new BigDecimal(2));

        Invoice invoice = fixedPriceStrategy.generateInvoice(null);

        assertThat(invoice.getValue()).isEqualTo(new BigDecimal(2));
    }

    @Test
    void isFreeByDefault() {
        FixedPriceStrategy fixedPriceStrategy = new FixedPriceStrategy();

        Invoice invoice = fixedPriceStrategy.generateInvoice(null);

        assertThat(invoice.getValue()).isEqualTo(BigDecimal.ZERO);
    }
}