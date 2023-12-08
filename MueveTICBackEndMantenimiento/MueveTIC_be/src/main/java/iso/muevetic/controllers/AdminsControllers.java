package iso.muevetic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import iso.muevetic.domain.AdminModel;
import iso.muevetic.domain.ConfigModel;
import iso.muevetic.domain.MaintenanceStaffModel;
import iso.muevetic.domain.UserUpdateModel;
import iso.muevetic.entities.GenericUser;
import iso.muevetic.entities.SystemConfig;
import iso.muevetic.exceptions.ConflictInDBException;
import iso.muevetic.exceptions.NotFoundException;
import iso.muevetic.services.AdminsService;
import iso.muevetic.services.ConfigService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("admins")
public class AdminsControllers {

	@Autowired
	AdminsService adminsService;
	
	@Autowired
	ConfigService configService;

	@PostMapping("/createAdmin/")
	public void createAdmin(@Valid @RequestBody AdminModel adminModel) {
		try {
			this.adminsService.createAdmin(adminModel);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/createMaintenance/")
	public void createMaintenance(@Valid @RequestBody MaintenanceStaffModel maintenanceStaffModel) {
		try {
			this.adminsService.createMaintenanceStaff(maintenanceStaffModel);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/users/{id}")
	public Object getUserInfo(@PathVariable String id) {
		try {
			return this.adminsService.getUserById(id);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/users/{id}")
	public void blockUser(@PathVariable String id) {
		try {
			this.adminsService.blockUser(id);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}
	
	@PatchMapping("/users/{id}")
	public GenericUser updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateModel userUpdateModel) {
		try {
			return this.adminsService.updateUser(id, userUpdateModel);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/users/")
	public List<GenericUser> getAllUsers(@RequestParam("type") String type) {
		return this.adminsService.getUsersByType(type);
	}

	@PatchMapping("/users/reactivate/{id}")
	public void reactivateUser(@PathVariable String id) {
		try {
			this.adminsService.reactivateUser(id);
		} catch (ConflictInDBException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/SystemConfig")
	public SystemConfig getConfig() {
		
		return this.configService.getConfig();
	}
	
	@PutMapping("/SystemConfig")
	public SystemConfig updateConfig(@RequestBody ConfigModel configModel) {
		
		return this.configService.updateConfig(configModel);
	}

}
