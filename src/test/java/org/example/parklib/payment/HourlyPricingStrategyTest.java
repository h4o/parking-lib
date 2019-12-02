package org.example.parklib.payment;

import org.example.parklib.ParkingSlot;
import org.example.parklib.car.CarType;
import org.example.parklib.car.CarTypeParkingFilter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class HourlyPricingStrategyTest {
    @Test
    void invoiceValueTest() {
        HourlyPricingStrategy hourlyPricingStrategy = new HourlyPricingStrategy(BigDecimal.TEN, BigDecimal.ONE);
        Receipt receipt = new Receipt(new ParkingSlot(new CarTypeParkingFilter(CarType.GAS)),
                LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
        );
        receipt.exit(LocalDateTime.ofInstant(Instant.EPOCH.plus(3, ChronoUnit.HOURS), ZoneId.systemDefault()));

        Invoice invoice = hourlyPricingStrategy.generateInvoice(receipt);

        assertThat(invoice.getValue()).isEqualTo(new BigDecimal(13));
    }

    @Test
    void invoiceRoundUpTimeTest() {
        HourlyPricingStrategy hourlyPricingStrategy = new HourlyPricingStrategy(BigDecimal.TEN, BigDecimal.ONE);
        Receipt receipt = new Receipt(new ParkingSlot(new CarTypeParkingFilter(CarType.GAS)),
                LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())
        );
        receipt.exit(LocalDateTime.ofInstant(Instant.EPOCH.plus(3, ChronoUnit.HOURS).plus(20, ChronoUnit.MINUTES),
                ZoneId.systemDefault()));

        Invoice invoice = hourlyPricingStrategy.generateInvoice(receipt);

        assertThat(invoice.getValue()).isEqualTo(new BigDecimal(14));
    }
}