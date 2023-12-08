package iso.muevetic.entities;


public class Car extends GenericVehicle{
	
	private int seats;

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}
	
	@Override
	public void setType(int type) {
		super.setType(1);
	}

}
