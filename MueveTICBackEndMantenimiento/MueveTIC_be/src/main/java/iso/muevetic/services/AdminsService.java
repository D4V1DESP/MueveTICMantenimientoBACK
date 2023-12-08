package iso.muevetic.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.graph.requests.DirectoryObjectCollectionWithReferencesPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserRequestBuilder;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.DirectoryObject;
import com.microsoft.graph.models.PasswordProfile;
import com.microsoft.graph.models.User;

import iso.muevetic.domain.AdminModel;
import iso.muevetic.domain.MaintenanceStaffModel;
import iso.muevetic.domain.UserUpdateModel;
import iso.muevetic.entities.Admin;
import iso.muevetic.entities.GenericUser;
import iso.muevetic.entities.MaintenanceStaff;
import iso.muevetic.entities.SystemConfig;
import iso.muevetic.exceptions.ConflictInDBException;
import iso.muevetic.exceptions.NotFoundException;
import jakarta.validation.Valid;

import okhttp3.Request;


@Service
public class AdminsService {
	
	@Autowired
	private GraphServiceClient<Request> graphClient;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ConfigService configService;

	private static final String EXTENSIONS_PREFIX = "extension_005961277dab49a08635542189bac000_";

	private static final String USER_SELECT_QUERY_ATTRIBUTE = 
			"identities,"
			+ "id,"
			+ "surname,"
			+ "givenName,"
			+ "city,"
			+ "accountEnabled,"
			+ "mail,"
			+ EXTENSIONS_PREFIX + "DNI,"
			+ EXTENSIONS_PREFIX + "Telefono,"
			+ EXTENSIONS_PREFIX + "CarnetConducir,"
			+ EXTENSIONS_PREFIX + "Experiencia,"
			+ EXTENSIONS_PREFIX + "Nacimiento";
	
	public String createAdmin(AdminModel adminModel) throws ConflictInDBException {
		
		PasswordProfile passwordProfile = new PasswordProfile();
		passwordProfile.forceChangePasswordNextSignIn = false;
		passwordProfile.password = adminModel.getPassword();
		
		SystemConfig config = this.configService.getConfig();
		Admin admin = this.modelMapper.map(adminModel, Admin.class, config.getAdminRoleId());
		admin.setRole(config.getAdminRoleId());
		
		com.microsoft.graph.models.User azureAdmin = admin.toAzureUser();
		azureAdmin.accountEnabled = true;
		azureAdmin.passwordProfile = passwordProfile;
		
		try {
			com.microsoft.graph.models.User azureAdded = graphClient.users().buildRequest().post(azureAdmin);
			graphClient.groups(config.getAdminRoleId()).members().references().buildRequest().post(azureAdded);
			admin = new Admin(azureAdded);
		} catch (ClientException e) {
			e.printStackTrace();
			throw new ConflictInDBException();
		}
		
		return admin.getId();
		
	}

	public String createMaintenanceStaff(@Valid MaintenanceStaffModel maintenanceStaffModel) throws ConflictInDBException {
		
		PasswordProfile passwordProfile = new PasswordProfile();
		passwordProfile.forceChangePasswordNextSignIn = false;
		passwordProfile.password = maintenanceStaffModel.getPassword();
		
		MaintenanceStaff staff = this.modelMapper.map(maintenanceStaffModel, MaintenanceStaff.class);
		
		com.microsoft.graph.models.User azureStaff = staff.toAzureUser();
		azureStaff.accountEnabled = true;
		azureStaff.passwordProfile = passwordProfile;
		
		try {
			SystemConfig config = this.configService.getConfig();
			
			com.microsoft.graph.models.User azureAdded = graphClient.users().buildRequest().post(azureStaff);
			graphClient.groups(config.getMaintenanceStaffRoleId()).members().references().buildRequest().post(azureAdded);
			staff = new MaintenanceStaff(azureAdded);
		} catch (ClientException e) {
			e.printStackTrace();
			throw new ConflictInDBException();
		}
		
		return staff.getId();
	}

	public void blockUser(String id) throws ConflictInDBException {
		User user = new User();
		user.accountEnabled = false;
		try {
			graphClient.users(id).buildRequest().patch(user);
		} catch (ClientException e) {
			throw new ConflictInDBException();
		}
	}

	public void reactivateUser(String id) throws ConflictInDBException {
		User user = new User();
		user.accountEnabled = true;
		try {
			graphClient.users(id).buildRequest().patch(user);
		} catch (ClientException e) {
			throw new ConflictInDBException();
		}
	}

	public GenericUser getUserById(String userId) throws NotFoundException {
		try {
	     	UserRequestBuilder req = this.graphClient.users(userId);
	    	com.microsoft.graph.models.User user = req.buildRequest().select(USER_SELECT_QUERY_ATTRIBUTE).get();
	    	DirectoryObjectCollectionWithReferencesPage groups = req.memberOf().buildRequest().get();
	    	List<DirectoryObject> roles = groups.getCurrentPage();
	    	String role = "";

	    	if (!roles.isEmpty()) {
	    		role = roles.get(0).id;
	    	}

	    	GenericUser generic = null;

	    	SystemConfig config = this.configService.getConfig();
	    	
			if (role.equals(config.getAdminRoleId())) {
				generic = new Admin(user);
	    	} else if (role.equals(config.getMaintenanceStaffRoleId())) {
	    		generic = new MaintenanceStaff(user);
	    	} else {
	    		generic = new iso.muevetic.entities.User(user);
	    	}
			
    		return generic;
	    } catch(ClientException e) {
	    	e.printStackTrace();
	    	throw new NotFoundException();
	    }
	}
	
	public List<GenericUser> getUsersByType(String type) {
		try {
			ArrayList<GenericUser> buf = new ArrayList<>();

			if (type.equalsIgnoreCase("ALL")) {
		    	List<com.microsoft.graph.models.User> all = this.graphClient.users().buildRequest().select(USER_SELECT_QUERY_ATTRIBUTE).get().getCurrentPage();
		    	
		    	for (int i = 0; i < all.size(); i++) {
		    		iso.muevetic.entities.User generic = new iso.muevetic.entities.User(all.get(i));
		    		if (!buf.contains(generic)) {
		    			buf.add(generic);
		    		}
		    	}
		    	
		    	return buf;
			}
			SystemConfig config = this.configService.getConfig();			
	    	List<DirectoryObject> group = graphClient.groups(config.getAdminRoleId())
	    		.members()
	    		.buildRequest()
	    		.select(USER_SELECT_QUERY_ATTRIBUTE)
	    		.get().getCurrentPage();
	    	
	    	for (int i = 0; i < group.size(); i++) {
	    		DirectoryObject user = group.get(i);
	    		buf.add(new Admin((com.microsoft.graph.models.User) user));
	    	}
	    	
	    	group = graphClient.groups(config.getMaintenanceStaffRoleId())
	    		.members()
	    		.buildRequest()
	    		.select(USER_SELECT_QUERY_ATTRIBUTE)
	    		.get().getCurrentPage();
	    	
	    	for (int i = 0; i < group.size(); i++) {
	    		DirectoryObject user = group.get(i);
	    		buf.add(new MaintenanceStaff((com.microsoft.graph.models.User) user));
	    	}

	    	List<com.microsoft.graph.models.User> all = this.graphClient.users().buildRequest().select(USER_SELECT_QUERY_ATTRIBUTE).get().getCurrentPage();
	    	
	    	for (int i = 0; i < all.size(); i++) {
	    		iso.muevetic.entities.User generic = new iso.muevetic.entities.User(all.get(i));
	    		if (!buf.contains(generic)) {
	    			buf.add(generic);
	    		}
	    	}

	    	return buf.stream().filter(user -> user.getRole().equals(type.toUpperCase())).toList();
	    } catch(ClientException e) {
	    	e.printStackTrace();
	    	return List.of();
	    }
	}

	public GenericUser updateUser(String userId, UserUpdateModel updateModel) throws NotFoundException, ConflictInDBException {
		GenericUser generic = getUserById(userId);

		modelMapper.map(updateModel, generic);
		
		User azureUser = generic.toAzureUser();
		
		azureUser.userPrincipalName = null;

		try {
			graphClient.users(userId).buildRequest().patch(azureUser);
		} catch (ClientException e) {
			e.printStackTrace();
			throw new ConflictInDBException();
		}

		return generic;
	}

	public void deleteUser(String userId) throws NotFoundException {
		try {
			graphClient.users(userId).buildRequest().delete();
		} catch (ClientException e) {
			throw new NotFoundException();
		}
	}

}
