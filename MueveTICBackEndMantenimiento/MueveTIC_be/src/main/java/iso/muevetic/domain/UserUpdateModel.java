package iso.muevetic.domain;

import java.util.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Past;


public class UserUpdateModel {

	@Valid
	private DniModel dni;
	private String name;
	private String surname;
	private String city;
	private String drivingLicense;
	private Integer experience;
	@Min(value = 100000000)
	@Max(value = 999999999)
	private Integer telephone;
	@Past
	private Date birthdate;


	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public Integer getTelephone() {
		return telephone;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public DniModel getDni() {
		return dni;
	}

	public String getCity() {
		return city;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setTelephone(Integer telephone) {
		this.telephone = telephone;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setDni(DniModel dni) {
		this.dni = dni;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

}
