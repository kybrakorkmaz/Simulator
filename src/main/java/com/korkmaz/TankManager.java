package com.korkmaz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TankManager implements TankRepository{
    private final List<ExternalTank> tanks = new ArrayList<>();

    Logger logger;
    TankManager() {
        this.logger = new ConsoleLogger();
    }
    @Override
    public ExternalTank getTankById(int tankId){
        for(ExternalTank t:tanks){
            if(t.getTankId()==tankId){return t;}
        }
        return null;
    }
    @Override
    public List<ExternalTank> getTanks(){
        return tanks;
    }
    // Repository internal usage
    void addTank(ExternalTank tank){
        tanks.add(tank);
    }
    void removeTank(ExternalTank tank){
        tanks.remove(tank);
    }
    @Override
    public List<ExternalTank> getAvailableTanks(List<ExternalTank> connectedTanks){
        //Filter usable tanks (valve open & has fuel)
        logger.info("Searching for available tanks");
        Iterator<ExternalTank> iterator = connectedTanks.iterator();
        List<ExternalTank> availableTanks = new ArrayList<>();
        while(iterator.hasNext()){
            ExternalTank tank = iterator.next();
            if(tank.canProvideFuel())availableTanks.add(tank);
        }
        if(availableTanks.isEmpty()) {
            logger.info("No available tanks");
        }else{
            logger.info("Found available tanks");
            listTanks(availableTanks);
        }
        return availableTanks;
    }
    @Override
    public void listTanks(List<ExternalTank> connectedTanks){
        for(ExternalTank t:connectedTanks){
           logger.info("Available tank ID: " + t.getTankId()+ "is valve open "+ t.isValveOpen());
        }
    }
    @Override
    /**
     * @param tankId
     */
    public void printTankInfo(int tankId){
        ExternalTank t = getTankById(tankId);
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
}
