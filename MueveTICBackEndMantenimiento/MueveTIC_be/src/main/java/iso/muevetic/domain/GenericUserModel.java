package iso.muevetic.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public class GenericUserModel {

	@Valid
	private DniModel dni;
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	private String city;
	@NotBlank
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,64}$")
	private String password;

	public DniModel getDni() {
		return dni;
	}
	
	public void setDni(DniModel dni) {
		this.dni = dni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}
	
	public void setCiy(String city) {
		this.city = city;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
