package com.korkmaz;

import java.util.List;

public interface FuelAbsorptionStrategy {
    void absorbFuel(InternalTank internalTank, List<ExternalTank> connectedTanks);
}
