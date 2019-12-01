package org.example.parklib;

import org.example.parklib.payment.Receipt;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.parklib.CarType.ELECTRIC_50KW;
import static org.example.parklib.CarType.GAS;

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
        Optional<Receipt> secondReceipt =  parking.park(new Car(GAS));

        assertThat(secondReceipt).isNotEmpty();
    }

    @Test
    void cantReuseParkingSlotWithoutFreeing() {
        Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(GAS)));

        parking.park(new Car(GAS));
        Optional<Receipt> receipt = parking.park(new Car(GAS));

        assertThat(receipt).isEmpty();
    }
}
