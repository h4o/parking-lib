package org.example.parklib;

public class Car {
    private CarType carType;

    public Car(CarType carType) {
        this.carType = carType;
    }

    public CarType getCarType() {
        return carType;
    }
}
