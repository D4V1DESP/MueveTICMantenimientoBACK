package iso.muevetic.domain;

import jakarta.validation.constraints.NotNull;

public class BikeModel extends GenericVehicleModel {

	@NotNull
	private boolean includesHelmet;

	public boolean getIncludesHelmet() {
		return includesHelmet;
	}
	
}
