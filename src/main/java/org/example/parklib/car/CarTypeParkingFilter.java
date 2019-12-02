package org.example.parklib.car;

import org.example.parklib.ParkingFilter;

/**
 * ParkingFilter used by Parking slots to check if a car can park based on its CarType
 */
public class CarTypeParkingFilter implements ParkingFilter {
    private CarType carType;

    public CarTypeParkingFilter(CarType carType) {
        this.carType = carType;
    }


    @Override
    public boolean canPark(Car car) {
        return car.getCarType() == carType;
    }
}
