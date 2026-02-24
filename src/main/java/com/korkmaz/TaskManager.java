package com.korkmaz;

import java.util.List;

public class TaskManager {

    private final List<String> taskList;
    private final SimulationManager simulationManager;
    private final Logger logger = new ConsoleLogger();
    public TaskManager(List<String> commandList, SimulationManager simulationManager){
        this.taskList = commandList;
        this.simulationManager = simulationManager;
    }

    private void executeTask(String fnc){
        switch (fnc){
            case "startEngine":
                simulationManager.start();
                break;
            case "listFuelTanks":
                simulationManager.listFuelTanks();
                break;
            case "printFuelTankCount":
                simulationManager.printFuelTankCount();
                break;
            case "listConnectedTanks":
                simulationManager.listConnectedTanks();
                break;
            case "printTotalFuelQuantity":
                simulationManager.totalFuelQuantity();
                break;
            case "printTotalConsumedFuelQuantity":
                simulationManager.totalConsumedFuelQuantity();
                break;
            case "stopEngine":
                simulationManager.stop();
                break;
            case "repairEngine":
                simulationManager.repairEngine();
                break;
            case "changeEngineBlock":
                simulationManager.changeEngineBlock();
                break;
            case "stopSimulation":
                logger.info("Stopping Simulation");
                System.exit(0);
                break;
            default:
                logger.error("Invalid command " + fnc);
                System.exit(0);
                break;
        }
    }
    private void executeTask(String fnc, int num){
        switch (fnc){
            case "addFuelTank":
                simulationManager.addFuelTank(num);
                break;
            case "connectFuelTankToEngine":
                simulationManager.connectFuelTankToEngine(num);
                break;
            case "removeFuelTank":
                simulationManager.removeFuelTank(num);
                break;
            case "disconnectFuelTankFromEngine":
                simulationManager.disconnectFuelTankFromEngine(num);
                break;
            case "openValve":
                simulationManager.openValve(num);
                break;
            case "closeValve":
                simulationManager.closeValve(num);
                break;
            case "wait":
                simulationManager.wait(num);
                break;
            case "printTankInfo":
                simulationManager.printTankInfo(num);
                break;
            case "fullThrottle":
                simulationManager.fullThrottle(num);
                break;
            default:
                logger.error("Invalid command " + fnc);
                System.exit(0);
                break;
        }
    }
    private void executeTask(String fnc, int num, double num2){
        if (fnc.equals("fillTank")) {
            simulationManager.fillTank(num, num2);
        } else {
            logger.error("Invalid command " + fnc);
            System.exit(0);
        }
    }
    private void executeTask(String fnc, int num, int num2, int num3){
        if(fnc.equals("sumOperator")) {
            simulationManager.sumOperator(num, num2, num3);
        }else{
            logger.error("Invalid command " + fnc);
            System.exit(0);
        }
    }
    public void sendTasks(){
        for (String task : taskList) {
            String[] cmd = task.split("\\s+");
            // Execute command
            if(cmd.length == 1){
                executeTask(cmd[0]);
            }
            else if(cmd.length == 2){
                executeTask(cmd[0], Integer.parseInt(cmd[1]));
            }
            else if(cmd.length == 3){
                executeTask(cmd[0], Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
            }else if(cmd.length == 4){
                executeTask(cmd[0], Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
            } else{
                logger.error("Invalid command type");
                break;
            }
            // if engine running, consumes fuel per second
            simulationManager.nextStep();
        }
    }
}
