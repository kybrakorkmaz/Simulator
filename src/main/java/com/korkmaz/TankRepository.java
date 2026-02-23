package com.korkmaz;

import java.util.List;

public interface TankRepository {
    ExternalTank getTankById(int id);
    List<ExternalTank> getTanks();
    List<ExternalTank> getAvailableTanks(List<ExternalTank> connectedTanks);
    void printTankInfo(int tankId);
    void listTanks(List<ExternalTank> availableTanks);
}
