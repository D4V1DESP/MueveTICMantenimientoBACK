package iso.muevetic.entities;


public class Scooter extends GenericVehicle {
	
	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public void setType(int type) {
		super.setType(3);
	}

}
