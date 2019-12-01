package org.example.parklib;

public class ParkingSlot {
    private ParkingFilter parkingFilter;
    private Car occupant;

    public ParkingSlot(ParkingFilter parkingFilter) {
        this.parkingFilter = parkingFilter;
    }

    public boolean isEmpty() {
        return occupant == null;
    }

    public boolean canPark(Car car) {
        return isEmpty() && parkingFilter.canPark(car);
    }

    public void park(Car car) {
        this.occupant = car;
    }

    public void free() {
        this.occupant = null;
    }
}
