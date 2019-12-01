package org.example.parklib;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingSlotTest {

    @Test
    void carNotPassingFilterIsRejected() {
        ParkingSlot parkingSlot = new ParkingSlot(car -> false);
        assertThat(parkingSlot.canPark(new Car(CarType.GAS))).isFalse();
    }

    @Test
    void carPassingFilterIsAccepted() {
        ParkingSlot parkingSlot = new ParkingSlot(car -> true);
        assertThat(parkingSlot.canPark(new Car(CarType.GAS))).isTrue();
    }

    @Test
    void parkingSetsSlotAsFull() {
        ParkingSlot parkingSlot = new ParkingSlot(car -> true);

        parkingSlot.park(new Car(CarType.GAS));

        assertThat(parkingSlot.isEmpty()).isFalse();
    }

    @Test
    void freeingSlotSetsSlotAsEmpty() {
        ParkingSlot parkingSlot = new ParkingSlot(car -> true);

        parkingSlot.park(new Car(CarType.GAS));
        parkingSlot.free();

        assertThat(parkingSlot.isEmpty()).isTrue();
    }

    @Test
    void cannotParkInFullSlot() {
        ParkingSlot parkingSlot = new ParkingSlot(car -> true);

        parkingSlot.park(new Car(CarType.GAS));
        boolean canRepark = parkingSlot.canPark(new Car(CarType.GAS));

        assertThat(canRepark).isFalse();
    }
}