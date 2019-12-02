package org.example.parklib;

import org.example.parklib.car.Car;
import org.example.parklib.payment.FixedPriceStrategy;
import org.example.parklib.payment.Invoice;
import org.example.parklib.payment.PricingStrategy;
import org.example.parklib.payment.Receipt;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A parking containing a list of parking slots.
 * <p>
 * Selects the appropriate (if available) parking slot and parks the car in it.
 * Uses the PricingStrategy in the constructor to charge the user when exiting. Defaults to free when no strategy is given
 * </p>
 *
 * @see ParkingSlot
 * @see Car
 */
public class Parking {
    private List<ParkingSlot> parkingSlots;
    private PricingStrategy pricingStrategy;
    private Clock clock = Clock.systemDefaultZone();

    public Parking(ParkingSlot... parkingSlots) {
        this.parkingSlots = new ArrayList<>(Arrays.asList(parkingSlots));
        this.pricingStrategy = new FixedPriceStrategy();
    }

    public Parking(PricingStrategy pricingStrategy, ParkingSlot... parkingSlots) {
        this.parkingSlots = new ArrayList<>(Arrays.asList(parkingSlots));
        this.pricingStrategy = pricingStrategy;
    }

    public Parking(List<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
        this.pricingStrategy = new FixedPriceStrategy();
    }

    public Parking(PricingStrategy pricingStrategy, List<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
        this.pricingStrategy = pricingStrategy;
    }

    /**
     * Try to find an available ParkingSlot and parks the car in it
     *
     * @param car the car to park
     * @return Empty Optional if no space is available, Optional containing a receipt if we found a parking slot
     * @see ParkingSlot#canPark(Car)
     */
    public synchronized Optional<Receipt> park(Car car) {
        Optional<ParkingSlot> parkingSlot = parkingSlots.stream().filter(slot -> slot.canPark(car)).findFirst();
        if (parkingSlot.isPresent()) {
            if (parkingSlot.get().park(car)) {
                return Optional.of(new Receipt(parkingSlot.get(), LocalDateTime.ofInstant(clock.instant(), clock.getZone())));
            }
        }
        return Optional.empty();
    }

    /**
     * Frees the ParkingSlot linked to the receipt if it has not been completed yet.
     * Charges using the PricingStrategy
     *
     * @param receipt The receipt object return by the park method
     * @return Optional of the Invoice if the receipt is not completed. Optional of empty if it is completed
     * @see PricingStrategy
     */
    public synchronized Optional<Invoice> free(Receipt receipt) {
        if (receipt.isCompleted())
            return Optional.empty();

        receipt.getParkingSlot().free();
        receipt.exit(LocalDateTime.ofInstant(clock.instant(), clock.getZone()));
        return Optional.of(pricingStrategy.generateInvoice(receipt));
    }

    /**
     * Sets the clock used for computing start and end time for Invoices
     *
     * @param clock The clock to use for receipts
     */
    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
