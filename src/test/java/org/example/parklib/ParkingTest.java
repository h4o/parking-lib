package org.example.parklib;

import org.example.parklib.car.Car;
import org.example.parklib.car.CarType;
import org.example.parklib.car.CarTypeParkingFilter;
import org.example.parklib.payment.FixedPriceStrategy;
import org.example.parklib.payment.Invoice;
import org.example.parklib.payment.Receipt;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.parklib.car.CarType.*;

public class ParkingTest {
    @Test
    void canPark() {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(GAS)));

        Optional<Receipt> receipt = parking.park(new Car(GAS));

        assertThat(receipt).isNotEmpty();
    }

    @Test
    void cantParkWithoutAnySlot() {
        Parking parking = new Parking();

        Optional<Receipt> receipt = parking.park(new Car(GAS));

        assertThat(receipt).isEmpty();
    }

    @Test
    void canSupportMultipleSlots() {
        CarTypeParkingFilter carTypeParkingFilter = new CarTypeParkingFilter(GAS);
        Parking parking = new Parking(new ParkingSlot(carTypeParkingFilter), new ParkingSlot(carTypeParkingFilter));

        Optional<Receipt> receipt = parking.park(new Car(GAS));
        Optional<Receipt> secondReceipt = parking.park(new Car(GAS));

        assertThat(receipt).isNotEmpty();
        assertThat(secondReceipt).isNotEmpty();
    }

    @Test
    void dispatchesTheToTheCorrespondingSlots() {
        ParkingSlot gasCarTypeParkingSlot = new ParkingSlot(new CarTypeParkingFilter(GAS));
        ParkingSlot electricCarTypeParkingSlot = new ParkingSlot(new CarTypeParkingFilter(ELECTRIC_50KW));
        Parking parking = new Parking(gasCarTypeParkingSlot, electricCarTypeParkingSlot);

        Optional<Receipt> receipt = parking.park(new Car(GAS));
        Optional<Receipt> secondReceipt = parking.park(new Car(ELECTRIC_50KW));

        assertThat(receipt).isNotEmpty();
        assertThat(receipt.get().getParkingSlot()).isEqualTo(gasCarTypeParkingSlot);
        assertThat(secondReceipt).isNotEmpty();
        assertThat(secondReceipt.get().getParkingSlot()).isEqualTo(electricCarTypeParkingSlot);

    }

    @Test
    void cantParkDifferentCarType() {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(GAS)));

        Optional<Receipt> receipt = parking.park(new Car(CarType.ELECTRIC_50KW));

        assertThat(receipt).isEmpty();
    }

    @Test
    void canReuseParkingSlot() {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(GAS)));

        Optional<Receipt> receipt = parking.park(new Car(GAS));
        parking.free(receipt.get());
        Optional<Receipt> secondReceipt = parking.park(new Car(GAS));

        assertThat(secondReceipt).isNotEmpty();
    }

    @Test
    void cantReuseParkingSlotWithoutFreeing() {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(GAS)));

        parking.park(new Car(GAS));
        Optional<Receipt> receipt = parking.park(new Car(GAS));

        assertThat(receipt).isEmpty();
    }

    @Test
    void parkingIsFreeByDefault() {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(GAS)));

        Optional<Receipt> receipt = parking.park(new Car(GAS));
        Optional<Invoice> invoice = parking.free(receipt.get());

        assertThat(invoice).isNotEmpty();
        assertThat(invoice.get().getValue()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void differentPricingStrategyTest() {
        Parking parking = new Parking(
                new FixedPriceStrategy(BigDecimal.TEN), Collections.singletonList(new ParkingSlot(new CarTypeParkingFilter(GAS)))
        );

        Optional<Receipt> receipt = parking.park(new Car(GAS));
        Optional<Invoice> invoice = parking.free(receipt.get());

        assertThat(invoice).isNotEmpty();
        assertThat(invoice.get().getValue()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void receiptContainsTheEnterAndExitTime() {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(GAS)));
        MutableClock clock = new MutableClock();
        parking.setClock(clock);
        Instant start = Instant.parse("2019-12-02T19:00:00Z");
        Instant stop = Instant.parse("2019-12-02T20:00:00Z");

        clock.setFixedAt(start);
        Optional<Receipt> receipt = parking.park(new Car(GAS));
        clock.setFixedAt(stop);
        parking.free(receipt.get());

        assertThat(receipt.get().getStartTime()).isEqualTo(LocalDateTime.ofInstant(start, clock.getZone()));
        assertThat(receipt.get().getEndTime()).isEqualTo(LocalDateTime.ofInstant(stop, clock.getZone()));
    }

    @RepeatedTest(10) // run multiple time to catch thread safety issue
    void parkIsThreadSafe() throws InterruptedException {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(ELECTRIC_20KW)), new ParkingSlot(new CarTypeParkingFilter(ELECTRIC_20KW)));
        Vector<Optional<Receipt>> receipts = new Vector<>(); // vector is thread safe, list is not
        ExecutorService es = Executors.newScheduledThreadPool(4);

        for(int i = 0; i  < 128; i++) {
            es.execute(() -> receipts.add(parking.park(new Car(ELECTRIC_20KW))));
        }
        es.shutdown();
        es.awaitTermination(20, TimeUnit.SECONDS);
        assertThat(receipts.stream().filter(Optional::isPresent).count()).isEqualTo(2);
    }


    @RepeatedTest(10) // run multiple time to catch thread safety issue
    void freeIsThreadSafe() throws InterruptedException {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(ELECTRIC_20KW)), new ParkingSlot(new CarTypeParkingFilter(ELECTRIC_20KW)));
        Receipt receipt1 = parking.park(new Car(ELECTRIC_20KW) ).get();
        Receipt receipt2 = parking.park(new Car(ELECTRIC_20KW) ).get();

        Vector<Optional<Invoice>> invoices = new Vector<>(); // vector is thread safe, list is not
        ExecutorService es = Executors.newScheduledThreadPool(4);

        for(int i = 0; i  < 128; i++) {
            int finalI = i; // we need a final variable for our threads
            es.execute(() -> invoices.add(parking.free(finalI % 2 == 0 ? receipt1 : receipt2)));
        }
        es.shutdown();
        es.awaitTermination(20, TimeUnit.SECONDS);
        assertThat(invoices.stream().filter(Optional::isPresent).count()).isEqualTo(2);
    }
}

class MutableClock extends Clock {
    Instant fixedInstant;

    public void setFixedAt(Instant instant) {
        fixedInstant = instant;
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this; // ignore the timezone change
    }

    @Override
    public Instant instant() {
        return fixedInstant;
    }
}