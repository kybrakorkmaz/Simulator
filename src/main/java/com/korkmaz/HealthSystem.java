package com.korkmaz;

import static com.korkmaz.Constants.HEALTH_DAMAGE_PERCENTAGE;

public class HealthSystem {

    private double health = 100;

    public double getHealth() {
        return health;
    }

    public void damage() {
        this.health -= health*HEALTH_DAMAGE_PERCENTAGE;
        if(this.health < 0){
            this.health = 0;
        }
    }
    public void repair(){
        // Engine health can be repaired only while not running
        if(!isDead()){
            System.out.println("Engine repairing...");
            this.health= 100;
        }
    }
    public void reset(){
        this.health= 100;
    }
    public boolean isDead(){
        return health == 0;
    }
}

