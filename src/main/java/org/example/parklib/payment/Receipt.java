package org.example.parklib.payment;

import org.example.parklib.Car;
import org.example.parklib.Parking;
import org.example.parklib.ParkingSlot;

public class Receipt {
    private ParkingSlot parkingSlot;

    public Receipt(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }
}
