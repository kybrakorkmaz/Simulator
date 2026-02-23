package com.korkmaz;

public class SimulationManager implements SimulationObserver{

    private final Engine engine;
    private final Logger logger;
    private final TankRepository tankRepository;
    private final TankService tankService;

    public SimulationManager(){
        TankManager tankManager = new TankManager();
        this.logger = new ConsoleLogger();
        this.tankRepository = tankManager;
        this.tankService = new TankService(tankManager, logger);
        Engine.initialize(tankRepository,
                new RandomFuelAbsorptionStrategy(),
                new HealthSystem(),
                new HeatSystem(),
                logger);
        this.engine = Engine.getInstance();
    }

    /*private final List<SimulationObserver> observers  = new ArrayList<>();

    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }

    public void stopSimulation(){
        for(SimulationObserver observer : observers){
            observer.onSimulationStopped();
        }
    }*/
    public TankService getTankService() {
        return tankService;
    }
    /* -------------------- ENGINE CONTROL -------------------- */
    public void start(){
        engine.startEngine();
    }
    public void stop(){
        engine.stopEngine();
    }
    public void connectFuelTankToEngine(int tankId){
        engine.connectFuelTankToEngine(tankId);
    }
    public void disconnectFuelTankFromEngine(int tankId){
        engine.disconnectFuelTankFromEngine(tankId);
    }
    public void listConnectedTanks(){
        engine.listConnectedTanks();
    }
    public void getTotalFuelQuantity(){
        engine.getTotalFuelQuantity();
    }

    public void getTotalConsumedFuelQuantity(){
        engine.getTotalConsumedFuelQuantity();
    }

    public void wait(int seconds){
        engine.wait(seconds);
    }
    public void nextStep(){
        engine.runEngine();
    }
    public void fullThrottle(int seconds){
        engine.fullThrottle(seconds);
    }
    public void repairEngine(){
        engine.repairEngine();
    }
    public void changeEngineBlock(){
        engine.changeEngineBlock();
    }
    /* -------------------- FUEL TANK CONTROL -------------------- */
    public void addFuelTank(int capacity){
        tankService.addFuelTank(capacity);
    }
    public void listFuelTanks(){tankService.listFuelTanks();}
    public void printFuelTankCount(){tankService.printFuelTankCount();}
    public void removeFuelTank( int tankId){tankService.removeFuelTank(tankId);}
    public void openValve(int tankId){tankService.openValve(tankId);}
    public void closeValve(int tankId){tankService.closeValve(tankId);}
    public void printTankInfo(int tankId){tankRepository.printTankInfo(tankId);}
    public void fillTank(int tankId, double fuelQuantity){tankService.fillTank(tankId, fuelQuantity);}
    @Override
    public void onSimulationStopped() {}
}
