package com.korkmaz;

public abstract class FuelTank {

    private final double capacity;
    private  double fuelQuantity;

    private double consumedTotalFuel;

    protected FuelTank(double capacity) {
        this.capacity = capacity;
        this.fuelQuantity = 0;
    }

    public double getCapacity() {  return capacity; }
    public double getFuelQuantity() {
        return fuelQuantity;
    }

    public double getConsumedTotalFuel() {
        return consumedTotalFuel;
    }

    public void addFuel(double fuelQuantity) {
        if(fuelQuantity <= 0) return;
        double newValue = this.fuelQuantity + fuelQuantity;
        // Fuel tank cannot exceed its capacity with added fuel
        this.fuelQuantity = Math.min(newValue, this.capacity);
    }

    public void consumeFuel(double fuelQuantity) {
        if (fuelQuantity <= 0) return;
        // fuel quantity cannot be negative
        // absorbed fuel can be whole fuel quantity at most
        double consumed = Math.min(fuelQuantity, this.fuelQuantity);
        this.fuelQuantity -= consumed;
        this.consumedTotalFuel += consumed;
    }

    public boolean isEmpty(){
        return this.fuelQuantity == 0;
    }
}
