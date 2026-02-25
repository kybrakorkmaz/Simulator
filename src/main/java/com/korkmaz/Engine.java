package com.korkmaz;

import java.util.ArrayList;
import java.util.List;

import static com.korkmaz.Constants.*;

// todo heat system does not work
class Engine implements SimulationObserver {

    private final Logger logger;

    private static Engine instance; //There is only one engine

    private final double fuelPerSecond=5.5; //Engine absorbs fuel quantity in per second
    private boolean status; //Engine's running status, true is running
    private final List<ExternalTank> connectedTanks; // Engine's connected external tank list

    private final InternalTank internalTank;
    private final TankRepository tankRepository;
    private final HealthSystem healthSystem;
    private final HeatSystem heatSystem;
    private final FuelAbsorptionStrategy strategy;


    /* -------------------- CONSTRUCTOR -------------------- */
    // Engine Constructor (Controlled Singleton)
    private Engine(TankRepository tankRepository,
                   FuelAbsorptionStrategy strategy,
                   HealthSystem healthSystem,
                   HeatSystem heatSystem,
                   Logger logger) {

        this.tankRepository = tankRepository;
        this.strategy= strategy;
        this.healthSystem= healthSystem;
        this.heatSystem= heatSystem;
        this.logger = logger;

        this.connectedTanks=new ArrayList<>();
        this.internalTank= new InternalTank();
        this.status = false;
    }
    /* -------------------- SINGLETON INIT -------------------- */
    // Singleton Initialize
    public static void initialize(TankRepository tankRepository,
                                  FuelAbsorptionStrategy strategy,
                                  HealthSystem healthSystem,
                                  HeatSystem heatSystem,
                                  Logger logger) {
        if(instance !=null){
            throw new IllegalStateException("Engine already initialized");
        }
        instance = new Engine(tankRepository, strategy, healthSystem, heatSystem, logger);
    }
    public static Engine getInstance(){
        if(instance==null){
            throw new IllegalStateException("Engine has not been initialized");
        }
        return instance;
    }

    /* -------------------- ENGINE CONTROL -------------------- */
    /**
     * Engine needs minimum one connected tank to start
     */
    public void startEngine(){
        logger.info("Starting Engine");
        if(connectedTanks.isEmpty()){
            logger.info("Engine cannot start without external tank");
            return;
        }
        if(healthSystem.getHealth() <= MIN_HEALTH){
            logger.warn("Health is under 20 "+ healthSystem.getHealth());
            return;
        }
        this.status=true;
        logger.info("Engine is running, connected tank count: "+connectedTanks.size());
    }
    /***
     * Engine stops when there is no fuel in connected fuel tank
     * Engine stops when a fuel tank disconnected from engine
     * When engine stopped it can cool down
     * */
    public void stopEngine(){
        logger.info("Engine is stopped");
        this.status=false;
        if(healthSystem.isDead()){
            changeEngineBlock();
            return;
        }
        heatSystem.coolDown();
    }
    /* -------------------- TANK CONNECTION -------------------- */
    public void connectFuelTankToEngine(int tankId){
        logger.info("Target tank must have fuel to connect, connecting tank: " + tankId);
        tankRepository.printTankInfo(tankId);
        ExternalTank fuelTank=tankRepository.getTankById(tankId);
        if(fuelTank==null){
            logger.warn("Fuel tank not found, tank id: " + tankId);
            return;
        }
        //check if target fuel tank has fuel
        if(fuelTank.getFuelQuantity()<=0){
            logger.warn("No enough fuel in tank ID: "+tankId);
            stopEngine();
            return;
        }
        //check if list has the fuel tank
        if(!connectedTanks.contains(fuelTank)){
            this.connectedTanks.add(fuelTank);
            logger.info("New connected tank's tankId: "+tankId);
        }else{
            logger.warn("This tank already connected, tank ID: "+tankId);
        }
    }
    public void disconnectFuelTankFromEngine(int tankId){
        // check if removed tank in the connected tank list
        ExternalTank fuelTank=tankRepository.getTankById(tankId);
        //if connected tank list doesn't have the fuel tank, inform the system
        // but engine continues running
        if(fuelTank==null){
            logger.warn("Fuel tank not found, tank id: " + tankId);
            return;
        }
        connectedTanks.remove(fuelTank);
        logger.info("Tank " +tankId+" disconnected from connected tank list");
        stopEngine();
    }
    /* -------------------- ENGINE CYCLE -------------------- */
    /**
     * Engine consumes fuel per second while it runs after every command is executed
     * Execution of every command takes 1 second
     * If full throttle is active consuming fuel will be increased by 5 times in per seconds
     */
    public void runEngine(){
        // To run engine status must be true
        // if not engine health system can be repaired
        if(!status){
            return;
        }
        if(healthSystem.getHealth() <= 0){
            logger.warn("Engine health is 0, needs to be changed!");
            stopEngine();
            return;
        }
        logger.info("Running Engine and consuming fuel per second");
        // absorb fuel if needed
        absorbFuel();
        if(internalTank.getFuelQuantity()<=0){
            logger.info("No fuel in internal tank, engine will be stopped");
            stopEngine();
            return;
        }
        // consume fuel per second
        logger.info("Internal tank fuel quantity before consuming by engine: "+ internalTank.getFuelQuantity());
        internalTank.consumeFuel(fuelPerSecond);
        logger.info("Internal tank fuel quantity after consuming by engine: "+ internalTank.getFuelQuantity());
        // Heat management on idle mode
        heatManagement();
        // Health management
        healthManagement();
    }
    private void absorbFuel(){
        if(internalTank.getFuelQuantity()<= MIN_INTERNAL_TANK_FUEL_QUANTITY){
            logger.info("Internal tank fuel quantity is under 20");
            List<ExternalTank> available = getAvailableTanks();
            if(available.isEmpty()){
                logger.warn("No available tank found");
                return;
            }
            strategy.absorbFuel(internalTank, available);
        }
    }
    private void heatManagement(){
        heatSystem.heatUp();
        heatSystem.updateHeatOnIdleMode(isIdle());
        logger.info("Heat changed: "+ heatSystem.getHeat());
    }
    private void healthManagement(){
        if(heatSystem.getHeat()>MAX_HEAT){
            logger.info("Health exceeded over "+ MAX_HEAT +"\n engine gets damage");
            healthSystem.damage();
        }
        logger.info("Health " + healthSystem.getHealth() +"/100.0");
    }
    /**
     * @param seconds
     * wait command consumes fuel along given seconds while engine runs
     */
    public void wait(int seconds){
        // To wait engine must be running
        if(!status){return;}
        logger.info("Engine waiting and consuming fuel...");
        double consumedFuel = fuelPerSecond*seconds;
        internalTank.consumeFuel(consumedFuel);
        logger.info("Internal tank fuel quantity after waiting: "+ internalTank.getFuelQuantity());
    }

    /**
     * @param seconds
     *  while full throttle engine heats up (no max limit) (1sec = 5C)
     *  also damages the engine if the engine is cooler than 90C (1 percent per seconds),
     *  full throttle also consumes 5 times more than fuel consumption of idle mode.
     */
    public void fullThrottle(int seconds){
        if(!status){return;}
        logger.info("Engine full throttle is active in "+ seconds+" seconds");
        while(seconds>0 && internalTank.getFuelQuantity()>0){
            logger.info("Full throttle");
            if(healthSystem.getHealth() <= 0){
                logger.warn("Engine health is 0, needs to be changed!");
                stopEngine();
                return;
            }
            heatSystem.updateHeatOnFullThrottleMode();
            if(heatSystem.getHeat()<FULL_THROTTLE_MIN_HEAT){
                logger.info("heat is under "+ FULL_THROTTLE_MIN_HEAT+ ", health is damaged.");
                healthSystem.damage();
                logger.info("health "+ healthSystem.getHealth()+"/100.0");
            }
            if(getAvailableTanks().isEmpty()){
                logger.info("No available tanks, consumes fuel from internal tank");
                break;
            }else{
                strategy.absorbFuel(internalTank, getAvailableTanks());
            }
            logger.info("Internal tank fuel quantity before full throttle: "+ internalTank.getFuelQuantity());
            internalTank.consumeFuel(fuelPerSecond*5);
            logger.info("Internal tank fuel quantity after full throttle: "+ internalTank.getFuelQuantity());
            seconds--;
        }
        logger.info("Engine full throttle completed");
    }

    /**
     * @return if no available tanks found return null, otherwise return available tanks
     * and engine's idle state is set to false if available tanks found (not on idle mode)
     */
    private List<ExternalTank> getAvailableTanks(){
        List<ExternalTank>availableTanks=tankRepository.getAvailableTanks(connectedTanks);
        if(availableTanks.isEmpty()){
            logger.info("No available tanks in connected tank list");
            return availableTanks;
        }
        logger.info("Available tanks listed");
        return availableTanks;
    }
    private boolean isIdle() {
        return tankRepository
                .getAvailableTanks(connectedTanks)
                .isEmpty();
    }
    public void listConnectedTanks(){
        logger.info("List Connected Tanks:");
        for(ExternalTank t:connectedTanks){
            logger.info("Connected Tank ID: "+t.getTankId());
        }
    }
    public void changeEngineBlock(){
        if(status){
            logger.warn("Engine block can be changed only while not running, health: "+ healthSystem.getHealth());
            return;
        }
        if(healthSystem.isDead()){
            logger.info("Engine block has been repaired");
            healthSystem.reset();
            heatSystem.reset();
            return;
        }
        logger.info("Engine can be changed if its health is equal to 0, current health: "+ healthSystem.getHealth());
    }
    public void repairEngine(){
        if(status){
            logger.warn("Engine cannot be repaired while running");
            return;
        }
        healthSystem.repair();
        logger.info("Engine repaired");
    }
    public double getTotalFuelQuantity(){
        return internalTank.getFuelQuantity();
    }

    public double getTotalConsumedFuelQuantity(){
        return internalTank.getConsumedTotalFuel();
    }

    @Override
    public void update(Message m) {
        logger.info("Engine: "+ m.getMessageContent());
    }

}
