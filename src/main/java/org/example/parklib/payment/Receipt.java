package org.example.parklib.payment;

import org.example.parklib.ParkingSlot;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * A receipt given after parking. Contains the parking slot, start time and end time corresponding to it's usage
 * @see org.example.parklib.Parking
 */
public class Receipt {
    private ParkingSlot parkingSlot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Receipt(ParkingSlot parkingSlot, LocalDateTime startTime) {
        this.parkingSlot = parkingSlot;
        this.startTime = startTime;
    }

    /**
     * Ends the parking usage on the receipt. Only updates the endtime if the receipt is completed
     * @param endTime the time at which the parking ends
     */
    public void exit(LocalDateTime endTime) {
        if (!isCompleted()) {
            this.endTime = endTime;
        }
    }

    /**
     * @return whether or not the exit method has been called
     */
    public boolean isCompleted() {
        return this.endTime != null;
    }

    /**
     * @return the parking slot linked to the receipt
     */
    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    /**
     * @return the time of creation of the receipt
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @return the time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @return the duration of the receipt (between startTime and endTime)
     * @throws IllegalStateException if the method end() has not been called
     */
    public Duration getDuration() {
        if(endTime == null)
            throw new IllegalStateException("getDuration can not be called before end()");
        return Duration.between(startTime, endTime);
    }
}
