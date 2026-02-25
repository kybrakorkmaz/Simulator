package com.korkmaz;

// Composition + Observer
public class Valve implements SimulationObserver{

    private boolean open;
    private final int valveId;
    public Valve(int valveId){
        this.valveId = valveId;
    }
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
    public void update(Message m){
        Logger logger = new ConsoleLogger();
        logger.info("Valve "+ this.valveId+ ":" + m.getMessageContent());
    }
}
