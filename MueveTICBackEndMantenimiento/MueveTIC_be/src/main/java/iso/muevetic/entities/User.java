package iso.muevetic.entities;

import java.util.Date;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.microsoft.graph.serializer.AdditionalDataManager;

import iso.muevetic.enums.UserRole;


public class User extends GenericUser {

	private Integer telephone;
	private String drivingLicense;
	private Date birthdate;
	
	private static final String TELEPHONE_EXTENSION = "Telefono";
	private static final String DRIVING_LICENSE_EXTENSION = "CarnetConducir";
	private static final String BIRTHDATE_EXTENSION = "Nacimiento";

	public User() {}

	public User(OAuth2User user) {
		super(user, UserRole.MEMBER.name());
		
		this.drivingLicense = user.getAttribute(EXTENSION_PREFIX + DRIVING_LICENSE_EXTENSION);

		Long holder = (Long) user.getAttribute(EXTENSION_PREFIX + TELEPHONE_EXTENSION);
		this.telephone = (holder != null) ? holder.intValue() : null;
		
		try {
			holder = Long.parseLong(user.getAttribute(EXTENSION_PREFIX + BIRTHDATE_EXTENSION));
			this.birthdate = (holder != null) ? new Date(holder.longValue()) : null;
		} catch (NumberFormatException e) {
			this.birthdate = null;
		}
	}

	public User(com.microsoft.graph.models.User user) {
		super(user, UserRole.MEMBER.name());
		AdditionalDataManager extensions = user.additionalDataManager();
		JsonElement telephonej = extensions.get(EXTENSION_PREFIX + AZURE_EXTENSION + TELEPHONE_EXTENSION);
		JsonElement drivingLicensej = extensions.get(EXTENSION_PREFIX + AZURE_EXTENSION + DRIVING_LICENSE_EXTENSION);
		JsonElement birthdatej = extensions.get(EXTENSION_PREFIX + AZURE_EXTENSION + BIRTHDATE_EXTENSION);
		
		if (telephonej != null)
			this.telephone = telephonej.getAsInt();
		
		if (drivingLicensej != null)
			this.drivingLicense = drivingLicensej.getAsString();
		
		if (birthdatej != null)
			this.birthdate = new Date(birthdatej.getAsLong());
	}

	public Integer getTelephone() {
		return telephone;
	}

	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}	
	
	@Override
	public com.microsoft.graph.models.User toAzureUser() {
		com.microsoft.graph.models.User user = super.toAzureUser();

		AdditionalDataManager dataManager = user.additionalDataManager();

		if (this.telephone != null)
			dataManager.put(EXTENSION_PREFIX + AZURE_EXTENSION + TELEPHONE_EXTENSION,  new JsonPrimitive(this.telephone));

		if (this.drivingLicense != null)
			dataManager.put(EXTENSION_PREFIX + AZURE_EXTENSION + DRIVING_LICENSE_EXTENSION, new JsonPrimitive(this.drivingLicense));

		if (this.birthdate != null)
			dataManager.put(EXTENSION_PREFIX + AZURE_EXTENSION + BIRTHDATE_EXTENSION, new JsonPrimitive("" + this.birthdate.getTime()));

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
