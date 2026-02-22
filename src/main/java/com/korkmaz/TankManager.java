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

}
