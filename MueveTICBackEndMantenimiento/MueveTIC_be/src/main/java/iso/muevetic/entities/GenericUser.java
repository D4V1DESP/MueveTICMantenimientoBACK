package iso.muevetic.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serializer.AdditionalDataManager;

import java.util.ArrayList;


@Document(value = "users")
public class GenericUser {
	
	protected static final String EXTENSION_PREFIX = "extension_";
	protected static final String AZURE_EXTENSION = "005961277dab49a08635542189bac000_";
	private static final String DNI_EXTENSION = "DNI";

	@Id
	private String id;
	private String name;
	private String surname;
	private String dni;
	private String email;
	private boolean deactivated;
	private String role;


	public GenericUser() {}
	
    public GenericUser(User raw, String role) {
		AdditionalDataManager extensions = raw.additionalDataManager();

		JsonElement dnij = extensions.get(EXTENSION_PREFIX + AZURE_EXTENSION + DNI_EXTENSION);
		if (dnij != null)
			this.dni = dnij.getAsString();

    	this.role = role;
    	
    	if (raw.identities != null)
    		this.email = raw.identities.get(0).issuerAssignedId;
    	else if(raw.mail != null)
    		this.email = raw.mail;
    	else
    		this.email = raw.displayName;

    	this.name = raw.givenName;
    	this.surname = raw.surname;
    	this.id = raw.id;
    	this.deactivated = (raw.accountEnabled != null) && !raw.accountEnabled;
    }

    public GenericUser(OAuth2User user, String role) {
    	this.id = user.getName();
		this.dni = user.getAttribute(EXTENSION_PREFIX + DNI_EXTENSION);
		this.name = user.getAttribute("given_name");
		this.surname = user.getAttribute("family_name");
		

		ArrayList<?> arrayList = user.getAttribute("emails");
		
		if (arrayList != null)
			this.email = (String) arrayList.get(0);
		else
			this.email = this.name + '.' + this.surname + "@iso2023.onmicrosoft.com";
		
		this.role = role;
		this.deactivated = false;
    }

	public String getId() {
		return id;
	}

    public String getRole() {
		return role;
	}
    
    public void setRole(String role) {
    	this.role = role;
    }

	public void setEmail(String email) {
		this.email = email;
    }

    public String getEmail() {
    	return email;
    }

	public boolean getDeactivated() {
		return deactivated;
	}

	public void setDeactivated(boolean deactivated) {
		this.deactivated = deactivated;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public com.microsoft.graph.models.User toAzureUser() {
		com.microsoft.graph.models.User user = new com.microsoft.graph.models.User();

		AdditionalDataManager dataManager = user.additionalDataManager();

		if (this.dni != null)
			dataManager.put(EXTENSION_PREFIX + AZURE_EXTENSION + DNI_EXTENSION, new JsonPrimitive(this.dni));

		user.givenName = this.name;
		user.surname = this.surname;
		user.id = this.id;
		user.mailNickname = this.name;
		
		if (this.name != null && this.surname != null)
			user.userPrincipalName = this.name.toLowerCase().trim().replace(' ', '.') + '.' + this.surname.toLowerCase().trim().replace(' ', '.') + "@iso2023.onmicrosoft.com";
		
		user.displayName = this.name + " " + this.surname;

		return user;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || this.getClass() == other.getClass())
			return false;
		return ((GenericUser) other).id.equals(this.id);
	}

	public int hashCode() {
		return super.hashCode();
	}

}
