package com.korkmaz;

public class HeatSystem {

    private int heat =  20;

    public int getHeat() {
        return heat;
    }
    public void heatUp() {
        heat++;
    }
    /**
     * while engine is stopped engine cools down to 20C (1 sec = 1C)
     */
    public void coolDown() {
        if(heat >20){
            heat -= 1;
        }
    }

    /**
     * while engine running idle engine cools down or heats up to 90C (1 sec = 1C)
     */
    public void updateHeatOnIdleMode(boolean idle){
        if(!idle) return;
        if(heat < 90){
            heat += 1;
        }else if(heat > 90){
            heat -= 1;
        }
    }

    /**
     * while full throttle engine heats up (no max limit) (1sec = 5C)
     */
    public void updateHeatOnFullThrottleMode(){
        heat += 5;
    }

    public void reset() {
        heat=20;
    }
}
