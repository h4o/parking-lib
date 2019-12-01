package org.example.parklib;

import org.example.parklib.payment.Receipt;

import java.util.Optional;

public class Parking {
    public Optional<Receipt> park(Car car) {
        return Optional.of(new Receipt());
    }

    public void unpark(Receipt receipt) {

    }
}
