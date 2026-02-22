package com.korkmaz;

public class InternalTank extends FuelTank{
    private static final double DEFAULT_CAPACITY =55.0;
    private static final double LOW_THRESHOLD = 20.0;
    public InternalTank(){
        super(DEFAULT_CAPACITY);
    }
    public boolean isLow(){
        return getFuelQuantity() <= LOW_THRESHOLD;
    }
}
