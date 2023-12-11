package iso.muevetic.entities;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.microsoft.graph.models.User;

import iso.muevetic.enums.UserRole;


public class TelephoneAttention extends GenericUser {

	private String city;

	public TelephoneAttention(OAuth2User user) {
		super(user, UserRole.TELEPHONEATTENTION.name());
		this.city = user.getAttribute("city");
	}

	public TelephoneAttention(User user) {
		super(user, UserRole.TELEPHONEATTENTION.name());
		this.city = user.city;
	}

	public TelephoneAttention() {}

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	 @Override
	 public com.microsoft.graph.models.User toAzureUser() {
		 com.microsoft.graph.models.User user = super.toAzureUser();
	     user.city = this.city;
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
