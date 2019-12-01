package org.example.parklib;

import org.example.parklib.payment.Receipt;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ParkingTest {
    @Test
    void canPark() {
        Parking parking = new Parking();

        Optional<Receipt> receipt = parking.park(new Car());

        assertThat(receipt).isNotEmpty();
    }
}
