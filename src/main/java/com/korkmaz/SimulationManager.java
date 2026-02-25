package com.korkmaz;

import java.util.ArrayList;
import java.util.List;

public class SimulationManager implements Subject{

    private final Engine engine;
    private final Logger logger;
    private final TankRepository tankRepository;
    private final TankService tankService;

    List<SimulationObserver> observers = new ArrayList<>();

    public SimulationManager(){
        TankManager tankManager = new TankManager(this);
        this.logger = new ConsoleLogger();
        this.tankRepository = tankManager;
        this.tankService = new TankService(tankManager, logger);
        Engine.initialize(tankRepository,
                new RandomFuelAbsorptionStrategy(),
                new HealthSystem(),
                new HeatSystem(),
                logger);
        this.engine = Engine.getInstance();
        attach(engine); // engine added as observer
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
    public void totalFuelQuantity(){
        logger.info("Total engine fuel quantity "+engine.getTotalFuelQuantity());
    }

    public void totalConsumedFuelQuantity(){
        logger.info("Total engine consumed fuel "+engine.getTotalConsumedFuelQuantity());
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
    public void sumOperator(int tankId, int tankId2, int tankId3){tankService.sumOperator(tankId, tankId2, tankId3);}

    public void stopSimulation(){
        notify(new Message("Simulation stopped"));
    }
    /* -------------------- OBSERVERS -------------------- */
    @Override
    public void notify(Message m){
        for(SimulationObserver observer : observers){
            observer.update(m);
        }
    }
    @Override
    public void attach(SimulationObserver observer){
        observers.add(observer);
    }
}
