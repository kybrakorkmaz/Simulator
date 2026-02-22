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
        return !broken && valve.isOpen() && !isEmpty();
    }

    @Override
    public void onSimulationStopped(){
        System.out.println("Tank " + tankId + ": Simulation stopped");
        valve.onSimulationStopped();
    }

}
