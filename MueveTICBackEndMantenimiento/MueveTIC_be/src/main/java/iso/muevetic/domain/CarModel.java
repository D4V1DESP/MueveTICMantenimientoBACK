package iso.muevetic.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CarModel extends GenericVehicleModel {

	@Min(value = 1)
	@Max(value = 5)
	@NotNull
	private int seats;

	public int getSeats() {
		return seats;
	}	
	
}
