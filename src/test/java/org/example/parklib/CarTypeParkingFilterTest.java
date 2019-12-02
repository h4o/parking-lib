package org.example.parklib;

import org.example.parklib.car.Car;
import org.example.parklib.car.CarTypeParkingFilter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.parklib.car.CarType.ELECTRIC_50KW;
import static org.example.parklib.car.CarType.GAS;

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