package iso.muevetic.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GenericVehicleModel {
	
	@NotBlank
	private String licensePlate;
	@NotBlank
	private String model;
	@Min(value = 0)
	@Max(value = 100)
	@NotNull
	private int battery;
	@NotBlank
	private String location;
	
	public String getModel() {
		return model;
	}
	
	public String getLicensePlate() {
		return licensePlate;
	}
	
	public int getBattery() {
		return battery;
	}
	
	public String getLocation() {
		return location;
	}

}
