package org.example.parklib;

import org.example.parklib.payment.Receipt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Parking {
    private List<ParkingSlot> parkingSlots;
    public Parking(ParkingSlot... parkingSlots) {
        this.parkingSlots = new ArrayList<>(Arrays.asList(parkingSlots));
    }

    public Optional<Receipt> park(Car car) {
        Optional<ParkingSlot> parkingSlot = parkingSlots.stream().filter(slot -> slot.canPark(car)).findFirst();
        if(parkingSlot.isPresent()){
            parkingSlot.get().park(car);
            return Optional.of(new Receipt(parkingSlot.get()));
        }
        return Optional.empty();
    }


    public void free(Receipt receipt) {
        receipt.getParkingSlot().free();
    }
}
