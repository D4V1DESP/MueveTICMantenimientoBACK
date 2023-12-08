package iso.muevetic.entities;


public class Bike extends GenericVehicle {
	
	private boolean includesHelmet;

	public boolean getIncludesHelmet() {
		return includesHelmet;
	}

	public void setIncludesHelmet(boolean includesHelmet) {
		this.includesHelmet = includesHelmet;
	}
	
	@Override
	public void setType(int type) {
		super.setType(2);
	}

}
