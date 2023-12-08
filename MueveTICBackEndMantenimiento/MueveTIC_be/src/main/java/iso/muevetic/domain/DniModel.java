package iso.muevetic.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DniModel {

	@Pattern(regexp = "[0-9]{8}[A-Z]{1}")
	@NotBlank
	private String dni;
	
	protected DniModel() { }

	public DniModel(String dni) {
		this.dni = dni;
	}

	public String getDni() {
		return dni;
	}

}
