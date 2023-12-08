package iso.muevetic.domain;


public class MaintenanceStaffModel extends GenericUserModel {

	private String drivingLicense;
	private int experience;

	public int getExperience() {
		return experience;
	}
	
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	public String getDrivingLicense() {
		return drivingLicense;
	}
	
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

}
