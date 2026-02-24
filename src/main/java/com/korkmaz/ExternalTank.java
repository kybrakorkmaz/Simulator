package com.korkmaz;

//todo external tank manager
public class ExternalTank extends FuelTank implements SimulationObserver{
    private final int tankId;
    private boolean broken;
    private final Valve valve;

    public ExternalTank(int tankID, double capacity){
        super(capacity);
        this.tankId = tankID;
        this.valve = new Valve();
        this.broken = false;
    }

    public int getTankId(){
        return this.tankId;
    }

    public void openValve(){
        this.valve.open();
    }
    public void closeValve(){
        this.valve.close();
    }
    public boolean isValveOpen(){
        return this.valve.isOpen();
    }

    public boolean isBroken(){return this.broken;}

    public void breakTank(){this.broken=true;}
    public void repairTank(){this.broken=false;}

    public boolean canProvideFuel(){
        return !broken && isValveOpen() && isEmpty();
    }
    public boolean isEmpty(){
        return this.getFuelQuantity()>0;
    }
    public boolean canReceiveFuel(double amount){
       return this.getFuelQuantity()+amount <= this.getCapacity();
    }
    public void transferFrom(ExternalTank tank2, ExternalTank tank3, Logger logger){
        if(tank2 == null || tank3 == null){
            logger.warn("External tank cannot found");
            return;
        }
        if(!tank2.canProvideFuel() && !tank2.canProvideFuel()){
            logger.warn("External tank cannot provide fuel");
        }
        double totalTransfer = tank2.getFuelQuantity() + tank3.getFuelQuantity();
        if(!canReceiveFuel(totalTransfer)){
            logger.warn("Transferred fuel exceeds capacity");
            return;
        }
        // Drain sources
        double fuel2 = tank2.getFuelQuantity();
        double fuel3 = tank3.getFuelQuantity();

        tank2.consumeFuel(fuel2);
        tank3.consumeFuel(fuel3);

        // Fill target
        this.addFuel(totalTransfer);

        logger.info("Fuel successfully transferred");
    }
    @Override
    public void onSimulationStopped(){
        System.out.println("Tank " + tankId + ": Simulation stopped");
        valve.onSimulationStopped();
    }

}
