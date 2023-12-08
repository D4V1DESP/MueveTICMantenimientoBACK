package iso.muevetic.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleUpdateModel {
	
	@NotBlank
	private String model;
	@Min(value = 0)
	@Max(value = 100)
	@NotNull
	private int battery;
	@NotBlank
	private String location;
	@NotNull
	private int type;
	private int seats;
	private boolean includesHelmet;
	private String color;
	
	public String getModel() {
		return model;
	}
	
	public int getBattery() {
		return battery;
	}
	
	public String getLocation() {
		return location;
	}
	
	public int getType() {
		return type;
	}
	
	public int getSeats() {
		return seats;
	}
	
	public boolean getIncludesHelmet() {
		return includesHelmet;
	}
	
	public String getColor() {
		return color;
	}

}
