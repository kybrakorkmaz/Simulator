package com.korkmaz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.korkmaz.Constants.MIN_INTERNAL_TANK_FUEL_QUANTITY;

public class RandomFuelAbsorptionStrategy implements  FuelAbsorptionStrategy {
    Logger logger = new ConsoleLogger();
    private final Random random = new Random();
    @Override
    public void absorbFuel(InternalTank internalTank, List<ExternalTank> availableTanks) {
        while(internalTank.isLow()) {
            logger.info("RandomFuelAbsorptionStrategy: Internal tank fuel quantity is under 20");
            ExternalTank selected = availableTanks.get(random.nextInt(availableTanks.size()));
            logger.info("Selected tank id: " + selected.getTankId());
            logger.info("Selected fuel quantity: " + selected.getFuelQuantity());
            logger.info("Selected tank's valve: " + selected.isValveOpen());

            double needed = internalTank.getCapacity() - internalTank.getFuelQuantity();
            logger.info("Needed: " + needed);
            double available = selected.getFuelQuantity();
            logger.info("Available: " + available);
            double transfer = Math.min(available, needed);
            logger.info("Transfer: " + transfer);
            selected.consumeFuel(transfer);
            logger.info("After consumed fuel tank's fuel quantity: " + selected.getFuelQuantity());
            internalTank.addFuel(transfer);;
            logger.info("After internal tank filled by fuel tank: " + internalTank.getFuelQuantity());
        }

    }
}
