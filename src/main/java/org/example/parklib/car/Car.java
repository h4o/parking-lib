package org.example.parklib.car;

import org.example.parklib.Parking;

/**
 * A car used in the Parking system. Has a CarType used to filter Parking Slots depending on its value
 *
 * @see Parking
 */
public class Car {
    private CarType carType;

    public Car(CarType carType) {
        this.carType = carType;
    }

    public CarType getCarType() {
        return carType;
    }
}
