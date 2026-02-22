package com.korkmaz;

// Composition + Observer
public class Valve implements SimulationObserver{

    private boolean open;

    public void open(){
        this.open = true;
    }

    public void close(){
        this.open = false;
    }

    public boolean isOpen(){
        return this.open;
    }


    @Override
    public void onSimulationStopped(){
        //todo add number next to valve tank1 2 3...
        System.out.println("Valve simulation stopped");
    }
}
