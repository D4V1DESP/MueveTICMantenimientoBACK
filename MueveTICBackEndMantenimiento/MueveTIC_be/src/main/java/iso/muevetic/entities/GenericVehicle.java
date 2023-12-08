package iso.muevetic.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import iso.muevetic.enums.VehicleState;


@Document(value = "vehicles")
public class GenericVehicle {

	@Id
	private String licensePlate;
	protected int type;
	private String model;
	private int battery;
	private String location;
	private boolean deactivated;
	private VehicleState status = VehicleState.DISPONIBLE;

	public String getLicensePlate() {
		return licensePlate;
	}
	
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public int getBattery() {
		return battery;
	}
	
	public void setBattery(int battery) {
		this.battery = battery;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean getDeactivated() {
		return deactivated;
	}

	public void setDeactivated(boolean deactivated) {
		this.deactivated = deactivated;
	}
	
	public VehicleState getStatus() {
		return status;
	}

	public void setStatus(VehicleState status) {
		this.status = status;
	}

}
