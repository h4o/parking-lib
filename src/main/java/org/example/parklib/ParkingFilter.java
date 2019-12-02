package org.example.parklib;

import org.example.parklib.car.Car;

/**
 * Parking filter used to check whether a car can park on not.
 * @see ParkingSlot
 */
public interface ParkingFilter {
    /**
     * @param car The car to check
     * @return whether the given car can park or not
     */
    boolean canPark(Car car);
}
