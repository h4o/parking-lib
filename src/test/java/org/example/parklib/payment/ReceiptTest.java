package org.example.parklib.payment;

import org.example.parklib.ParkingSlot;
import org.example.parklib.car.CarType;
import org.example.parklib.car.CarTypeParkingFilter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;


public class ReceiptTest {
    @Test
    void setStartAndEndTime() {
        LocalDateTime start = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
        LocalDateTime stop = start.plus(2, ChronoUnit.HOURS);

        Receipt receipt = new Receipt(new ParkingSlot(new CarTypeParkingFilter(CarType.GAS)), start);
        receipt.exit(stop);

        assertThat(receipt.getStartTime()).isEqualTo(start);
        assertThat(receipt.getEndTime()).isEqualTo(stop);
    }
}