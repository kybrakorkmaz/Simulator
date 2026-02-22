package com.korkmaz;

public class TankService {
    private final TankManager tankManager;
    private final Logger  logger;
    private int nextTankID = 1;

    public TankService(TankManager tankManager,
                       Logger logger) {
        this.tankManager = tankManager;
        this.logger = logger;
    }

    /**
     * @param capacity
     * create new external tank with wanted capacity
     * broken and valve properties must be false
     */
    public void addFuelTank(double capacity){
        ExternalTank t =new ExternalTank(nextTankID++,capacity);
        t.closeValve();
        tankManager.addTank(t);
        logger.info("New fuel tank added");
        printTankInfo(t.getTankId());
    }
    /**
     * @param tankId       created tank id from tank list
     * @param fuelQuantity to fill created tanks
     *                     fill_tank function fills tanks based on their id with wanted fuel quantity
     */
    public void fillTank(int tankId, double fuelQuantity){
        logger.info("Filling tank " + tankId + " with fuel " + fuelQuantity);
        // get tank object by its id
        ExternalTank t = tankManager.getTankById(tankId);
        // if tank object is null end the function
        if(t==null){ return;}
        //fill the tank with wanted fuel quantity if wanted quantity exceed its capacity
        // fill it with its capacity
        System.out.println("Before Filling ");
        printTankInfo(t.getTankId());
        t.addFuel(fuelQuantity);
        System.out.println("After Filling ");
        printTankInfo(t.getTankId());
    }
    public void removeFuelTank(int tankId){
        ExternalTank t = tankManager.getTankById(tankId);
        if(t != null && !t.isValveOpen()){
            tankManager.removeTank(t);
            logger.info("Removed tank id: "+t.getTankId());
        }
    }
    public void openValve(int tankId){
        ExternalTank t = tankManager.getTankById(tankId);
        if(t != null){
            t.openValve();
            logger.info("Valve opened");
            printTankInfo(t.getTankId());
        }
    }
    public void closeValve(int tankId){
        ExternalTank t = tankManager.getTankById(tankId);
        if(t != null){
            t.closeValve();
            logger.info("Valve closed");
            printTankInfo(t.getTankId());
        }
    }
    public void listFuelTanks(){
        for(ExternalTank t : tankManager.getTanks()){
            logger.info("Fuel tank " + t.getTankId());
        }
    }

    /**
     * @param tankId
     */
    public void printTankInfo(int tankId){
        ExternalTank t = tankManager.getTankById(tankId);
        if(t ==null){ return;}
        String info =(
                "Tank id: "+ t.getTankId()+
                " Tank capacity: "+ t.getCapacity()+
                " Tank fuel quantity: "+ t.getFuelQuantity()+
                " Valve is open: "+ t.isValveOpen()+
                " Broken: "+ t.isBroken()
        );
        logger.info(info);
    }

    public void printFuelTankCount(){
        int count = tankManager.getTanks().size();
        logger.info("Fuel tank count: " + count);
    }

}
