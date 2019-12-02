package org.example.parklib;

import org.example.parklib.car.Car;

/**
 * A car representing a parking slot.
 * <p>
 * A parking slot can only be occupied by one car.
 * It uses the given ParkingFilter to check if a car can Park.
 * </p>
 *
 * @see ParkingFilter
 */
public class ParkingSlot {
    private ParkingFilter parkingFilter;
    private volatile boolean occupied = false;

    public ParkingSlot(ParkingFilter parkingFilter) {
        this.parkingFilter = parkingFilter;
    }

    /**
     * Checks if the parking is free
     *
     * @return whether the parking is empty or not
     */
    public boolean isEmpty() {
        return !occupied;
    }

    /**
     * Checks if the given car can park
     * <p>
     * A car can park if the ParkingSlot is empty and the ParkingFilter accepts the car
     * </p>
     *
     * @param car the car to check
     * @return Whether the car can park or not
     */
    public boolean canPark(Car car) {
        return isEmpty() && parkingFilter.canPark(car);
    }

    /**
     * Checks if the car can park (using canPark) and sets itself as occupied if it can
     *
     * @param car the car to park
     * @return whether the car was parked or not
     */
    public boolean park(Car car) {
        if (canPark(car)) {
            this.occupied = true;
            return true;
        }
        return false;
    }

    /**
     * frees the parking slot
     */
    public void free() {
        this.occupied = false;
    }
}
