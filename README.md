# Parklib
![Build status](https://github.com/h4o/parking-lib/workflows/Java%20CI/badge.svg) 

Java implementation of a toll parking management library.


## Building

To build run `mvn clean install`

## Usage
### Adding to a project
To Add to your project, add the jitpack repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then, add to your pom.xml:
```xml
<dependency>
    <groupId>com.github.h4o</groupId>
    <artifactId>parking-lib</artifactId>
    <version>1.0</version>
</dependency>
```
And then run `mvn clean install`
### Documentation
The javadoc is available [here](https://javadoc.jitpack.io/com/github/h4o/parking-lib/1.0/javadoc/)
### Simple usage
Example build a parking that is free, with one slot for a GAS car
````java
Parking parking = new Parking(new ParkingSlot(new CarTypeParkingFilter(CarType.GAS)));

Optional<Receipt> receipt = parking.park(new Car(GAS));
Optional<Invoice> invoice = parking.free(receipt.get());
```` 
invoice will contain the price to pay for the parking. For this example it will be free as no PricingStrategy is given
### Concepts
#### Parking
Parking is the main class of our library. It permits to define a list of parking slots and a pricing strategy within its constructor.
You can also set a Clock to change the timezone or for mocking purposes.
You can use the function park to try to park a car if their is any spot available. 
You can use the function free to exit a spot after parking. 
The class is modeled after the receipt given by parking machines. Parking gives you a receipt. 
Exiting the parking requires a receipts and returns an invoice.
#### ParkingSlot
ParkingSlot is the class used to represents the slots in the parking. A parking splot uses a parking filter to check if a ar can park in it and also maintains its state (free or full). 
#### ParkingFilter
ParkingFilter is an interface used to check if a car can park in a ParkingSlot.
##### CarTypeParkingFilter
A CarTypeParkingFilter is a special kind of ParkingFilter that permits only of a given CarType to park in a ParkingSlot
For example, `new CarTypeParkingFilter(CarType.GAS)` limits a ParkingSlot for gas cars only.
#### Receipt
A receipt represents a car's usage of the parking. It is created when a car parks with the parking slot and the start date.
It is completed when exiting the slot.
#### PricingStrategy
A pricing strategy is an interface used to determine price once a car exits the parking. It is called when `parking.free(receipt)` is called.
##### FixedPriceStrategy
The FixedPriceStrategy is a strategy that charges the same amount of money for each receipt. It is free by default.
##### HourlyPricingStrategy
The HourlyPricingStrategy is a strategy for charging a fixed amount of money plus an hourly fee.
The number of hours used to compute the price is rounded up (for ex 2h30 is charged as 3 hours of usage).
### Extending the library
#### New pricing strategy
You can create new pricing strategies to use with this project by implementing the class PricingStrategy.

For example if you want to bill 10 euros for each receipts except the ones starting on a sunday you could implement it as :
````java

public class FreeSundayPricingStrategy implements PricingStrategy {
    @Override
    public Invoice generateInvoice(Receipt receipt) {
        if(receipt.getStartTime().getDayOfWeek() == DayOfWeek.SUNDAY)
            return new Invoice(BigDecimal.ZERO);
        return new Invoice(BigDecimal.TEN);
    }
}
````
You could then use it in your parking implementation with `new Parking(new FreeSundayPricingStrategy(), new ParkingSlot(new CarTypeParkingFilter(GAS)))`

#### New Parking slot filter
You can create new parking slot filters, permitting new rules for allocating slots.
For example, if you want a slot that is available for electric cars, regardless of their charging rate, you could implement it as:
```java

public class ElectricCarFilter implements ParkingFilter {
        @Override
        public boolean canPark(Car car) {
            return car.getCarType() == CarType.ELECTRIC_20KW | car.getCarType() == CarType.ELECTRIC_50KW;
        }
}
```
You then could use when defining slots with ``new ParkingSlot(new ElectricCarFilter())``
## Assumptions
* The implementation is in memory
* This library is not meant to be used in a distributed environment (ex: as a webservice backend) 
* As a lot of parking problems, this is meant to be thread safe. As a result park() and free() are synchronized and the occupied boolean is volatile
* We need to use little to no dependencies as we are building a library
## Design points 
### Dependencies
Since we are building a library, every runtime dependency we add is a dependency our user will have to include (either directly, or in our jar if we shade it). 
When providing a library, having the minimal number of dependencies is a desirable feature as we don't force our users to include them.
I built this library using only dependencies for testing and no dependency for the final jar.
### Money
The money has euros as a fixed currency. It uses BigDecimal to store it's amount.
The choice of BigDecimal explain itself by the need for precision when dealing with money.
Nobody wants to lose money because an addition or a multiplication was done using floats and the amounts are false.
Since I wanted to minimize dependencies, I had two choices either use long and store the value as cents or use BigDecimal.
I chose BigDecimal as it does not have the risk of forgetting to scale the price in cents, which would result in errors like under or over charging.
### Extensibility
We provide way to extend both the pricing strategy (via PricingStrategy) and the way parking slots are attributed (via ParkingFilter).
### Optional usage
In this library we chose to use Optional as the return types of methods that can fail. 
The reason for using Optional is because the method might fail and this is normal for the domain.
Optional permits us to communicate the fact that there is no result without returning a null or throwing an Exception.
Returning a null gives us a risk for unexpected NullPointerException as users might not check for null and nothing reminds them of the possibility.
Throwing an exception forces the user to handle it or declare it. It makes it an exceptional case that is less elegant to handle and does not map well to our domain.
Optional is a way to strongly suggest users to check if we did return an object while keeping a more elegant API and communicating that failures may happen and are normal.
## Requirements
You need java 1.8. To import this lib either build it or add it to your maven/gradle configuration