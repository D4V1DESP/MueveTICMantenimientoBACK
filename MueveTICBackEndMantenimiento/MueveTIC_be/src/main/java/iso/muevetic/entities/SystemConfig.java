package iso.muevetic.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Document(collection = "config")
public class SystemConfig {
	
	@Id
	@JsonIgnore
	private String id;
	
	//JWTAuthoritationFilter data
	@JsonIgnore
	private String adminRoleId;
	@JsonIgnore
    private String maintenanceStaffRoleId;
	@JsonIgnore
	private String telephoneAttentionRoleID;
    
    //SecurityConfig 
	@JsonIgnore
    private String clientId;
    @JsonIgnore
    private String clientSecret;
    @JsonIgnore
    private String tenantId;
    
    //GenericUser
    @JsonIgnore
    private String azureExtension;
    
    //Parameters
    private int minBattery;
    private int maxMaintenance;
    private int ridePrice;
    private int batteryPerRide;

    public String getId() {
    	return id;
    }
    
	public String getAdminRoleId() {
		return adminRoleId;
	}

	public String getMaintenanceStaffRoleId() {
		return maintenanceStaffRoleId;
	}

	public String gettelephoneAttentionRoleID() {
		return telephoneAttentionRoleID;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getAzureExtension() {
		return azureExtension;
	}

	public int getMinBattery() {
		return minBattery;
	}
	
	public void setMinBattery(int minBattery) {
		this.minBattery = minBattery;
	}

	public int getMaxMaintenance() {
		return maxMaintenance;
	}
	
	public void setMaxMaintenance(int maxMaintenance) {
		this.maxMaintenance = maxMaintenance;
	}

	public int getRidePrice() {
		return ridePrice;
	}
	
	public void setRidePrice(int ridePrice) {
		this.ridePrice = ridePrice;
	}

	public int getBatteryPerRide() {
		return batteryPerRide;
	}
	
	public void setBatteryPerRide(int batteryPerRide) {
		this.batteryPerRide = batteryPerRide;
	}

}
