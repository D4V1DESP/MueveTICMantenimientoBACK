package iso.muevetic.entities;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import iso.muevetic.enums.ReserveState;


@Document(value = "reserves")
public class Reserve {

	private int rating;

	@Id
	private String id;

	private String userId;
	private String comment;

	private Date start;
	private Date end;

	@DBRef
	private GenericVehicle vehicle;
	private ReserveState state;
	
	public Reserve() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public GenericVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(GenericVehicle vehicle) {
		this.vehicle = vehicle;
	}

	public ReserveState getState() {
		return state;
	}

	public void setState(ReserveState state) {
		this.state = state;
	}

}
