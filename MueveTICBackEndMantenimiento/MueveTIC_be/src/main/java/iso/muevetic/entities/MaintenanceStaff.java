package iso.muevetic.entities;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serializer.AdditionalDataManager;

import iso.muevetic.enums.UserRole;


public class MaintenanceStaff extends GenericUser {

	private String city;
	private String drivingLicense;
	private Integer experience;
	
	private static final String DRIVING_LICENSE_EXTENSION = "CarnetConducir";
	private static final String EXPERIENCE_EXTENSION = "Experiencia";

	public MaintenanceStaff(OAuth2User user) {
		super(user, UserRole.MAINTENANCE.name());

		this.city = user.getAttribute("city");
		this.drivingLicense = user.getAttribute(EXTENSION_PREFIX + DRIVING_LICENSE_EXTENSION);

		Long holder = (Long) user.getAttribute(EXTENSION_PREFIX + EXPERIENCE_EXTENSION);
		this.experience = (holder != null) ? holder.intValue() : null;
	}

	public MaintenanceStaff(User user) {
		super(user, UserRole.MAINTENANCE.name());
		AdditionalDataManager extensions = user.additionalDataManager();
		JsonElement experiencej = extensions.get(EXTENSION_PREFIX + AZURE_EXTENSION + EXPERIENCE_EXTENSION);
		JsonElement drivingLicensej = extensions.get(EXTENSION_PREFIX + AZURE_EXTENSION + DRIVING_LICENSE_EXTENSION);

		if (experiencej != null)
			this.experience = experiencej.getAsInt();
		
		if (drivingLicensej != null)
			this.drivingLicense = drivingLicensej.getAsString();
		
		this.city = user.city;
	}

	public MaintenanceStaff() {}

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public  String getDrivingLicense() {
		return drivingLicense;
	}
	
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	public Integer getExperience() {
		return experience;
	}
	
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	@Override
	public com.microsoft.graph.models.User toAzureUser() {
		com.microsoft.graph.models.User user = super.toAzureUser();

		AdditionalDataManager dataManager = user.additionalDataManager();

		user.city = this.city;
		
		if (this.experience != null)
			dataManager.put(EXTENSION_PREFIX + AZURE_EXTENSION + EXPERIENCE_EXTENSION,  new JsonPrimitive(this.experience));

		if (this.drivingLicense != null)
			dataManager.put(EXTENSION_PREFIX + AZURE_EXTENSION + DRIVING_LICENSE_EXTENSION, new JsonPrimitive(this.drivingLicense));

		return user;
	}
	
	 @Override
	 public boolean equals(Object object) {
		 return super.equals(object);
	 }

	 @Override
	 public int hashCode() {
		 return super.hashCode();
	 }
	
}
