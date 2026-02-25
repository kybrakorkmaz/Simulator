package com.korkmaz;

public interface Subject {
    void notify(Message m);
    void attach(SimulationObserver observer);
}
