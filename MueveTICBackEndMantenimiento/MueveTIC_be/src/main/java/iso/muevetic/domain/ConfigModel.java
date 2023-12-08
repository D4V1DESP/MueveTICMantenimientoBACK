package iso.muevetic.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ConfigModel {

	@Max(value = 100)
    @Min(value = 0)
	private int minBattery;
    @Min(value = 0)
    private int maxMaintenance;
    @Min(value = 0)
    private int ridePrice;
    @Max(value = 100)
    @Min(value = 0)
    private int batteryPerRide;

	public int getMinBattery() {
		return minBattery;
	}

	public int getMaxMaintenance() {
		return maxMaintenance;
	}

	public int getRidePrice() {
		return ridePrice;
	}

	public int getBatteryPerRide() {
		return batteryPerRide;
	}
    
    
}
