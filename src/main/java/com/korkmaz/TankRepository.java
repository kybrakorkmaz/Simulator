package com.korkmaz;

import java.util.List;

public interface TankRepository {
    ExternalTank getTankById(int id);
    List<ExternalTank> getTanks();
}
