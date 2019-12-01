package org.example.parklib;

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
