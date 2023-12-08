package iso.muevetic.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;


public class RateModel {

	@Min(0)
	@Max(5)
	private int rating;

	@Size(min=0, max=255)
	@NotBlank
	private String comment;

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
