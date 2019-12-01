package org.example.parklib;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.parklib.CarType.ELECTRIC_50KW;
import static org.example.parklib.CarType.GAS;

public class CarTypeParkingFilterTest {

    @Test
    void chosenTypeCanPark() {
        CarTypeParkingFilter carTypeParkingFilter = new CarTypeParkingFilter(GAS);

        boolean canPark = carTypeParkingFilter.canPark(new Car(GAS));

        assertThat(canPark).isTrue();
    }

    @Test
    void wrongTypeCantPark() {
        CarTypeParkingFilter carTypeParkingFilter = new CarTypeParkingFilter(GAS);

        boolean canPark = carTypeParkingFilter.canPark(new Car(ELECTRIC_50KW));

        assertThat(canPark).isFalse();
    }


}